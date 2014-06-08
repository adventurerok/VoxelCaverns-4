/**
 * 
 */
package vc4.api.world;

import java.util.List;
import java.util.Random;

import vc4.api.biome.Biome;
import vc4.api.block.Block;
import vc4.api.entity.*;
import vc4.api.generator.WorldGenerator;
import vc4.api.io.Dictionary;
import vc4.api.sound.Music;
import vc4.api.tileentity.TileEntity;
import vc4.api.util.*;
import vc4.api.vbt.TagCompound;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;

/**
 * @author paul
 * 
 */
public interface World {

	public boolean chunkExists(ChunkPos pos);

	public boolean chunkExists(long x, long y, long z);

	public Random createRandom(long in1, long in2, long in3);

	public Random createRandom(long in1, long in2, long in3, long in4);

	public Chunk generateChunk(ChunkPos pos);

	public AABB[] getAABBsInBounds(Chunk chunk, AABB bounds, Entity exclude);

	public AABB[] getAABBsInBounds(AABB bounds, Entity exclude);

	public Biome getBiome(long x, long z);

	public byte getBlockData(long x, long y, long z);
	public byte getBlockData(Vector3l pos);

	public short getBlockId(long x, long y, long z);
	public short getBlockId(Vector3l pos);

	public Block getBlockType(long x, long y, long z);
	public Block getBlockType(Vector3l pos);

	public byte getBlockLight(long x, long y, long z);
	public byte getBlockLight(Vector3l pos);
	
	public byte[] getBlockExtended(long x, long y, long z);
	public byte[] getBlockExtended(Vector3l pos);

	public Chunk getChunk(ChunkPos pos);

	public Chunk getChunk(long x, long y, long z);

	public String[] getDebugInfo();

	public List<Entity> getEntitiesInBounds(Chunk chunk, AABB bounds);

	public List<Entity> getCollidableEntitiesInBounds(Chunk chunk, AABB bounds);

	public List<Entity> getEntitiesInBoundsExcluding(Chunk chunk, AABB bounds, Entity exclude);

	public List<Entity> getEntitiesInBounds(AABB bounds);

	public List<Entity> getCollidableEntitiesInBounds(AABB bounds);

	public List<Entity> getEntitiesInBoundsExcluding(AABB bounds, Entity exclude);

	public double getFallAcceleration();

	public double getFallMaxSpeed();

	public WorldGenerator getGenerator();

	public TagCompound getGeneratorTag();

	public MapData getMapData(long x, long z);

	public Music getMusic(EntityPlayer player);

	public String getName();

	public int getNearbyHeight(long x, long z, Direction dir);

	public int getNearbyHeight(long x, long z, int dir);

	public boolean hasNearbySkylight(long x, long y, long z, Direction dir);

	public boolean hasNearbySkylight(long x, long y, long z, int dir);

	public byte getNearbyBlockLight(long x, long y, long z, Direction dir);

	public byte getNearbyBlockData(long x, long y, long z, Direction dir);

	public byte getNearbyBlockData(long x, long y, long z, int d);

	public short getNearbyBlockId(long x, long y, long z, Direction dir);

	public short getNearbyBlockId(long x, long y, long z, int d);
	
	public byte[] getNearbyBlockExtended(long x, long y, long z, Direction dir);
	public byte[] getNearbyBlockExtended(Vector3l pos, Direction dir);

	public Block getNearbyBlockType(long x, long y, long z, Direction dir);

	public Block getNearbyBlockType(long x, long y, long z, int dir);

	public TileEntity getNearbyTileEntity(long x, long y, long z, Direction dir);

	public TileEntity getNearbyTileEntity(long x, long y, long z, int dir);

	public List<EntityPlayer> getPlayers();

	public byte getRegisteredBiome(String name);

	public short getRegisteredBlock(String name);

	public short getRegisteredCrafting(String name);

	public short getRegisteredEntity(String name);

	public int getRegisteredItem(String name);

	public String getSaveName();

	public long getSeed();

	public TileEntity getTileEntity(long x, long y, long z);

	public long getTime();

	public int getTimeOfDay();

	public byte getDayOfYear();

	public float getSeason();

	public RayTraceResult rayTraceBlocks(Vector3d start, Vector3d end, int amount);

	public void setBlockData(long x, long y, long z, int data);
	public void setBlockData(Vector3l pos, int data);

	public void setBlockDataNoNotify(long x, long y, long z, int data);

	public void setBlockId(long x, long y, long z, int id);
	public void setBlockId(Vector3l pos, int id);

	public void setBlockIdData(long x, long y, long z, int id, int data);
	public void setBlockIdData(Vector3l pos, int id, int data);
	
	public void setBlockExtended(long x, long y, long z, byte[] extended);
	public void setBlockExtended(Vector3l pos, byte[] extended);

	public void setBlockIdDataNoNotify(long x, long y, long z, int id, int data);

	public void setBlockIdNoNotify(long x, long y, long z, int id);

	public void setNearbyBlockData(long x, long y, long z, int data, Direction dir);

	public void setNearbyBlockDataNoNotify(long x, long y, long z, int data, Direction dir);
	
	public void setNearbyBlockExtended(long x, long y, long z, byte[] extended, Direction dir);
	public void setNearbyBlockExtended(Vector3l pos, byte[] extended, Direction dir);

	public void setNearbyBlockId(long x, long y, long z, int id, Direction dir);

	public void setNearbyBlockIdData(long x, long y, long z, int id, int data, Direction dir);

	public void setNearbyBlockIdDataNoNotify(long x, long y, long z, int id, int data, Direction dir);

	public void setNearbyBlockIdNoNotify(long x, long y, long z, int id, Direction dir);

	public void setNearbyTileEntity(long x, long y, long z, TileEntity t, Direction dir);

	public void setTileEntity(long x, long y, long z, TileEntity t);

	public Chunk loadChunk(ChunkPos pos);

	public String getEntityName(int id);

	public String getContainerName(int id);

	public String getItemEntityName(int id);

	public String getTileEntityName(int id);

	public short getRegisteredItemEntity(String name);

	public short getRegisteredTileEntity(String name);

	public short getRegisteredContainer(String name);

	public void setDirty(long x, long y, long z);

	public RayTraceResult rayTraceEntitys(EntityLiving entity, Vector3d end, double reach);

	public void broadcast(String message, Vector3d pos, double radius);

	public String getTraitName(int id);

	public short getRegisteredTrait(String name);

	public String getAreaName(int id);

	public short getRegisteredArea(String name);

	public List<Entity> getEntitiesInBounds(AABB bounds, Class<? extends Entity> type);

	public List<Entity> getEntitiesInBoundsExcluding(AABB bounds, Class<? extends Entity> type, Entity exclude);

	public String getTimeText();

	public int getDayOfMonth();

	public int getMonth();

	public void addTime(long add);

	public int getHeight(long x, long z);

	public boolean blockTransparencyChange(long x, long y, long z, boolean trans);

	public boolean hasSkyLight(long x, long y, long z);

	public boolean hasDayLight(long x, long y, long z);

	public boolean scheduleBlockUpdate(long x, long y, long z, int time, int buid);

	public float getNearbySkylight(long x, long y, long z, int d);

	public void notifyNear(long x, long y, long z);

	public Dictionary getDictionary(String name);

	public float getBlockDensity(Vector3d pos, AABB bounds);

	public float getSkyLight();
}
