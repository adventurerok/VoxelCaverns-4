package vc4.tester.cmd;

import java.io.IOException;

import vc4.api.cmd.Command;
import vc4.api.cmd.CommandHandler;
import vc4.api.logging.Logger;
import vc4.api.packet.Packet30MessageString;
import vc4.tester.TesterConsole;

public class CommandListener implements CommandHandler {

	@Override
	public void handleCommand(Command command) {
		if(command.getCommand().equals("msg")){
			try {
				((TesterConsole)TesterConsole.getConsole()).getClient().sendPacket(new Packet30MessageString(command.getArgsAsString(0)));
			} catch (IOException e) {
				Logger.getLogger(CommandListener.class).warning("Exception occured", e);
			}
		}
	}

}
