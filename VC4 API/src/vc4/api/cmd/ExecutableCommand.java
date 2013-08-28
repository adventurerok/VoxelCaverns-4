package vc4.api.cmd;

public class ExecutableCommand implements Comparable<ExecutableCommand>{

	CommandInfo info;
	CommandHandler handler;
	
	public ExecutableCommand(CommandInfo info, CommandHandler handler) {
		super();
		this.info = info;
		this.handler = handler;
	}
	
	public CommandInfo getInfo() {
		return info;
	}
	
	public CommandHandler getHandler() {
		return handler;
	}

	@Override
	public int compareTo(ExecutableCommand o) {
		return info.compareTo(o.info);
	}
}
