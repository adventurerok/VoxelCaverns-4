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
		switch(sub){
		case "list":
			list(command);
			return;
		case "get":
			get(command);
			return;
		case "set":
			set(command);
			return;
		}
	}
	
	private void set(Command command) {
		// TODO Auto-generated method stub
		
	}

	private void get(Command command) {
		// TODO Auto-generated method stub
		
	}

	private void list(Command command){
		if(command.getArgsLength() < 2){
			command.getSender().message("{l:cmd.perms.wrongargs,list," + 2 + "}");
			return;
		}
		String type = command.getArg(1);
		switch(type){
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
	}

	private void listUser(Command command) {
		if(command.getArgsLength() < 3){
			command.getSender().message("{l:cmd.perms.wrongargs,list user," + 3 + "}");
			return;
		}
		UserInfo info = UserManager.getUserInfo(command.getArg(2));
		if(info == null){
			command.getSender().message("{l:cmd.perms.nouser," + command.getArg(2) + "}");
			return;
		}
		command.getSender().message("Permissions for user \"" + command.getArg(2) + "\":");
		if(info.getPermissions() == null){
			command.getSender().message("\t(There are none)");
			command.getSender().message("\t(User is in group \"" + info.getGroupName() + "\", their permissions also apply)");
			return;
		}
		String def[] = info.getPermissions().list();
		if(def.length < 1) command.getSender().message("\t(There are none)");
		for(String s : def) command.getSender().message("\t" + s);
		command.getSender().message("\t(User is in group \"" + info.getGroupName() + "\", their permissions also apply)");
	}

	private void listGroup(Command command) {
		if(command.getArgsLength() < 3){
			command.getSender().message("{l:cmd.perms.wrongargs,list group," + 3 + "}");
			return;
		}
		UserGroup g = UserManager.getGroup(command.getArg(2));
		if(g == null){
			command.getSender().message("{l:cmd.perms.nogroup," + command.getArg(2) + "}");
			return;
		}
		command.getSender().message("Permissions for group \"" + g.getName() + "\":");
		String def[] = g.getPermissions().list();
		if(def.length < 1) command.getSender().message("\t(There are none)");
		for(String s : def) command.getSender().message("\t" + s);
	}

	private void listDefault(Command command) {
		command.getSender().message("Default permissions: ");
		String def[] = DefaultPermissions.getPerms().list();
		if(def.length < 1) command.getSender().message("\t(There are none)");
		for(String s : def) command.getSender().message("\t" + s);
	}

}
