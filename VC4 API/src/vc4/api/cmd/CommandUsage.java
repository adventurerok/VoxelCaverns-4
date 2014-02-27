package vc4.api.cmd;

public class CommandUsage {

	public int minimumArgs = 0;
	public int maximumArgs = -1;
	public int capabilities = 0;
	public CommandArgument[] argumentChecks = new CommandArgument[0];
	public String[] permissions = new String[0];

	public CommandUsage setMaximumArgs(int maximumArgs) {
		this.maximumArgs = maximumArgs;
		return this;
	}

	public CommandUsage setArgumentChecks(CommandArgument... argumentChecks) {
		this.argumentChecks = argumentChecks;
		return this;
	}

	public CommandUsage setRequiredCapabilities(int capabilities) {
		this.capabilities = capabilities;
		return this;
	}

	public CommandUsage setMinimumArgs(int minimumArgs) {
		this.minimumArgs = minimumArgs;
		return this;
	}

	public CommandUsage setPermissions(String... permissions) {
		this.permissions = permissions;
		return this;
	}

	public boolean check(Command command) {
		if ((command.getSender().getUserLevel() & capabilities) != capabilities) {
			if ((capabilities & 0b1) == 0b1 && (command.getSender().getUserLevel() & 0b1) == 0) {
				command.getSender().message("{l:cmd.nouser}");
			}
			if ((capabilities & 0b10) == 0b10 && (command.getSender().getUserLevel() & 0b10) == 0) {
				command.getSender().message("{l:cmd.noplayer}");
			}
			if ((capabilities & 0b100) == 0b100 && (command.getSender().getUserLevel() & 0b100) == 0) {
				command.getSender().message("{l:cmd.consoleonly}");
			}
			return false;
		}
		for (String s : permissions) {
			boolean b = true;
			if (!command.getSender().hasPermission(s)) {
				command.getSender().message("{l:cmd.nopermission," + s + "}");
				b = false;
			}
			if (!b) return false;
		}
		if (command.getArgsLength() < minimumArgs) {
			if (maximumArgs == -1) command.getSender().message("{l:cmd.minargs," + minimumArgs + "}");
			else command.getSender().message("{l:cmd.numargs," + minimumArgs + "," + maximumArgs + "}");
			return false;
		}
		if (maximumArgs != -1 && command.getArgsLength() > maximumArgs) {
			if (minimumArgs == 0) if (maximumArgs == -1) command.getSender().message("{l:cmd.maxargs," + maximumArgs + "}");
			else command.getSender().message("{l:cmd.numargs," + minimumArgs + "," + maximumArgs + "}");
			return false;
		}
		for (int d = 0; d < command.getArgsLength() && d < argumentChecks.length; ++d) {
			switch (argumentChecks[d]) {
				case STRING:
					continue;
				case DOUBLE:
					if (Double.isNaN(command.getArgAsDouble(d, Double.NaN))) {
						command.getSender().message("{l:cmd.doubleneeded," + d + "}");
						return false;
					}
					continue;
				case INTEGER:
					if (command.getArgAsInt(d, Integer.MIN_VALUE) == Integer.MIN_VALUE) {
						command.getSender().message("{l:cmd.integerneeded," + d + "}");
						return false;
					}
					continue;
				case BOOLEAN:
					if (command.getArgAsBool(d) == -1) {
						command.getSender().message("{l:cmd.booleanneeded," + d + "}");
						return false;
					}
					continue;
			}
		}
		return true;
	}

}
