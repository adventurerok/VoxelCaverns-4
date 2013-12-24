package vc4.server.user;

import vc4.api.permissions.PermissionGroup;
import vc4.api.server.Group;
import vc4.api.yaml.YamlMap;
import vc4.impl.permissions.ImplPermissionGroup;

public class UserGroup implements Group {
	
	private String name;
	private String prefix;
	private PermissionGroup permissions;
	
	

	public UserGroup(String name, String prefix, PermissionGroup permissions) {
		super();
		this.name = name;
		this.prefix = prefix;
		this.permissions = permissions;
	}
	
	public UserGroup(String name, YamlMap map){
		if(map.hasKey("prefix")) prefix = map.getString("prefix");
		else prefix = "";
		if(map.hasKey("permissions")){
			permissions = new ImplPermissionGroup(map.getSubMap("permissions"));
		} else permissions = new ImplPermissionGroup();
	}
	
	public YamlMap toYaml(){
		YamlMap map = new YamlMap();
		if(prefix != null && !prefix.isEmpty()) map.setString("prefix", prefix);
		if(permissions != null){
			map.setSubMap("permissions", permissions.toYaml());
		}
		return map;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getChatPrefix() {
		return prefix;
	}

	@Override
	public int getPermission(String perm) {
		return permissions.getSubPermission(perm);
	}

	@Override
	public void setPermission(String perm, int on) {
		permissions.setSubPermission(perm, on);
	}

	@Override
	public PermissionGroup getPermissions() {
		return permissions;
	}

}
