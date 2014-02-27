package vc4.api.io;

import java.io.IOException;

import vc4.api.world.*;

public interface SaveFormat {

	public Chunk readChunk(World world, long x, long y, long z) throws IOException;

	public void writeChunk(Chunk chunk) throws IOException;

	public MapData readMap(World world, long x, long z) throws IOException;

	public void writeMap(World world, MapData map) throws IOException;

	public void writeChunk(Chunk chunk, BitOutputStream out);

	public Chunk readChunk(World world, BitInputStream in);

	public void writeNetworkBytes(Chunk chunk, SwitchOutputStream out) throws IOException;

	public Chunk readNetworkBytes(World world, long x, long y, long z, SwitchInputStream in) throws IOException;
}
