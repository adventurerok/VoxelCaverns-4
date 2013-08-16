/**
 * 
 */
package vc4.api.world;

import java.util.List;
import java.util.Random;

import org.jnbt.CompoundTag;

import vc4.api.biome.Biome;
import vc4.api.block.Block;
import vc4.api.entity.*;
import vc4.api.generator.WorldGenerator;
import vc4.api.sound.Music;
import vc4.api.tileentity.TileEntity;
import vc4.api.util.*;
import vc4.api.vector.Vector3d;

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
	public AABB[] getAABBsInBounds(AABB bounds, Entity exclude);
	public Biome getBiome(long x, long z);
	public byte getBlockData(long x, long y, long z);
	public short getBlockId(long x, long y, long z);
	public Block getBlockType(long x, long y, long z);
	public byte getBlockLight(long x, long y, long z);
	public Chunk getChunk(ChunkPos pos);
	public Chunk getChunk(long x, long y, long z);
	public abstract String[] getDebugInfo();
	public List<Entity> getEntitiesInBounds(AABB bounds);
	public List<Entity> getCollidableEntitiesInBounds(AABB bounds);
	public List<Entity> getEntitiesInBoundsExcluding(AABB bounds, Entity exclude);
	public double getFallAcceleration();
	public double getFallMaxSpeed();
	public WorldGenerator getGenerator();
	public CompoundTag getGeneratorTag();
	public MapData getMapData(long x, long z);
	public Music getMusic(EntityPlayer player);
	public abstract String getName();
	public int getNearbyHeight(long x, long z, Direction dir);
	public int getNearbyHeight(long x, long z, int dir);
	public boolean hasNearbySkylight(long x, long y, long z, Direction dir);
	public boolean hasNearbySkylight(long x, long y, long z, int dir);
	public byte getNearbyBlockLight(long x, long y, long z, Direction dir);
	public byte getNearbyBlockData(long x, long y, long z, Direction dir);
	public abstract byte getNearbyBlockData(long x, long y, long z, int d);
	public short getNearbyBlockId(long x, long y, long z, Direction dir);
	public abstract short getNearbyBlockId(long x, long y, long z, int d);
	public Block getNearbyBlockType(long x, long y, long z, Direction dir);
	public abstract Block getNearbyBlockType(long x, long y, long z, int dir);
	public abstract TileEntity getNearbyTileEntity(long x, long y, long z, Direction dir);
	public abstract TileEntity getNearbyTileEntity(long x, long y, long z, int dir);
	public List<EntityPlayer> getPlayers();
	public abstract byte getRegisteredBiome(String name);
	public short getRegisteredBlock(String name);
	public abstract short getRegisteredCrafting(String name);
	public abstract short getRegisteredEntity(String name);
	public int getRegisteredItem(String name);
	public abstract String getSaveName();
	public long getSeed();
	public abstract TileEntity getTileEntity(long x, long y, long z);
	public long getTime();
	public int getTimeOfDay();
	public byte getDayOfYear();
	public float getSeason();
	public RayTraceResult rayTraceBlocks(Vector3d start, Vector3d end, int amount);
	public void setBlockData(long x, long y, long z, int data);
	public void setBlockDataNoNotify(long x, long y, long z, int data);
	public void setBlockId(long x, long y, long z, int id);
	public void setBlockIdData(long x, long y, long z, int id, int data);
	public void setBlockIdDataNoNotify(long x, long y, long z, int id, int data);
	public void setBlockIdNoNotify(long x, long y, long z, int id);
	public void setNearbyBlockData(long x, long y, long z, int data, Direction dir);
	public void setNearbyBlockDataNoNotify(long x, long y, long z, int data, Direction dir);
	public void setNearbyBlockId(long x, long y, long z, int id, Direction dir);
	public void setNearbyBlockIdData(long x, long y, long z, int id, int data, Direction dir);
	public void setNearbyBlockIdDataNoNotify(long x, long y, long z, int id, int data, Direction dir);
	public void setNearbyBlockIdNoNotify(long x, long y, long z, int id, Direction dir);
	public abstract void setNearbyTileEntity(long x, long y, long z, TileEntity t, Direction dir);
	public abstract void setTileEntity(long x, long y, long z, TileEntity t);
	public Chunk loadChunk(ChunkPos pos);
	public String getEntityName(int id);
	public String  getContainerName(int id);
	public abstract String getItemEntityName(int id);
	public abstract String getTileEntityName(int id);
	public abstract short getRegisteredItemEntity(String name);
	public abstract short getRegisteredTileEntity(String name);
	public abstract short getRegisteredContainer(String name);
	public abstract void setDirty(long x, long y, long z);
	public abstract RayTraceResult rayTraceEntitys(EntityLiving entity, Vector3d end, double reach);
	public void broadcast(String message, Vector3d pos, double radius);
	public abstract String getTraitName(int id);
	public abstract short getRegisteredTrait(String name);
	public abstract String getAreaName(int id);
	public abstract short getRegisteredArea(String name);
	public abstract List<Entity> getEntitiesInBounds(AABB bounds, Class<? extends Entity> type);
	public abstract List<Entity> getEntitiesInBoundsExcluding(AABB bounds, Class<? extends Entity> type, Entity exclude);
	public abstract String getTimeText();
	public abstract int getDayOfMonth();
	public abstract int getMonth();
	public abstract void addTime(long add);
	public abstract int getHeight(long x, long z);
	public abstract boolean blockTransparencyChange(long x, long y, long z, boolean trans);
}
