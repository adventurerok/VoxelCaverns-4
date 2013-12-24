package vc4.api.permissions;

public class DefaultPermissions {

	
	private static PermissionGroup perms;
	
	public static void setPerms(PermissionGroup perms) {
		DefaultPermissions.perms = perms;
	}
	
	public static int getPermission(String perm){
		return perms.getSubPermission(perm);
	}
	
	public static void setPermission(String perm, int change){
		perms.setSubPermission(perm, change);
	}
	
	public static PermissionGroup getPerms() {
		return perms;
	}
	
}
