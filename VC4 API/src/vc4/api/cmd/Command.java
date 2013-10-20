package vc4.api.cmd;

import vc4.api.server.User;

public class Command {

	
	private String plugin;
	private String command;
	private String[] args;
	private User sender;
	
	
	
	public User getSender() {
		return sender;
	}
	
	public String[] getArgs() {
		return args;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getPlugin() {
		return plugin;
	}
	
	public String getArg(int index){
		return args[index];
	}
	
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
