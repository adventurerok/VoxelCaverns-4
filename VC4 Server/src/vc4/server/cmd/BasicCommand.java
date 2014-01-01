package vc4.server.cmd;

import vc4.api.cmd.Command;
import vc4.api.cmd.CommandHandler;
import vc4.server.chat.NameFilter;

public class BasicCommand implements CommandHandler {

	@Override
	public void handleCommand(Command command) {
		switch(command.getCommand()){
		case "name":
		case "nick":
			cmd_name(command);
			return;
		}
	}
	
	private void cmd_name(Command command){
		String name = command.getArg(0);
		if(!NameFilter.filter(name)){
			command.getSender().message("{l:cmd.name.invalid,"+name+"}");
		}
		boolean any = false;
		if(command.hasArg(1)){
			if(command.getArg(1).toLowerCase().equals("any")){
				any = true;
			} else {
				command.getSender().message("{l:cmd.invalidarg,"+command.getArg(1)+"}");
				return;
			}
		}
		boolean b = command.getSender().changeChatName(name, any);
		if(b) command.getSender().message("{l:cmd.name.success,"+command.getSender().getChatName()+"}");
		else {
			if(any) command.getSender().message("{l:cmd.name.fail}");
			else command.getSender().message("{l:cmd.name.taken}");
		}
	}

}
