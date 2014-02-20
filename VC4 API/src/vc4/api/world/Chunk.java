/**
 * 
 */
package vc4.api.world;

import java.util.ArrayList;

import vc4.api.area.Area;
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
	public byte getBlockLight(int x, int y, int z);
	public ChunkPos getChunkPos();
	public World getWorld();
	public void addEntity(Entity entity);
	public ArrayList<Entity> getEntityList();
	public abstract boolean isModified();
	public abstract void setModified(boolean modified);
	public abstract void setDirty(int x, int y, int z);
	public abstract void addArea(Area ar);
	public abstract ArrayList<Area> getAreas();
	public abstract byte getBlockLightWithBBCheck(int x, int y, int z);
	public abstract boolean isLit();
	public abstract void initialLight();
	public void scheduleBlockUpdate(int x, int y, int z, int time, int buid);
	
}
