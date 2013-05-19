/**
 * 
 */
package vc4.api.world;

import java.util.List;
import java.util.Random;

import org.jnbt.CompoundTag;

import vc4.api.biome.Biome;
import vc4.api.block.Block;
import vc4.api.entity.Entity;
import vc4.api.entity.EntityPlayer;
import vc4.api.generator.WorldGenerator;
import vc4.api.sound.Music;
import vc4.api.util.*;
import vc4.api.vector.Vector3d;

/**
 * @author paul
 *
 */
public interface World {

	public Block getBlockType(long x, long y, long z);
	public short getBlockId(long x, long y, long z);
	public byte getBlockData(long x, long y, long z);
	public void setBlockId(long x, long y, long z, int id);
	public void setBlockData(long x, long y, long z, int data);
	public void setBlockIdData(long x, long y, long z, int id, int data);
	public void setBlockIdNoNotify(long x, long y, long z, int id);
	public void setBlockDataNoNotify(long x, long y, long z, int data);
	public void setBlockIdDataNoNotify(long x, long y, long z, int id, int data);
	public Block getNearbyBlockType(long x, long y, long z, Direction dir);
	public short getNearbyBlockId(long x, long y, long z, Direction dir);
	public byte getNearbyBlockData(long x, long y, long z, Direction dir);
	public void setNearbyBlockId(long x, long y, long z, int id, Direction dir);
	public void setNearbyBlockData(long x, long y, long z, int data, Direction dir);
	public void setNearbyBlockIdData(long x, long y, long z, int id, int data, Direction dir);
	public void setNearbyBlockIdNoNotify(long x, long y, long z, int id, Direction dir);
	public void setNearbyBlockDataNoNotify(long x, long y, long z, int data, Direction dir);
	public void setNearbyBlockIdDataNoNotify(long x, long y, long z, int id, int data, Direction dir);
	public Chunk getChunk(long x, long y, long z);
	public Chunk getChunk(ChunkPos pos);
	public long getSeed();
	public RayTraceResult rayTraceBlocks(Vector3d start, Vector3d end, int amount);
	public Random createRandom(long in1, long in2, long in3);
	public Random createRandom(long in1, long in2, long in3, long in4);
	public AABB[] getAABBsInBounds(AABB bounds, Entity exclude);
	public long getTime();
	public short getRegisteredBlock(String name);
	public int getRegisteredItem(String name);
	public CompoundTag getGeneratorTag();
	public List<EntityPlayer> getPlayers();
	public EntityList getEntitiesInBounds(AABB bounds);
	public EntityList getEntitiesInBoundsExcluding(AABB bounds, Entity exclude);
	public double getFallAcceleration();
	public double getFallMaxSpeed();
	public boolean chunkExists(long x, long y, long z);
	public boolean chunkExists(ChunkPos pos);
	public Chunk generateChunk(ChunkPos pos);
	public WorldGenerator getGenerator();
	public Music getMusic(EntityPlayer player);
	public MapData getMapData(long x, long z);
	public Biome getBiome(long x, long z);
	public abstract String getSaveName();
	public abstract String getName();
	public abstract String[] getDebugInfo();
}
