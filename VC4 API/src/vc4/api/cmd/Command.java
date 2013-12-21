package vc4.api.cmd;

import vc4.api.server.User;

public class Command {

	
	private String plugin;
	private String command;
	private String[] args;
	private User sender;
	
	
	
	/**
	 * Gets the user/console who sent the command
	 * 
	 * @return The user who sent the command
	 */
	public User getSender() {
		return sender;
	}
	
	/**
	 * Gets the arguments provided to the command
	 * 
	 * @return The arguments provided
	 */
	public String[] getArgs() {
		return args;
	}
	
	public String getCommand() {
		return command;
	}
	
	/**
	 * Gets the name of the plugin that is handling the command
	 * 
	 * @return The name of the plugin that is handling the command
	 */
	public String getPlugin() {
		return plugin;
	}
	
	/**
	 * Gets the argument at the given index in the command
	 * 
	 * @param index The index to get the argument
	 * @return The argument at the index
	 */
	public String getArg(int index){
		return args[index];
	}
	
	/**
	 * 
	 * @param start The first argument to be included in the string
	 * @return The constructed string
	 */
	public String getArgsAsString(int start){
		StringBuilder result = new StringBuilder();
		for(int d = start; d < args.length - 1; ++d){
			result.append(args[d]).append(' ');
		}
		if(start < args.length) result.append(args[args.length - 1]);
		return result.toString();
	}
	
	/**
	 * Gets the number of arguments given. Equivalent to GetArgs().length
	 * 
	 * @return The length of the argument array
	 */
	public int getArgsLength(){
		return args.length;
	}
	
	public int getArgAsInt(int index, int def){
		if(index >= args.length) return def;
		try{
			return Integer.parseInt(args[index]);
		} catch(Exception e){
			return def;
		}
	}
	
	public int getArgAsBool(int index){
		if(index >= args.length) return -1;
		try{
			return Boolean.parseBoolean(args[index]) ? 1 : 0;
		} catch(Exception e){
			return -1;
		}
	}
	
	public double getArgAsDouble(int index, double def){
		if(index >= args.length) return def;
		try{
			return Double.parseDouble(args[index]);
		} catch(Exception e){
			return def;
		}
	}

	public Command(String command, String[] args, User sender) {
		super();
		this.command = command.toLowerCase();
		this.args = args;
		this.sender = sender;
	}

	public Command(String plugin, String command, String[] args, User sender) {
		super();
		this.plugin = plugin.toLowerCase();
		this.command = command.toLowerCase();
		this.args = args;
		this.sender = sender;
	}
	
}
