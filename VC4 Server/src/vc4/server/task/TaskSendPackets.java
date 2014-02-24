package vc4.server.task;

import vc4.api.packet.Packet;
import vc4.server.user.ServerUser;

public class TaskSendPackets implements Task {
	
	ServerUser user;
	Packet[] packets;
	
	

	@Override
	public void run() {
		for(Packet p : packets){
			user.sendPacket(p);
		}
	}



	public TaskSendPackets(ServerUser user, Packet...packets) {
		super();
		this.user = user;
		this.packets = packets;
	}

}
