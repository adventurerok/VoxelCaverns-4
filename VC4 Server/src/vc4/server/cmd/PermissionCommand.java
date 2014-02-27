package vc4.server.cmd;

import vc4.api.cmd.Command;
import vc4.api.cmd.CommandHandler;
import vc4.api.permissions.DefaultPermissions;
import vc4.server.user.UserGroup;
import vc4.server.user.UserInfo;
import vc4.server.user.UserManager;

public class PermissionCommand implements CommandHandler {

	@Override
	public void handleCommand(Command command) {
		String sub = command.getArg(0).toLowerCase();
		switch (sub) {
			case "list":
				list(command);
				return;
			case "get":
				get(command);
				return;
			case "set":
				set(command);
				return;
			case "clear":
				clear(command);
				return;
		}
		command.getSender().message("{l:cmd.perms.invalid," + sub + "}");
	}

	private void clear(Command command) {
		if (command.getArgsLength() < 2) {
			command.getSender().message("{l:cmd.perms.wrongargs,clear," + 2 + "}");
			return;
		}
		String type = command.getArg(1);
		switch (type) {
			case "user":
				clearUser(command);
				return;
			case "group":
				clearGroup(command);
				return;
		}
		command.getSender().message("{l:cmd.perms.invalid," + type + "}");
	}

	private void set(Command command) {
		if (command.getArgsLength() < 2) {
			command.getSender().message("{l:cmd.perms.wrongargs,set," + 2 + "}");
			return;
		}
		String type = command.getArg(1);
		switch (type) {
			case "default":
				setDefault(command);
				return;
			case "group":
				setGroup(command);
				return;
			case "user":
				setUser(command);
				return;
		}
		command.getSender().message("{l:cmd.perms.invalid," + type + "}");
	}

	private void get(Command command) {
		if (command.getArgsLength() < 2) {
			command.getSender().message("{l:cmd.perms.wrongargs,get," + 2 + "}");
			return;
		}
		String type = command.getArg(1);
		switch (type) {
			case "default":
				getDefault(command);
				return;
			case "group":
				getGroup(command);
				return;
			case "user":
				getUser(command);
				return;
		}
		command.getSender().message("{l:cmd.perms.invalid," + type + "}");
	}

	private void list(Command command) {
		if (command.getArgsLength() < 2) {
			command.getSender().message("{l:cmd.perms.wrongargs,list," + 2 + "}");
			return;
		}
		String type = command.getArg(1);
		switch (type) {
			case "default":
				listDefault(command);
				return;
			case "group":
				listGroup(command);
				return;
			case "user":
				listUser(command);
				return;
		}
		command.getSender().message("{l:cmd.perms.invalid," + type + "}");
	}

	private void clearUser(Command command) {
		if (command.getArgsLength() < 3) {
			command.getSender().message("{l:cmd.perms.wrongargs,clear user," + 3 + "}");
			return;
		}
		UserInfo info = UserManager.getUserInfo(command.getArg(2));
		if (info == null) {
			command.getSender().message("{l:cmd.perms.nouser," + command.getArg(2) + "}");
			return;
		}
		if (info.getPermissions() == null) {
			command.getSender().message("{l:cmd.perms.none," + command.getArg(2) + "}");
			return;
		}
		info.getPermissions().clear();
		command.getSender().message("{l:cmd.perms.clear.user," + command.getArg(2) + "}");

	}

	private void clearGroup(Command command) {
		if (command.getArgsLength() < 3) {
			command.getSender().message("{l:cmd.perms.wrongargs,clear group," + 3 + "}");
			return;
		}
		UserGroup g = UserManager.getGroup(command.getArg(2));
		if (g == null) {
			command.getSender().message("{l:cmd.perms.nogroup," + command.getArg(2) + "}");
			return;
		}
		if (g.getPermissions() == null) {
			command.getSender().message("{l:cmd.perms.none," + command.getArg(2) + "}");
			return;
		}
		g.getPermissions().clear();
		command.getSender().message("{l:cmd.perms.clear.group," + command.getArg(2) + "}");
	}

