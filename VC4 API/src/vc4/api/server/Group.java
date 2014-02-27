package vc4.api.server;

import vc4.api.permissions.PermissionGroup;

public interface Group {

	public String getName();

	public String getChatPrefix();

	public int getPermission(String perm);

	public void setPermission(String perm, int on);

	public PermissionGroup getPermissions();
}
