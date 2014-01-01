package vc4.impl.cmd;

import java.util.HashMap;

import vc4.api.VoxelCaverns;
import vc4.api.client.Client;
import vc4.api.cmd.Command;
import vc4.api.cmd.CommandArgument;
import vc4.api.cmd.CommandHandler;
import vc4.api.cmd.CommandInfo;
import vc4.api.cmd.CommandUsage;
import vc4.api.cmd.ExecutableCommand;
import vc4.api.logging.Level;
import vc4.api.logging.Logger;
import vc4.api.packet.Packet30MessageString;
import vc4.api.server.ServerConsole;
import vc4.api.sound.Audio;
import vc4.impl.plugin.PluginManager;

public class CommandExecutor implements CommandHandler {

	static HashMap<String, ExecutableCommand> engineCommands = new HashMap<>();
	static CommandExecutor handle = new CommandExecutor();

	static {
		addCommand(new ExecutableCommand(new CommandInfo("teleport")
				.setUsage("<x> <y> <z>")
				.setDescription("{l:cmd.tp.desc}")
				.addAlias("tp")
				.setCommandUsage(
						new CommandUsage()
								.setRequiredCapabilities(0b10)
								.setMinimumArgs(3)
								.setMaximumArgs(3)
								.setPermissions("vc4.cmd.tp")
								.setArgumentChecks(CommandArgument.DOUBLE,
										CommandArgument.DOUBLE,
										CommandArgument.DOUBLE)), handle));
		addCommand(new ExecutableCommand(new CommandInfo("sound")
				.setUsage("<name> [volume] [pitch]")
				.setDescription("{l:cmd.sound.desc}")
				.setCommandUsage(
						new CommandUsage()
								.setRequiredCapabilities(0b10)
								.setMinimumArgs(1)
								.setMaximumArgs(3)
								.setArgumentChecks(CommandArgument.STRING,
										CommandArgument.DOUBLE,
										CommandArgument.DOUBLE)), handle));
		addCommand(new ExecutableCommand(
				new CommandInfo("log")
						.setUsage("<name> <level> <message>")
						.setDescription("{l:cmd.log.desc}")
						.setCommandUsage(
								new CommandUsage().setMinimumArgs(3)
										.setArgumentChecks(
												CommandArgument.STRING,
												CommandArgument.STRING)),
				handle));
		addCommand(new ExecutableCommand(new CommandInfo("broadcast")
				.setUsage("[message]")
				.setDescription("{l:cmd.broadcast.desc}")
				.setCommandUsage(
						new CommandUsage().setMinimumArgs(0).setPermissions(
								"vc4.cmd.broadcast")), handle));
		addCommand(new ExecutableCommand(new CommandInfo("print")
				.addAlias("echo").setUsage("[message]")
				.setDescription("{l:cmd.print.desc}")
				.setCommandUsage(new CommandUsage().setMinimumArgs(0)), handle));
	}

	public static void addCommand(ExecutableCommand cmd) {
		engineCommands.put(cmd.getInfo().getName(), cmd);
		for (String s : cmd.getInfo().getAliases()) {
			engineCommands.put(s, cmd);
		}
	}

	public static void executeCommand(Command command) {
		try {
			ExecutableCommand e = engineCommands.get(command.getCommand());
			if (e == null) {
				PluginManager.handleCommand(command);
				return;
			}
			if (!e.getInfo().getCommandUsage().check(command))
				return;
			e.getHandler().handleCommand(command);
		} catch (Exception e) {
			command.getSender().message("{l:cmd.fail}");
			Logger.getLogger("VC4").warning(
					"Failed to execute command \"" + command.getCommand()
							+ "\"", e);
		}
	}

	@Override
	public void handleCommand(Command command) {
		switch (command.getCommand().toLowerCase()) {
		case "teleport":
		case "tp":
			handleCommand_tp(command);
			return;
		case "log":
			handleCommand_log(command);
			return;
		case "sound":
			handleCommand_sound(command);
			return;
		case "broadcast":
			handleCommand_broadcast(command);
			return;
		case "echo":
		case "print":
			handleCommand_print(command);
			return;
		}
	}

	private void handleCommand_tp(Command command) {
		double x = command.getArgAsDouble(0, 0);
		double y = command.getArgAsDouble(1, 0);
		double z = command.getArgAsDouble(2, 0);
		command.getSender().getPlayer().teleport(x, y, z);
		command.getSender().message(
				"{l:cmd.tp.done," + x + "," + y + "," + z + "}");
	}

	private void handleCommand_sound(Command command) {
		String name = command.getArg(0);
		double volume = command.getArgAsDouble(1, 1);
		double pitch = command.getArgAsDouble(2, 1);
		Audio.playSound(name, (float) volume, (float) pitch);
	}

	private void handleCommand_log(Command command) {
		String lvl = command.getArg(1);
		Level level;
		try {
			level = Level.valueOf(lvl.toUpperCase());
		} catch (Exception e) {
			command.getSender().message("{l:cmd.log.badlvl," + lvl + "}");
			return;
		}
		String name = command.getArg(0);
		if (name.equals("VC4")) {
			command.getSender().message("{l:cmd.log.vc4}");
			return;
		}
		String text = command.getArgsAsString(2);
		Logger.getLogger(name).log(level, text);
	}

	private void handleCommand_broadcast(Command command) {
		String msg = command.getArgsAsString(0);
		if (VoxelCaverns.isServer()) {
			VoxelCaverns.getServer().sendPacket(new Packet30MessageString(msg));
			ServerConsole.getConsole().writeLine(msg);
		} else {
			Client.getPlayer().message(msg);
		}
	}

	private void handleCommand_print(Command command) {
		String msg = command.getArgsAsString(0);
		command.getSender().message(msg);
	}
}
