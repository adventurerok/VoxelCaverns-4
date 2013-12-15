package vc4.api.cmd;

/**
 * Represents a command that can be executed
 * 
 * @author paul
 *
 */
public class ExecutableCommand implements Comparable<ExecutableCommand>{

	CommandInfo info;
	CommandHandler handler;
	
	public ExecutableCommand(CommandInfo info, CommandHandler handler) {
		super();
		this.info = info;
		this.handler = handler;
	}
	
	/**
	 * Gets the commandinfo object relating to this command
	 * 
	 * @return The CommandInfo
	 */
	public CommandInfo getInfo() {
		return info;
	}
	
	/**
	 * Gets the commandhandler for this command
	 * 
	 * @return The CommandHandler
	 */
	public CommandHandler getHandler() {
		return handler;
	}

	@Override
	public int compareTo(ExecutableCommand o) {
		return info.compareTo(o.info);
	}
}
