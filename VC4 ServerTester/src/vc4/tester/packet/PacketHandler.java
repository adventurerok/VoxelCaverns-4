package vc4.tester.packet;

import java.io.IOException;

import vc4.api.packet.Packet;
import vc4.api.packet.Packet0Accept;
import vc4.api.packet.Packet30MessageString;
import vc4.api.packet.Packet3SUID;
import vc4.api.packet.Packet40NBT;
import vc4.api.packet.Packet43SettingString;
import vc4.api.packet.Packet4Login;
import vc4.api.packet.Packet5Chat;
import vc4.api.server.ServerConsole;
import vc4.tester.ClientHandler;
import vc4.tester.TesterConsole;

public class PacketHandler {
	
	byte[] severUSID;

	public static boolean handlePacket(ClientHandler h, Packet p) throws IOException{
		switch(p.getId()){
		case Packet0Accept.ID: return handlePacketAccept(h, (Packet0Accept) p);
		case Packet3SUID.ID: return handlePacketSUID(h, (Packet3SUID) p);
		case Packet5Chat.ID: return handlePacketChat(h, (Packet5Chat) p);
		case Packet30MessageString.ID: return handlePacketMsgString(h, (Packet30MessageString) p);
		}
		return true;
	}
	
	public static boolean handlePacketChat(ClientHandler h, Packet5Chat p){
		ServerConsole.getConsole().writeLine(p.prefix + p.name + ": " + p.msg);
		return true;
	}
	
	public static boolean handlePacketSUID(ClientHandler h, Packet3SUID p){
		return true;
	}
	
	public static boolean handlePacketAccept(ClientHandler h, Packet0Accept p) throws IOException {
		h.sendPacket(new Packet0Accept());
		h.sendPacket(new Packet40NBT().setMessage(0, ((TesterConsole)TesterConsole.getConsole()).getClientDetails()));
		h.sendPacket(new Packet4Login(new byte[]{0, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}));
		h.sendPacket(new Packet43SettingString(0, "adventurerok"));
		return true;
	}
	
	public static boolean handlePacketMsgString(ClientHandler h, Packet30MessageString p){
		ServerConsole.getConsole().writeLine(((Packet30MessageString)p).message);
		return true;
	}
}