	private void listUser(Command command) {
		if (command.getArgsLength() < 3) {
			command.getSender().message("{l:cmd.perms.wrongargs,list user," + 3 + "}");
			return;
		}
		UserInfo info = UserManager.getUserInfo(command.getArg(2));
		if (info == null) {
			command.getSender().message("{l:cmd.perms.nouser," + command.getArg(2) + "}");
			return;
		}
		command.getSender().message("{l:cmd.perms.list.user," + command.getArg(2) + "}");
		if (info.getPermissions() == null) {
			command.getSender().message("\t{l:cmd.perms.list.none}");
			command.getSender().message("\t{l:cmd.perms.list.alsogroup," + info.getGroupName() + "}");
			return;
		}
		String def[] = info.getPermissions().list();
		if (def.length < 1) command.getSender().message("\t{l:cmd.perms.list.none}");
		for (String s : def)
			command.getSender().message("\t" + s);
		command.getSender().message("\t{l:cmd.perms.list.alsogroup," + info.getGroupName() + "}");
	}

	private void listGroup(Command command) {
		if (command.getArgsLength() < 3) {
			command.getSender().message("{l:cmd.perms.wrongargs,list group," + 3 + "}");
			return;
		}
		UserGroup g = UserManager.getGroup(command.getArg(2));
		if (g == null) {
			command.getSender().message("{l:cmd.perms.nogroup," + command.getArg(2) + "}");
			return;
		}
		command.getSender().message("{l:cmd.perms.list.group," + command.getArg(2) + "}");
		String def[] = g.getPermissions().list();
		if (def.length < 1) command.getSender().message("\t{l:cmd.perms.list.none}");
		for (String s : def)
			command.getSender().message("\t" + s);
	}

	private void listDefault(Command command) {
		command.getSender().message("{l:cmd.perms.list.default}");
		String def[] = DefaultPermissions.getPerms().list();
		if (def.length < 1) command.getSender().message("\t{l:cmd.perms.list.none}");
		for (String s : def)
			command.getSender().message("\t" + s);
	}

	private void getDefault(Command command) {
		if (command.getArgsLength() < 3) {
			command.getSender().message("{l:cmd.perms.wrongargs,get default," + 3 + "}");
			return;
		}
		command.getSender().message("{l:cmd.perms.list.default}");
		for (int d = 2; d < command.getArgsLength(); ++d) {
			String perm = command.getArg(d);
			int i = DefaultPermissions.getPermission(perm);
			String out = "not-set";
			if (i == 1) out = "true";
			else if (i == -1) out = "false";
			command.getSender().message("\t" + perm + ": " + out);
		}
	}

	private void getGroup(Command command) {
		if (command.getArgsLength() < 4) {
			command.getSender().message("{l:cmd.perms.wrongargs,get group," + 3 + "}");
			return;
		}
		UserGroup g = UserManager.getGroup(command.getArg(2));
		if (g == null) {
			command.getSender().message("{l:cmd.perms.nogroup," + command.getArg(2) + "}");
			return;
		}
		command.getSender().message("{l:cmd.perms.list.group," + command.getArg(2) + "}");
		for (int d = 3; d < command.getArgsLength(); ++d) {
			String perm = command.getArg(d);
			int i = g.getPermission(perm);
			if (i == 0) i = DefaultPermissions.getPermission(perm);
			String out = "not-set";
			if (i == 1) out = "true";
			else if (i == -1) out = "false";
			command.getSender().message("\t" + perm + ": " + out);
		}
	}

	private void getUser(Command command) {
		if (command.getArgsLength() < 4) {
			command.getSender().message("{l:cmd.perms.wrongargs,get user," + 3 + "}");
			return;
		}
		UserInfo info = UserManager.getUserInfo(command.getArg(2));
		if (info == null) {
			command.getSender().message("{l:cmd.perms.nouser," + command.getArg(2) + "}");
			return;
		}
		command.getSender().message("{l:cmd.perms.list.user," + command.getArg(2) + "}");
		for (int d = 3; d < command.getArgsLength(); ++d) {
			String perm = command.getArg(d);
			int i = info.getPermission(perm);
			if (i == 0) i = info.getGroup().getPermission(perm);
			if (i == 0) i = DefaultPermissions.getPermission(perm);
			String out = "not-set";
			if (i == 1) out = "true";
			else if (i == -1) out = "false";
			command.getSender().message("\t" + perm + ": " + out);
		}

	}

