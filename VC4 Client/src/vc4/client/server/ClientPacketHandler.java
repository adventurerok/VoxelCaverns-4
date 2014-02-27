package vc4.client.server;

import java.io.IOException;

import vc4.api.VoxelCaverns;
import vc4.api.client.Client;
import vc4.api.logging.Logger;
import vc4.api.packet.*;
import vc4.api.world.ChunkPos;
import vc4.impl.world.ImplWorld;

public class ClientPacketHandler {

	static byte[] severUSID;

	public static boolean handlePacket(Server h, Packet p) throws IOException {
		switch (p.getId()) {
			case Packet0Accept.ID:
				return handlePacketAccept(h, (Packet0Accept) p);
			case Packet3SUID.ID:
				return handlePacketSUID(h, (Packet3SUID) p);
			case Packet5Chat.ID:
				return handlePacketChat(h, (Packet5Chat) p);
			case Packet6Chunk.ID:
				return handlePacketChunk(h, (Packet6Chunk) p);
			case Packet30MessageString.ID:
				return handlePacketMsgString(h, (Packet30MessageString) p);
		}
		return true;
	}

	private static boolean handlePacketChunk(Server h, Packet6Chunk p) {
		ChunkPos pos = p.chunk.getChunkPos();
		Logger.getLogger("VC4").info("Recieved chunk at: " + pos.x + ", " + pos.y + ", " + pos.z);
		((ImplWorld) VoxelCaverns.getCurrentWorld()).addChunk(p.chunk);
		return true;
	}

	public static boolean handlePacketChat(Server h, Packet5Chat p) {
		Client.getGame().printChatLine(p.prefix + p.name + ": " + p.msg);
		return true;
	}

	public static boolean handlePacketSUID(Server h, Packet3SUID p) throws IOException {
		if (p.message == 1) {
			severUSID = p.suid;
			if (ServerMap.hasServer(severUSID)) h.writePacket(new Packet4Login(ServerMap.getSuid(severUSID).getSuid()));
			else h.writePacket(new Packet31MessageInt(0));
		} else if (p.message == 0 && !ServerMap.hasServer(severUSID)) {
			ServerMap.addSuid(severUSID, p.suid);
			h.writePacket(new Packet4Login(ServerMap.getSuid(severUSID).getSuid()));
		}
		return true;
	}

	public static boolean handlePacketAccept(Server h, Packet0Accept p) throws IOException {
		h.writePacket(new Packet0Accept());
		h.writePacket(new Packet40NBT().setMessage(0, Client.getGame().getClientDetails()));
		return true;
	}

	public static boolean handlePacketMsgString(Server h, Packet30MessageString p) {
		Client.getGame().printChatLine(((Packet30MessageString) p).message);
		return true;
	}
}
