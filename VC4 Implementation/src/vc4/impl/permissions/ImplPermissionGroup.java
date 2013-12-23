package vc4.impl.permissions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import vc4.api.list.IntList;
import vc4.api.permissions.PermissionGroup;
import vc4.api.yaml.YamlMap;

public class ImplPermissionGroup implements PermissionGroup {
	
	/*
	 * 0 = unset
	 * 1 = on
	 * -1 = off
	 */
	
	private int ordinal(int o1, int o2){
		return o2 != 0 ? o2 : o1;
	}
	
	private int ordinal(int...ints){
		if(ints == null) return 0;
		while(ints.length > 1){
			ints[ints.length - 2] = ordinal(ints[ints.length - 2], ints[ints.length - 1]);
			ints = Arrays.copyOf(ints, ints.length - 1);
		}
		return ints[0];
	}
	
	public ImplPermissionGroup() {
		
	}
	
	public ImplPermissionGroup(YamlMap in){
		loadPermissions(in);
	}
	
	@Override
	public void loadPermissions(YamlMap in){
		for(Entry<Object, Object> ent : in.getBaseMap().entrySet()){
			String perm = ent.getKey().toString();
			int val = 0;
			if(ent.getValue() instanceof Boolean) val = ((Boolean)ent.getValue()).booleanValue() ? 1 : -1;
			setSubPermission(perm, val);
		}
	}
	
	public ImplPermissionGroup(InputStream in) throws IOException{
		loadPermissions(in);
	}
	
	@Override
	public void loadPermissions(InputStream in) throws IOException{
		BufferedReader read = new BufferedReader(new InputStreamReader(in));
		String line;
		int on;
		while((line = read.readLine()) != null){
			line = line.trim();
			if(line.startsWith("#")) continue;
			on = 1;
			if(line.startsWith("+")){
				on = 1;
				line = line.substring(1);
			} else if(line.startsWith("=")){
				on = 0;
				line = line.substring(1);
			}
			else if(line.startsWith("-")){
				on = -1;
				line = line.substring(1);
			}
			setSubPermission(line, on);
		}
	}
	
	public ImplPermissionGroup(String...perms) throws IOException{
		loadPermissions(perms);
	}
	
	@Override
	public void loadPermissions(String...perms){
		int on;
		for(String line : perms){
			if(line == null) continue;
			line = line.trim();
			if(line.startsWith("#")) continue;
			on = 1;
			if(line.startsWith("+")){
				on = 1;
				line = line.substring(1);
			} else if(line.startsWith("=")){
				on = 0;
				line = line.substring(1);
			}
			else if(line.startsWith("-")){
				on = -1;
				line = line.substring(1);
			}
			setSubPermission(line, on);
		}
	}
	
	int p = 0;
	int k = 0;
	HashMap<String, Object> subs = new HashMap<>();
	@Override
	public int getP() {
		return p;
	}

	@Override
	public int getK() {
		return k;
	}

	@Override
	public void permissionCalc(IntList ints, String perm) {
		if(perm.contains(".")){
			ints.add(k);
			int dotIndex = perm.indexOf('.');
			String before = perm.substring(0, dotIndex);
			String after = perm.substring(dotIndex + 1, perm.length());
			Object sub = subs.get(before);
			if(sub == null) return;
			else if(sub instanceof PermissionGroup) ((PermissionGroup)sub).permissionCalc(ints, after);
		} else {
			ints.add(k);
			if(perm.equals("*")) return;
			Object sub = subs.get(perm);
			if(sub == null) return;
			else if(sub instanceof Number) ints.add(((Number)sub).intValue());
			else if(sub instanceof PermissionGroup) ints.add(((PermissionGroup)sub).getP());
		}
	}

	@Override
	public void setP(int p) {
		this.p = p;
	}

	@Override
	public void setK(int k) {
		this.k = k;
	}

	@Override
	public void setSubPermission(String perm, int on) {
		if(perm.contains(".")){
			int dotIndex = perm.indexOf('.');
			String before = perm.substring(0, dotIndex);
			String after = perm.substring(dotIndex + 1, perm.length());
			Object sub = subs.get(before);
			if(sub instanceof PermissionGroup){
				((PermissionGroup)sub).setSubPermission(after, on);
			} else if(sub instanceof Number){
				PermissionGroup gal = new ImplPermissionGroup();
				gal.setP(((Number)sub).intValue());
				gal.setSubPermission(after, on);
				subs.put(before, gal);
			} else {
				PermissionGroup gal = new ImplPermissionGroup();
				gal.setSubPermission(after, on);
				subs.put(before, gal);
			}
		} else {
			if(perm.equals("*")){
				k = on;
				return;
			}
			Object sub = subs.get(perm);
			if(sub == null || !(sub instanceof PermissionGroup)){
				subs.put(perm, Byte.valueOf((byte) on));
			} else {
				((PermissionGroup)sub).setP(on);
			}
		}
	}

	@Override
	public boolean getSubPermission(String perm) {
		IntList ints = new IntList();
		permissionCalc(ints, perm);
		int res = ordinal(ints.toArray());
		return res > 0 ? true : false;
	}
}
