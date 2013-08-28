package vc4.impl.cmd;

import java.util.HashMap;

import vc4.api.cmd.*;
import vc4.impl.plugin.PluginManager;

public class CommandExecutor implements CommandHandler{

	static HashMap<String, ExecutableCommand> engineCommands = new HashMap<>();
	static CommandExecutor handle = new CommandExecutor();
	
	static{
		addCommand(new ExecutableCommand(new CommandInfo("tp").setUsage("<x> <y> <z>").setDescription("{l:cmd.tp.desc}").setCommandUsage(new CommandUsage().setRequiresUser(true).setMinimumArgs(3).setMaximumArgs(3).setArgumentChecks(CommandArgument.DOUBLE, CommandArgument.DOUBLE, CommandArgument.DOUBLE)), handle));
	}
	
	static void addCommand(ExecutableCommand cmd){
		engineCommands.put(cmd.getInfo().getName(), cmd);
		for(String s : cmd.getInfo().getAliases()){
			engineCommands.put(s, cmd);
		}
	}
	
	public static void executeCommand(Command command){
		try{
			ExecutableCommand e = engineCommands.get(command.getCommand());
			if(e == null){
				PluginManager.handleCommand(command);
				return;
			}
			if(!e.getInfo().getCommandUsage().check(command)) return;
			e.getHandler().handleCommand(command);
		} catch(Exception e){
			command.getSender().message("{l:cmd.fail}");
		}
	}

	@Override
	public void handleCommand(Command command) {
		if(command.getCommand().equals("tp")){
			double x = command.getArgAsDouble(0, 0);
			double y = command.getArgAsDouble(1, 0);
			double z = command.getArgAsDouble(2, 0);
			command.getSender().getPlayer().teleport(x, y, z);
			command.getSender().message("{l:cmd.tp.done," + x + "," + y + "," + z + "}");
		}
	}
}
