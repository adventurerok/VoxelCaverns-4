/**
 * 
 */
package vc4.impl.world;

import java.util.*;
import java.util.Map.Entry;

import vc4.api.area.Area;
import vc4.api.block.Block;
import vc4.api.entity.Entity;
import vc4.api.generator.GeneratorOutput;
import vc4.api.math.MathUtils;
import vc4.api.tileentity.TileEntity;
import vc4.api.util.Direction;
import vc4.api.vector.*;
import vc4.api.world.*;

/**
 * @author paul
 * 
 */
public class ImplChunk implements Chunk {

	protected static final byte n_0 = 0;
	protected static final byte n_15 = 15;
	protected static final byte n_16 = 16;

	protected static final short s_0 = 0;
	
	private static LinkedList<Vector3i> stat_ilightPositions;
	private static ChunkPos stat_ilightingChunk;
	
	BlockStore[] stores = new BlockStore[8];
	ChunkPos pos;
	private ImplWorld world;
	private boolean populated;
	public ArrayList<Entity> entitys = new ArrayList<>();
	public ArrayList<Area> areas = new ArrayList<>();
	private boolean isModified = false;
	private boolean unloading = false;
	private boolean lit = false;
	private boolean skyChecked = false;
	private int skyCheckZ = 0;
	
	private LinkedList<Vector3i> ilightPositions;
	
	
	public volatile HashMap<Vector3i, TileEntity> tileEntitys = new HashMap<Vector3i, TileEntity>();

	/**
	 * 
	 */
	public ImplChunk(ImplWorld world, ChunkPos pos) {
		this.world = world;
		this.pos = pos;
		for (int d = 0; d < 8; ++d) {
			stores[d] = new BlockStore((d >> 2) << 4, ((d >> 1) & 1) << 4, (d & 1) << 4);
		}
	}

	public TileEntity getTileEntity(int x, int y, int z) {
		return tileEntitys.get(new Vector3i(x, y, z));
	}
	
	public HashMap<Vector3i, TileEntity> getTileEntitys() {
		return tileEntitys;
	}
	
