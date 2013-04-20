/**
 * 
 */
package vc4.api.world;

import vc4.api.entity.Entity;

/**
 * @author paul
 *
 */
public interface Chunk {

	public short getBlockId(int x, int y, int z);
	public byte getBlockData(int x, int y, int z);
	public void setBlockId(int x, int y, int z, short id);
	public void setBlockData(int x, int y, int z, byte data);
	public void setBlockIdData(int x, int y, int z, short id, byte data);
	public ChunkPos getChunkPos();
	public World getWorld();
	public void addEntity(Entity entity);
}