	private void setDefault(Command command) {
		if (command.getArgsLength() < 3) {
			command.getSender().message("{l:cmd.perms.wrongargs,set default," + 3 + "}");
			return;
		}
		command.getSender().message("{l:cmd.perms.list.default}");
		for (int d = 2; d < command.getArgsLength(); ++d) {
			String perm = command.getArg(d);
			String parts[] = perm.split(":");
			if (parts.length != 2) {
				command.getSender().message("{l:cmd.perms.invalid," + perm + "}");
				return;
			}
			parts[1] = parts[1].toLowerCase();
			int set = 0;
			if (parts[1].equals("true") || parts[1].equals("1")) set = 1;
			else if (parts[1].equals("not-set") || parts[1].equals("null") || parts[1].equals("0")) set = 0;
			else if (parts[1].equals("false") || parts[1].equals("-1")) set = -1;
			else {
				command.getSender().message("{l:cmd.perms.invalid," + parts[1] + "}");
				return;
			}
			DefaultPermissions.setPermission(parts[0], set);
			String out = "not-set";
			if (set == 1) out = "true";
			else if (set == -1) out = "false";
			command.getSender().message("\t" + parts[0] + ": " + out);
		}
	}

	private void setGroup(Command command) {
		if (command.getArgsLength() < 4) {
			command.getSender().message("{l:cmd.perms.wrongargs,set group," + 3 + "}");
			return;
		}
		UserGroup g = UserManager.getGroup(command.getArg(2));
		if (g == null) {
			command.getSender().message("{l:cmd.perms.nogroup," + command.getArg(2) + "}");
			return;
		}
		command.getSender().message("{l:cmd.perms.list.group," + command.getArg(2) + "}");
		for (int d = 2; d < command.getArgsLength(); ++d) {
			String perm = command.getArg(d);
			String parts[] = perm.split(":");
			if (parts.length != 2) {
				command.getSender().message("{l:cmd.perms.invalid," + perm + "}");
				return;
			}
			parts[1] = parts[1].toLowerCase();
			int set = 0;
			if (parts[1].equals("true") || parts[1].equals("1")) set = 1;
			else if (parts[1].equals("not-set") || parts[1].equals("null") || parts[1].equals("0")) set = 0;
			else if (parts[1].equals("false") || parts[1].equals("-1")) set = -1;
			else {
				command.getSender().message("{l:cmd.perms.invalid," + parts[1] + "}");
				return;
			}
			g.setPermission(parts[0], set);
			String out = "not-set";
			if (set == 1) out = "true";
			else if (set == -1) out = "false";
			command.getSender().message("\t" + parts[0] + ": " + out);
		}
	}

	private void setUser(Command command) {
		if (command.getArgsLength() < 4) {
			command.getSender().message("{l:cmd.perms.wrongargs,set user," + 3 + "}");
			return;
		}
		UserInfo info = UserManager.getUserInfo(command.getArg(2));
		if (info == null) {
			command.getSender().message("{l:cmd.perms.nouser," + command.getArg(2) + "}");
			return;
		}
		command.getSender().message("{l:cmd.perms.list.user," + command.getArg(2) + "}");
		for (int d = 2; d < command.getArgsLength(); ++d) {
			String perm = command.getArg(d);
			String parts[] = perm.split(":");
			if (parts.length != 2) {
				command.getSender().message("{l:cmd.perms.invalid," + perm + "}");
				return;
			}
			parts[1] = parts[1].toLowerCase();
			int set = 0;
			if (parts[1].equals("true") || parts[1].equals("1")) set = 1;
			else if (parts[1].equals("not-set") || parts[1].equals("null") || parts[1].equals("0")) set = 0;
			else if (parts[1].equals("false") || parts[1].equals("-1")) set = -1;
			else {
				command.getSender().message("{l:cmd.perms.invalid," + parts[1] + "}");
				return;
			}
			info.setPermission(parts[0], set);
			String out = "not-set";
			if (set == 1) out = "true";
			else if (set == -1) out = "false";
			command.getSender().message("\t" + parts[0] + ": " + out);
		}

	}

}