	public void setTileEntity(int x, int y, int z, TileEntity t) {
		isModified = true;
		if (t != null) tileEntitys.put(new Vector3i(x, y, z), t);
		else tileEntitys.remove(new Vector3i(x, y, z));
		
		stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].clearRenderers();
		notifyNear(x, y, z);
	}
	
	@Override
	public ArrayList<Area> getAreas() {
		return areas;
	}
	
	@Override
	public void addArea(Area ar){
		areas.add(ar);
	}
	
	@Override
	public void setModified(boolean modified) {
		this.isModified = modified;
	}
	
	@Override
	public boolean isModified() {
		return isModified;
	}
	
	@Override
	public boolean isLit() {
		return lit && skyChecked;
	}
	
	public void setData(GeneratorOutput data) {
		for (int d = 0; d < 8; ++d) {
			stores[d].blocks = new short[4096];
			stores[d].data = new byte[4096];
		}
		int y, z;
		for (int x = 0; x < 32; ++x) {
			for (y = 0; y < 32; ++y) {
				for (z = 0; z < 32; ++z) {
					if (data.blocks[GeneratorOutput.arrayCalc(x, y, z)] == 0) continue;
					stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].blocks[BlockStore.arrayCalc(x & 15, y & 15, z & 15)] = data.blocks[GeneratorOutput.arrayCalc(x, y, z)];
					if (data.data[GeneratorOutput.arrayCalc(x, y, z)] == 0) continue;
					stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].data[BlockStore.arrayCalc(x & 15, y & 15, z & 15)] = data.data[GeneratorOutput.arrayCalc(x, y, z)];
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.Chunk#getBlockId(int, int, int)
	 */
	@Override
	public short getBlockId(int x, int y, int z) {
		return stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].getBlockId(x & 0xF, y & 0xF, z & 0xF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.Chunk#getBlockData(int, int, int)
	 */
	@Override
	public byte getBlockData(int x, int y, int z) {
		return stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].getBlockData(x & 0xF, y & 0xF, z & 0xF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.Chunk#setBlockId(int, int, int, short)
	 */
	@Override
	public void setBlockId(int x, int y, int z, short id) {
		short old = getBlockId(x, y, z);
		if(stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].setBlockId(x & 0xF, y & 0xF, z & 0xF, id)){
			isModified = true;
			checkLight(x, y, z, old);
			int n = getBlockId(x, y, z);
			if(Block.blockOpacity[old] >= 6 && Block.blockOpacity[n] < 6) getWorld().blockTransparencyChange(pos.worldX(x), pos.worldY(y), pos.worldZ(z), true);
			else if(Block.blockOpacity[n] >= 6 && Block.blockOpacity[old] < 6) getWorld().blockTransparencyChange(pos.worldX(x), pos.worldY(y), pos.worldZ(z), false);
			notifyNear(x, y, z);
		}
	}
	
	public void setBlockLight(int x, int y, int z, byte light) {
		if(stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].setBlockLight(x & 0xF, y & 0xF, z & 0xF, light)) isModified = true;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.Chunk#setBlockData(int, int, int, byte)
	 */
	@Override
	public void setBlockData(int x, int y, int z, byte data) {
		if(stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].setBlockData(x & 0xF, y & 0xF, z & 0xF, data)) isModified = true;
		notifyNear(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.Chunk#setBlockIdData(int, int, int, short, byte)
	 */
	@Override
	public void setBlockIdData(int x, int y, int z, short id, byte data) {
		short old = getBlockId(x, y, z);
		if(stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].setBlockIdData(x & 0xF, y & 0xF, z & 0xF, id, data)){
			isModified = true;
			checkLight(x, y, z, old);
			int n = getBlockId(x, y, z);
			if(Block.blockOpacity[old] >= 6 && Block.blockOpacity[n] < 6) getWorld().blockTransparencyChange(pos.worldX(x), pos.worldY(y), pos.worldZ(z), true);
			else if(Block.blockOpacity[n] >= 6 && Block.blockOpacity[old] < 6) getWorld().blockTransparencyChange(pos.worldX(x), pos.worldY(y), pos.worldZ(z), false);
			notifyNear(x, y, z);
		}
	}

	public void notifyNear(int x, int y, int z) {
		if (x % 16 == 0) world.getBlockStoreFromWorld(pos.worldX(x - 1), pos.worldY(y), pos.worldZ(z)).clearRenderers();
		else if (x % 16 == 15) world.getBlockStoreFromWorld(pos.worldX(x + 1), pos.worldY(y), pos.worldZ(z)).clearRenderers();
		if (y % 16 == 0) world.getBlockStoreFromWorld(pos.worldX(x), pos.worldY(y - 1), pos.worldZ(z)).clearRenderers();
		else if (y % 16 == 15) world.getBlockStoreFromWorld(pos.worldX(x), pos.worldY(y + 1), pos.worldZ(z)).clearRenderers();
		if (z % 16 == 0) world.getBlockStoreFromWorld(pos.worldX(x), pos.worldY(y), pos.worldZ(z - 1)).clearRenderers();
		else if (z % 16 == 15) world.getBlockStoreFromWorld(pos.worldX(x), pos.worldY(y), pos.worldZ(z + 1)).clearRenderers();
	}
	
	@Override
	public void setDirty(int x, int y, int z){
		stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].clearRenderers();
		notifyNear(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.Chunk#getChunkPos()
	 */
	@Override
	public ChunkPos getChunkPos() {
		return pos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.Chunk#getWorld()
	 */
	@Override
	public World getWorld() {
		return world;
	}

	/**
	 * @param i
	 */
	public BlockStore getBlockStore(int num) {
		return stores[num];
	}

	public double distanceSquared(Vector3d vec) {
		Vector3d me = new Vector3d(pos.worldX(16), pos.worldY(16), pos.worldZ(16));
		return me.distanceSquared(vec);
	}

	/**
	 * 
	 */
	public void empty() {
		for (BlockStore s : stores) {
			s.empty();
		}
		stores = null;
		world = null;
	}

	/**
	 * @return the populated
	 */
	public boolean isPopulated() {
		return populated;
	}

	/**
	 * @param populated
	 *            the populated to set
	 */
	public void setPopulated(boolean populated) {
		this.populated = populated;
	}

	public boolean isSurrounded() {
		int y, z;
		for (int x = -1; x < 2; ++x) {
			for (y = -1; y < 2; ++y) {
				for (z = -1; z < 2; ++z) {
					if (!world.chunkExists(x + pos.x, y + pos.y, z + pos.z)) return false;
				}
			}
		}
		return true;
	}

	public void update(Random rand) {
		for(int d = areas.size() - 1; d > -1; --d){
			areas.get(d).updateTick();
		}
		ArrayList<Entity> alive = new ArrayList<>();
		Entity e;
		long qx, qy, qz;
		for (int dofor = entitys.size() - 1; dofor > -1; --dofor) {
			e = entitys.get(dofor);
			if (e == null) continue;
			if (!e.hadUpdate) e.update();
			if (!e.needsRemoving()) {
				qx = MathUtils.floor(e.position.x) >> 5;
				qy = MathUtils.floor(e.position.y) >> 5;
				qz = MathUtils.floor(e.position.z) >> 5;
				if (ChunkPos.create(qx, qy, qz).equals(pos) || !world.chunkExists(qx, qy, qz)) {
					e.hadUpdate = false;
					alive.add(e);
				} else {
					e.hadUpdate = true;
					world.chunks.get(ChunkPos.create(qx, qy, qz)).entitys.add(e);
				}

			}
		}
		entitys = alive;
		int x, y, z, time;
		for(int d = 0; d < 10; ++d){
			x = rand.nextInt(32);
			y = rand.nextInt(32);
			z = rand.nextInt(32);
			time = Block.byId(getBlockId(x, y, z)).blockUpdate(world, rand, pos.worldX(x), pos.worldY(y), pos.worldZ(z), getBlockData(x, y, z));
			if(time > 0){
				world.scheduleBlockUpdate(pos.worldX(x), pos.worldY(y), pos.worldZ(z), time);
			}
		}
		List<TileEntity> tiles = new ArrayList<TileEntity>(tileEntitys.values());
		for (int dofor = tiles.size() - 1; dofor > -1; --dofor) {
			tiles.get(dofor).updateTick();
			if (tiles.get(dofor).remove) {
				Vector3i pos = tiles.get(dofor).getPositionInChunk();
//				if (unloading) return;
				tileEntitys.remove(pos);
				stores[((pos.x >> 4) * 2 + (pos.y >> 4)) * 2 + (pos.z >> 4)].clearRenderers();
				notifyNear(pos.x, pos.y, pos.z);
			}
		}
		tiles.clear();
		tiles = null;
	}
	
	public void drawEntitys(){
		for(int d = 0; d < entitys.size(); ++d){
			entitys.get(d).draw();
		}
		for(Entry<Vector3i, TileEntity> e : tileEntitys.entrySet()){
			e.getValue().draw();
		}
	}

	@Override
	public void addEntity(Entity entity) {
		entitys.add(entity);
		isModified = true;
		
	}

	public void removeGraphics() {
		for (BlockStore s : stores) {
			s.removeGraphics();
		}
		
	}
	
	public void removeData(){
		for(BlockStore s : stores){
			s.removeData();
		}
	}

	@Override
	public ArrayList<Entity> getEntityList() {
		return entitys;
	}
	
	public boolean isUnloading() {
		return unloading;
	}
	
	public void setUnloading(boolean unloading) {
		this.unloading = unloading;
	}

	@Override
	public byte getBlockLight(int x, int y, int z) {
		return stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].getBlockLight(x & 0xF, y & 0xF, z & 0xF);
	}
	
	@Override
	public byte getBlockLightWithBBCheck(int x, int y, int z){
		if (x < 0 || y < 0 || z < 0 || x > 31 || y > 31 || z > 31) {
			Chunk c = world.getChunk(pos.getCoordOfNearbyChunk(x, y, z));
			if (c != null) {
				return c.getBlockLightWithBBCheck(x & 31, y & 31, z & 31);
			}
			return n_0;
		}
		return getBlockLight(x, y, z);
	}
	
	public void updateLight(int x, int y, int z, int num){
		if(num > 127){
			world.addLightUpdate(pos.worldPos(x, y, z));
			return;
		}
		if (x < 0 || y < 0 || z < 0 || x > 31 || y > 31 || z > 31) {
			ImplChunk c = world.getChunk(pos.getCoordOfNearbyChunk(x, y, z));
			if (c != null) {
				c.updateLight(x & 31, y & 31, z & 31, num + 1);
			}
			return;
		}
		byte blockLight = Block.blockLight[getBlockId(x, y, z)];
		byte opacity = Block.blockOpacity[getBlockId(x, y, z)];
		byte l_0 = getBlockLightWithBBCheck(x + 1, y, z);
		byte l_1 = getBlockLightWithBBCheck(x, y, z + 1);
		byte l_2 = getBlockLightWithBBCheck(x - 1, y, z);
		byte l_3 = getBlockLightWithBBCheck(x, y, z - 1);
		byte l_4 = getBlockLightWithBBCheck(x, y + 1, z);
		byte l_5 = getBlockLightWithBBCheck(x, y - 1, z);
		if((l_0 & 0xF) > opacity){
			l_0 = (byte) ((l_0 & 0xF) - opacity);
		} else l_0 = n_0;
		if((l_1 & 0xF) > opacity){
			l_1 = (byte) ((l_1 & 0xF) - opacity);
		} else l_1 = n_0;
		if((l_2 & 0xF) > opacity){
			l_2 = (byte) ((l_2 & 0xF) - opacity);
		} else l_2 = n_0;
		if((l_3 & 0xF) > opacity){
			l_3 = (byte) ((l_3 & 0xF) - opacity);
		} else l_3 = n_0;
		if((l_4 & 0xF) > opacity){
			l_4 = (byte) ((l_4 & 0xF) - opacity);
		} else l_4 = n_0;
		if((l_5 & 0xF) > opacity){
			l_5 = (byte) ((l_5 & 0xF) - opacity);
		} else l_5 = n_0;
		if((l_0 & 0xF) > (blockLight & 0xF)){
			blockLight = l_0;
		}
		if((l_1 & 0xF) > (blockLight & 0xF)){
			blockLight = l_1;
		}
		if((l_2 & 0xF) > (blockLight & 0xF)){
			blockLight = l_2;
		}
		if((l_3 & 0xF) > (blockLight & 0xF)){
			blockLight = l_3;
		}
		if((l_4 & 0xF) > (blockLight & 0xF)){
			blockLight = l_4;
		}
		if((l_5 & 0xF) > (blockLight & 0xF)){
			blockLight = l_5;
		}
		
		if(getBlockLight(x, y, z) != blockLight){
			setBlockLight(x, y, z, blockLight);
			notifyNear(x, y, z);
			updateLight(x + 1, y, z, num + 1);
			updateLight(x, y, z + 1, num + 1);
			updateLight(x - 1, y, z, num + 1);
			updateLight(x, y, z - 1, num + 1);
			updateLight(x, y + 1, z, num + 1);
			updateLight(x, y - 1, z, num + 1);
			//setDirty(x, y, z);
		}
		
	}
	
	@Override
	public void initialLight(){
		if(lit && skyChecked) return;
		if(!lit) initialBlockLight();
		else initialSkyLight();
	}
	
	public void initialSkyLight() {
		MapData dat = world.getMapData(this);
		if(dat == null) return;
		int amt = 0;
		int z;
		while(amt < 5){
			z = skyCheckZ;
			for(int x = 0; x < 32; ++x){
				if((dat.getHeight(x, z) >> 5) != pos.y) continue;
				if(Block.blockOpacity[getBlockId(x, dat.getHeight(x, z) & 31, z)] < 5){
					world.downScan(pos.worldX(x), dat.getHeight(x, z), pos.worldZ(z), 3);
				}
			}
			++skyCheckZ;
			if(skyCheckZ == 32){
				skyChecked = true;
				return;
			}
			++amt;
		}
	}

	public void initialBlockLight(){
		stat_ilightPositions = ilightPositions;
		stat_ilightingChunk = pos;
		if(ilightPositions == null){
			stat_ilightPositions = ilightPositions = new LinkedList<>();
			int y, z;
			byte light;
			for(int x = 0; x < 32; ++x){
				for(y = 0; y < 32; ++y){
					for(z = 0; z < 32; ++z){
						light = Block.blockLight[getBlockId(x, y, z)];
						if(light == 0) continue;
						setBlockLight(x, y, z, light);
						ilightPositions.add(new Vector3i(x, y, z));
					}
				}
			}
			stat_ilightPositions = null;
			stat_ilightingChunk = null;
			return;
		}
		Direction dir;
		int d;
		int upto = 0;
		Vector3i pos;
		while((pos = ilightPositions.poll()) != null && upto < 1000){
			if(initialUpdateLight(pos.x, pos.y, pos.z, 0)) ++upto;
			else {
				for(d = 0; d < 6; ++d){
					dir = Direction.getDirection(d);
					if(initialUpdateLight(pos.x + dir.getX(), pos.y + dir.getY(), pos.z + dir.getZ(), 0)) ++upto;
				}
			}
		}
		stat_ilightPositions = null;
		stat_ilightingChunk = null;
		if(ilightPositions.size() > 0) return;
		lit = true;
	}
	
	public boolean initialUpdateLight(int x, int y, int z, int num){
		if(num > 3){
			ChunkPos temp = pos.subtract(stat_ilightingChunk);
			stat_ilightPositions.add(new Vector3i((int)temp.worldX(x), (int)temp.worldY(y), (int)temp.worldZ(z)));
			return true;
		}
		if (x < 0 || y < 0 || z < 0 || x > 31 || y > 31 || z > 31) {
			ImplChunk c = world.getChunk(pos.getCoordOfNearbyChunk(x, y, z));
			if (c != null) {
				return c.initialUpdateLight(x & 31, y & 31, z & 31, num + 1);
			}
			return false;
		}
		byte blockLight = Block.blockLight[getBlockId(x, y, z)];
		if(blockLight != 0) return false; //This stops world-gen lag
		byte opacity = Block.blockOpacity[getBlockId(x, y, z)];
		byte l_0 = getBlockLightWithBBCheck(x + 1, y, z);
		byte l_1 = getBlockLightWithBBCheck(x, y, z + 1);
		byte l_2 = getBlockLightWithBBCheck(x - 1, y, z);
		byte l_3 = getBlockLightWithBBCheck(x, y, z - 1);
		byte l_4 = getBlockLightWithBBCheck(x, y + 1, z);
		byte l_5 = getBlockLightWithBBCheck(x, y - 1, z);
		if((l_0 & 0xF) > opacity){
			l_0 = (byte) ((l_0 & 0xF) - opacity);
		} else l_0 = n_0;
		if((l_1 & 0xF) > opacity){
			l_1 = (byte) ((l_1 & 0xF) - opacity);
		} else l_1 = n_0;
		if((l_2 & 0xF) > opacity){
			l_2 = (byte) ((l_2 & 0xF) - opacity);
		} else l_2 = n_0;
		if((l_3 & 0xF) > opacity){
			l_3 = (byte) ((l_3 & 0xF) - opacity);
		} else l_3 = n_0;
		if((l_4 & 0xF) > opacity){
			l_4 = (byte) ((l_4 & 0xF) - opacity);
		} else l_4 = n_0;
		if((l_5 & 0xF) > opacity){
			l_5 = (byte) ((l_5 & 0xF) - opacity);
		} else l_5 = n_0;
		if((l_0 & 0xF) > (blockLight & 0xF)){
			blockLight = l_0;
		}
		if((l_1 & 0xF) > (blockLight & 0xF)){
			blockLight = l_1;
		}
		if((l_2 & 0xF) > (blockLight & 0xF)){
			blockLight = l_2;
		}
		if((l_3 & 0xF) > (blockLight & 0xF)){
			blockLight = l_3;
		}
		if((l_4 & 0xF) > (blockLight & 0xF)){
			blockLight = l_4;
		}
		if((l_5 & 0xF) > (blockLight & 0xF)){
			blockLight = l_5;
		}
		
		if(getBlockLight(x, y, z) != blockLight){
			setBlockLight(x, y, z, blockLight);
			notifyNear(x, y, z);
			initialUpdateLight(x + 1, y, z, num + 1);
			initialUpdateLight(x, y, z + 1, num + 1);
			initialUpdateLight(x - 1, y, z, num + 1);
			initialUpdateLight(x, y, z - 1, num + 1);
			initialUpdateLight(x, y + 1, z, num + 1);
			initialUpdateLight(x, y - 1, z, num + 1);
			//setDirty(x, y, z);
		}
		return true;
	}
	
	protected void checkLight(int x, int y, int z, short bid) {
		if (!populated) return;
		if ((Block.blockLight[bid] != Block.blockLight[getBlockId(x, y, z)]) || (Block.blockOpacity[bid] != Block.blockOpacity[getBlockId(x, y, z)])) updateLight(x, y, z, 0);
	}

}
