package vc4.editor.util;

import java.util.List;
import java.util.Random;

import org.jnbt.CompoundTag;

import vc4.api.biome.Biome;
import vc4.api.block.Block;
import vc4.api.entity.Entity;
import vc4.api.entity.EntityPlayer;
import vc4.api.generator.WorldGenerator;
import vc4.api.sound.Music;
import vc4.api.tileentity.TileEntity;
import vc4.api.util.*;
import vc4.api.vector.Vector3d;
import vc4.api.world.*;

public class TestWorld implements World {

	long seed = new Random().nextLong();
	
	public TestWorld() {
		// TASK Auto-generated constructor stub
	}
	
	public TestWorld(long seed) {
		super();
		this.seed = seed;
	}

	@Override
	public Block getBlockType(long x, long y, long z) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public short getBlockId(long x, long y, long z) {
		// TASK Auto-generated method stub
		return 0;
	}

	@Override
	public byte getBlockData(long x, long y, long z) {
		// TASK Auto-generated method stub
		return 0;
	}

	@Override
	public void setBlockId(long x, long y, long z, int id) {
		// TASK Auto-generated method stub

	}

	@Override
	public void setBlockData(long x, long y, long z, int data) {
		// TASK Auto-generated method stub

	}

	@Override
	public void setBlockIdData(long x, long y, long z, int id, int data) {
		// TASK Auto-generated method stub

	}

	@Override
	public void setBlockIdNoNotify(long x, long y, long z, int id) {
		// TASK Auto-generated method stub

	}

	@Override
	public void setBlockDataNoNotify(long x, long y, long z, int data) {
		// TASK Auto-generated method stub

	}

	@Override
	public void setBlockIdDataNoNotify(long x, long y, long z, int id, int data) {
		// TASK Auto-generated method stub

	}

	@Override
	public Block getNearbyBlockType(long x, long y, long z, Direction dir) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public short getNearbyBlockId(long x, long y, long z, Direction dir) {
		// TASK Auto-generated method stub
		return 0;
	}

	@Override
	public byte getNearbyBlockData(long x, long y, long z, Direction dir) {
		// TASK Auto-generated method stub
		return 0;
	}

	@Override
	public void setNearbyBlockId(long x, long y, long z, int id, Direction dir) {
		// TASK Auto-generated method stub

	}

	@Override
	public void setNearbyBlockData(long x, long y, long z, int data, Direction dir) {
		// TASK Auto-generated method stub

	}

	@Override
	public void setNearbyBlockIdData(long x, long y, long z, int id, int data, Direction dir) {
		// TASK Auto-generated method stub

	}

	@Override
	public void setNearbyBlockIdNoNotify(long x, long y, long z, int id, Direction dir) {
		// TASK Auto-generated method stub

	}

	@Override
	public void setNearbyBlockDataNoNotify(long x, long y, long z, int data, Direction dir) {
		// TASK Auto-generated method stub

	}

	@Override
	public void setNearbyBlockIdDataNoNotify(long x, long y, long z, int id, int data, Direction dir) {
		// TASK Auto-generated method stub

	}

	@Override
	public Chunk getChunk(long x, long y, long z) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public Chunk getChunk(ChunkPos pos) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public long getSeed() {
		return seed;
	}

	@Override
	public RayTraceResult rayTraceBlocks(Vector3d start, Vector3d end, int amount) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public Random createRandom(long in1, long in2, long in3) {
		long seed = in1 * 341873128712L + in2 * 132897987541L + getSeed() + in3;
		return new Random(seed);
	}

	@Override
	public Random createRandom(long in1, long in2, long in3, long in4) {
		long seed = in1 * 725659903004L + in2 * 341873128712L + in3 * 132897987541L + getSeed() + in4;
		return new Random(seed);
	}

	@Override
	public AABB[] getAABBsInBounds(AABB bounds, Entity exclude) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public long getTime() {
		// TASK Auto-generated method stub
		return 0;
	}

	@Override
	public short getRegisteredBlock(String name) {
		// TASK Auto-generated method stub
		return 0;
	}

	@Override
	public int getRegisteredItem(String name) {
		// TASK Auto-generated method stub
		return 0;
	}

	@Override
	public CompoundTag getGeneratorTag() {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public List<EntityPlayer> getPlayers() {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public EntityList getEntitiesInBounds(AABB bounds) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public EntityList getEntitiesInBoundsExcluding(AABB bounds, Entity exclude) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public double getFallAcceleration() {
		// TASK Auto-generated method stub
		return 0;
	}

	@Override
	public double getFallMaxSpeed() {
		// TASK Auto-generated method stub
		return 0;
	}

	@Override
	public boolean chunkExists(long x, long y, long z) {
		// TASK Auto-generated method stub
		return false;
	}

	@Override
	public boolean chunkExists(ChunkPos pos) {
		// TASK Auto-generated method stub
		return false;
	}

	@Override
	public Chunk generateChunk(ChunkPos pos) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public WorldGenerator getGenerator() {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public Music getMusic(EntityPlayer player) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public MapData getMapData(long x, long z) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public Biome getBiome(long x, long z) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public String getSaveName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String[] getDebugInfo() {
		return null;
	}


	@Override
	public short getRegisteredCrafting(String name) {
		return 0;
	}

	@Override
	public void setNearbyTileEntity(long x, long y, long z, TileEntity t, Direction dir) {
	}

	@Override
	public TileEntity getTileEntity(long x, long y, long z) {
		return null;
	}

	@Override
	public void setTileEntity(long x, long y, long z, TileEntity t) {
	}

	@Override
	public Block getNearbyBlockType(long x, long y, long z, int dir) {
		return null;
	}

	@Override
	public TileEntity getNearbyTileEntity(long x, long y, long z, int dir) {
		return null;
	}

	@Override
	public TileEntity getNearbyTileEntity(long x, long y, long z, Direction dir) {
		return null;
	}

	@Override
	public short getNearbyBlockId(long x, long y, long z, int d) {
		return 0;
	}

	@Override
	public byte getNearbyBlockData(long x, long y, long z, int d) {
		return 0;
	}

	@Override
	public byte getRegisteredBiome(String name) {
		// TASK Auto-generated method stub
		return 0;
	}

	@Override
	public short getRegisteredEntity(String name) {
		// TASK Auto-generated method stub
		return 0;
	}

	@Override
	public Chunk loadChunk(ChunkPos pos) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public String getEntityName(int id) {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public String getItemEntityName(int id) {
		return null;
	}

	@Override
	public String getTileEntityName(int id) {
		return null;
	}

	@Override
	public short getRegisteredItemEntity(String name) {
		return 0;
	}

	@Override
	public short getRegisteredTileEntity(String name) {
		return 0;
	}

	@Override
	public String getContainerName(int id) {
		// TASK Auto-generated method stub
		return null;
	}

	public short getRegisteredContainer(String name) {
		return 0;
	}

	public void setDirty(long x, long y, long z) {
	}

}
