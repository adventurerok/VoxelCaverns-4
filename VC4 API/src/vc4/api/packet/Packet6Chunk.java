package vc4.api.packet;

import java.io.IOException;

import vc4.api.VoxelCaverns;
import vc4.api.io.SwitchInputStream;
import vc4.api.io.SwitchOutputStream;
import vc4.api.world.Chunk;

public class Packet6Chunk extends Packet {

	public static final int ID = 6;

	public long x, y, z;
	public Chunk chunk;

	public Packet6Chunk() {

	}

	@Override
	public void write(SwitchOutputStream out) throws IOException {
		out.writeLong(x);
		out.writeLong(y);
		out.writeLong(z);
		VoxelCaverns.getSaveFormat().writeNetworkBytes(chunk, out);
	}

	@Override
	public void read(SwitchInputStream in) throws IOException {
		x = in.readLong();
		y = in.readLong();
		z = in.readLong();
		chunk = VoxelCaverns.getSaveFormat().readNetworkBytes(VoxelCaverns.getCurrentWorld(), x, y, z, in);
	}

	@Override
	public int getId() {
		return 6;
	}

	public Packet6Chunk(Chunk chunk) {
		this.x = chunk.getChunkPos().x;
		this.y = chunk.getChunkPos().y;
		this.z = chunk.getChunkPos().z;
		this.chunk = chunk;
	}

	public Packet6Chunk(long x, long y, long z, Chunk chunk) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.chunk = chunk;
	}

}
