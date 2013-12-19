package vc4.server.packet;

import org.jnbt.CompoundTag;

import vc4.api.packet.*;
import vc4.server.user.ServerUser;

public class PacketHandler {

	public boolean handlePacket(ServerUser s, Packet p){
		switch(p.getId()){
			case 0: return handlePacket0(s, (Packet0Accept) p);
			case 1: return handlePacket1(s, (Packet1Disconnect) p);
			case 2: return handlePacket2(s, (Packet2Message) p);
			case 3: return handlePacket3(s, (Packet3NBT) p);
		}
		return false;
	}
	
	public boolean handlePacket0(ServerUser s, Packet0Accept p){
		s.setAccepted(true);
		return true;
	}
	
	public boolean handlePacket1(ServerUser s, Packet1Disconnect p){
		return true;
	}
	
	public boolean handlePacket2(ServerUser s, Packet2Message p){
		return true;
	}
	
	public boolean handlePacket3(ServerUser s, Packet3NBT p){
		switch(p.getType()){
			case 0:
				s.setInfoTag((CompoundTag) p.tag);
				break;
		}
		return true;
	}
}
