package vc4.server.packet;

import org.jnbt.CompoundTag;

import vc4.api.packet.*;
import vc4.server.user.ServerUser;

public class PacketHandler {

	public boolean handlePacket(ServerUser s, Packet p){
		switch(p.getId()){
			case Packet0Accept.ID: return handlePacketAccept(s, (Packet0Accept) p);
			case Packet1Disconnect.ID: return handlePacketDisconnect(s, (Packet1Disconnect) p);
			case Packet4Login.ID: return handlePacketLogin(s, (Packet4Login) p);
			case Packet30MessageString.ID: return handlePacketMsgString(s, (Packet30MessageString) p);
			case Packet31MessageInt.ID: return handlePacketMsgInt(s, (Packet31MessageInt) p);
			case Packet40NBT.ID: return handlePacketNBT(s, (Packet40NBT) p);
		}
		return false;
	}
	
	public boolean handlePacketLogin(ServerUser s, Packet4Login p) {
		return true;
	}

	public boolean handlePacketAccept(ServerUser s, Packet0Accept p){
		s.setAccepted(true);
		return true;
	}
	
	public boolean handlePacketDisconnect(ServerUser s, Packet1Disconnect p){
		return true;
	}
	
	public boolean handlePacketMsgString(ServerUser s, Packet30MessageString p){
		return true;
	}
	
	public boolean handlePacketMsgInt(ServerUser s, Packet31MessageInt p){
		return true;
	}
	
	public boolean handlePacketNBT(ServerUser s, Packet40NBT p){
		switch(p.getType()){
			case 0:
				s.setInfoTag((CompoundTag) p.tag);
				break;
		}
		return true;
	}
}
