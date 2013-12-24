package vc4.api.permissions;

import java.io.IOException;
import java.io.InputStream;

import vc4.api.list.IntList;
import vc4.api.yaml.YamlMap;

public interface PermissionGroup {
	
	public int getP();
	public int getK();
	public int getSubPermission(String perm);
	public void permissionCalc(IntList ints, String perm);
	public void setP(int p);
	public void setK(int k);
	public void setSubPermission(String perm, int on);
	public void loadPermissions(String...perms);
	public void loadPermissions(InputStream in) throws IOException;
	public void loadPermissions(YamlMap in);
	public String[] list();
	public YamlMap toYaml();
	
	

}
