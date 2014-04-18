package vc4.server.packet;

import java.util.Arrays;

import vc4.api.VoxelCaverns;
import vc4.api.cmd.Command;
import vc4.api.logging.Logger;
import vc4.api.packet.*;
import vc4.api.server.SUID;
import vc4.api.server.ServerConsole;
import vc4.api.util.StringSplitter;
import vc4.api.util.SuidUtils;
import vc4.api.vbt.TagCompound;
import vc4.api.world.Chunk;
import vc4.impl.cmd.CommandExecutor;
import vc4.impl.world.ImplWorld;
import vc4.server.server.ServerSettings;
import vc4.server.task.TaskManager;
import vc4.server.user.*;

public class PacketHandler {

	public boolean handlePacket(ServerUser s, Packet p) {
		switch (p.getId()) {
			case Packet0Accept.ID:
				return handlePacketAccept(s, (Packet0Accept) p);
			case Packet1Disconnect.ID:
				return handlePacketDisconnect(s, (Packet1Disconnect) p);
			case Packet4Login.ID:
				return handlePacketLogin(s, (Packet4Login) p);
			case Packet30MessageString.ID:
				return handlePacketMsgString(s, (Packet30MessageString) p);
			case Packet31MessageInt.ID:
				return handlePacketMsgInt(s, (Packet31MessageInt) p);
			case Packet40NBT.ID:
				return handlePacketNBT(s, (Packet40NBT) p);
			case Packet43SettingString.ID:
				return handlePacketSettingString(s, (Packet43SettingString) p);
		}
		return false;
	}

	public boolean handlePacketLogin(ServerUser s, Packet4Login p) {
		SUID suid = new SUID(p.uid);
		UserInfo info = UserManager.getOrCreateUserInfo(suid);
		s.setInfo(info);
		Logger.getLogger("VC4").info(info.getChatName() + " connected to the server");

		for (Chunk c : ((ImplWorld) VoxelCaverns.getCurrentWorld()).chunks.values()) {
			if (c == null) continue;
			TaskManager.sendPackets(s, new Packet6Chunk(c));
		}
		return true;
	}

	public void sendDictionaries(ServerUser s){
		
	}
	
	public boolean handlePacketAccept(ServerUser s, Packet0Accept p) {
		s.setAccepted(true);
		TaskManager.sendPackets(s, new Packet3SUID(1, ServerSettings.usid));
		return true;
	}

	public boolean handlePacketDisconnect(ServerUser s, Packet1Disconnect p) {
		return true;
	}

	public boolean handlePacketMsgString(ServerUser s, Packet30MessageString p) {
		if (p.message.startsWith("/")) {
			String input = p.message.substring(1);
			if (input.isEmpty()) return true;
			String[] parts = StringSplitter.splitString(input, false);
			String cmd = parts[0];
			String[] args = Arrays.copyOfRange(parts, 1, parts.length);
			Command command = new Command(cmd, args, s);
			CommandExecutor.executeCommand(command);
			return true;
		}
		Packet5Chat chat = new Packet5Chat(s.getChatName(), s.getInfo().getGroup().getChatPrefix(), p.message);
		s.getServer().sendPacket(chat);
		ServerConsole.getConsole().writeLine(s.getInfo().getGroup().getChatPrefix() + s.getChatName() + ": " + p.message);
		return true;
	}

	public boolean handlePacketMsgInt(ServerUser s, Packet31MessageInt p) {
		switch (p.message) {
			case 0:
				TaskManager.sendPackets(s, new Packet3SUID(0, SuidUtils.generateRandomSuid()));
				break;
		}
		return true;
	}

	public boolean handlePacketSettingString(ServerUser s, Packet43SettingString p) {
		if (p.setting == 0) {
			String old = s.getInfo().getChatName();
			if (old.equals(p.change)) return true;
			s.getInfo().changeChatName(p.change, true);
			Logger.getLogger("VC4").info(old + " changed name to " + s.getInfo().getChatName());
		}
		return true;
	}

	public boolean handlePacketNBT(ServerUser s, Packet40NBT p) {
		switch (p.getType()) {
			case 0:
				s.setInfoTag((TagCompound) p.tag);
				break;
		}
		return true;
	}
}
