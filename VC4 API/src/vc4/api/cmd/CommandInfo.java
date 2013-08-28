package vc4.api.cmd;

import java.util.ArrayList;

public class CommandInfo implements Comparable<CommandInfo>{

	CommandUsage commandUsage = new CommandUsage();
	String desc;
	String usage;
	String name;
	ArrayList<String> aliases = new ArrayList<>();
	
	
	
	public CommandInfo setCommandUsage(CommandUsage commandUsage) {
		this.commandUsage = commandUsage;
		return this;
	}
	
	public CommandUsage getCommandUsage() {
		return commandUsage;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public ArrayList<String> getAliases() {
		return aliases;
	}
	
	
	public CommandInfo setDescription(String desc) {
		this.desc = desc;
		return this;
	}
	
	public CommandInfo setUsage(String usage) {
		this.usage = usage;
		return this;
	}
	
	public CommandInfo addAlias(String alias){
		aliases.add(alias);
		return this;
	}

	@Override
	public int compareTo(CommandInfo o) {
		return name.compareTo(o.name);
	}

	public CommandInfo(String name) {
		super();
		this.name = name;
	}
	
}
