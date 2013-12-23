package vc4.api.permissions;

import java.io.IOException;
import java.io.InputStream;

import vc4.api.list.IntList;
import vc4.api.yaml.YamlMap;

public interface PermissionGroup {
	
	public int getP();
	public int getK();
	public boolean getSubPermission(String perm);
	public void permissionCalc(IntList ints, String perm);
	public void setP(int p);
	public void setK(int k);
	public void setSubPermission(String perm, int on);
	public abstract void loadPermissions(String...perms);
	public abstract void loadPermissions(InputStream in) throws IOException;
	public abstract void loadPermissions(YamlMap in);
	
	

}
