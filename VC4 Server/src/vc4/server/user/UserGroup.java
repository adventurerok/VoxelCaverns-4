package vc4.server.user;

import vc4.api.permissions.PermissionGroup;
import vc4.api.server.Group;
import vc4.api.yaml.YamlMap;
import vc4.impl.permissions.ImplPermissionGroup;

public class UserGroup implements Group {

	private String name;
	private String prefix;
	private String inherit;
	private PermissionGroup permissions;

	public UserGroup(String name, String prefix, PermissionGroup permissions) {
		super();
		this.name = name;
		this.prefix = prefix;
		this.permissions = permissions;
	}

	public UserGroup(String name, YamlMap map) {
		this.name = name;
		if (map.hasKey("prefix")) prefix = map.getString("prefix");
		if (map.hasKey("inherit")) {
			inherit = map.getString("inherit");
			if (inherit != null && inherit.equals(name)) inherit = null;
		} else prefix = "";
		if (map.hasKey("permissions")) {
			permissions = new ImplPermissionGroup(map.getSubMap("permissions"));
		} else permissions = new ImplPermissionGroup();
	}

	public void setInherit(String inherit) {
		if (inherit != null && inherit.equals(name)) throw new RuntimeException("Can't inherit from self");
		this.inherit = inherit;
	}

	public void setChatPrefix(String prefix) {
		this.prefix = prefix;
	}

	public YamlMap toYaml() {
		YamlMap map = new YamlMap();
		if (prefix != null && !prefix.isEmpty()) map.setString("prefix", prefix);
		if (inherit != null && !inherit.isEmpty()) map.setString("inherit", inherit);
		if (permissions != null) {
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
		int i = permissions.getSubPermission(perm);
		if (i == 0 && inherit != null && !inherit.isEmpty()) return UserManager.getGroup(inherit).getPermission(perm);
		return 0;
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
