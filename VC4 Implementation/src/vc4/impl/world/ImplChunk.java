/**
 * 
 */
package vc4.impl.world;

import java.util.Random;

import vc4.api.block.Block;
import vc4.api.entity.Entity;
import vc4.api.generator.GeneratorOutput;
import vc4.api.math.MathUtils;
import vc4.api.util.EntityList;
import vc4.api.vector.Vector3d;
import vc4.api.world.Chunk;
import vc4.api.world.ChunkPos;
import vc4.api.world.World;

/**
 * @author paul
 * 
 */
public class ImplChunk implements Chunk {

	BlockStore[] stores = new BlockStore[8];
	ChunkPos pos;
	private ImplWorld world;
	private boolean populated;
	public EntityList entitys = new EntityList();

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

	public void setData(GeneratorOutput data) {
		for (int d = 0; d < 8; ++d) {
			stores[d].blocks = new short[4096];
			stores[d].data = new byte[4096];
		}
		for (int x = 0; x < 32; ++x) {
			for (int y = 0; y < 32; ++y) {
				for (int z = 0; z < 32; ++z) {
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
		stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].setBlockId(x & 0xF, y & 0xF, z & 0xF, id);
		notifyNear(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.Chunk#setBlockData(int, int, int, byte)
	 */
	@Override
	public void setBlockData(int x, int y, int z, byte data) {
		stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].setBlockData(x & 0xF, y & 0xF, z & 0xF, data);
		notifyNear(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.Chunk#setBlockIdData(int, int, int, short, byte)
	 */
	@Override
	public void setBlockIdData(int x, int y, int z, short id, byte data) {
		stores[((x >> 4) * 2 + (y >> 4)) * 2 + (z >> 4)].setBlockIdData(x & 0xF, y & 0xF, z & 0xF, id, data);
		notifyNear(x, y, z);
	}

	public void notifyNear(int x, int y, int z) {
		if (x % 16 == 0) world.getBlockStoreFromWorld(pos.worldX(x - 1), pos.worldY(y), pos.worldZ(z)).clearRenderers();
		else if (x % 16 == 15) world.getBlockStoreFromWorld(pos.worldX(x + 1), pos.worldY(y), pos.worldZ(z)).clearRenderers();
		if (y % 16 == 0) world.getBlockStoreFromWorld(pos.worldX(x), pos.worldY(y - 1), pos.worldZ(z)).clearRenderers();
		else if (y % 16 == 15) world.getBlockStoreFromWorld(pos.worldX(x), pos.worldY(y + 1), pos.worldZ(z)).clearRenderers();
		if (z % 16 == 0) world.getBlockStoreFromWorld(pos.worldX(x), pos.worldY(y), pos.worldZ(z - 1)).clearRenderers();
		else if (z % 16 == 15) world.getBlockStoreFromWorld(pos.worldX(x), pos.worldY(y), pos.worldZ(z + 1)).clearRenderers();
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
		for (int x = -1; x < 2; ++x) {
			for (int y = -1; y < 2; ++y) {
				for (int z = -1; z < 2; ++z) {
					if (!world.chunkExists(x + pos.x, y + pos.y, z + pos.z)) return false;
				}
			}
		}
		return true;
	}

	public void update(Random rand) {
		EntityList alive = new EntityList();
		for (int dofor = entitys.size() - 1; dofor > -1; --dofor) {
			Entity e = entitys.get(dofor);
			if (e == null) continue;
			if (!e.hadUpdate) e.update();
			if (!e.needsRemoving()) {
				long x = MathUtils.floor(e.position.x) >> 5;
				long y = MathUtils.floor(e.position.y) >> 5;
				long z = MathUtils.floor(e.position.z) >> 5;
				if (ChunkPos.create(x, y, z).equals(pos) || !world.chunkExists(x, y, z)) {
					e.hadUpdate = false;
					alive.add(e);
				} else {
					e.hadUpdate = true;
					world.chunks.get(ChunkPos.create(x, y, z)).entitys.add(e);
				}

			}
		}
		entitys = alive;
		for(int d = 0; d < 10; ++d){
			int x = rand.nextInt(32);
			int y = rand.nextInt(32);
			int z = rand.nextInt(32);
			int time = Block.byId(getBlockId(x, y, z)).blockUpdate(world, rand, pos.worldX(x), pos.worldY(y), pos.worldZ(z));
			if(time > 0){
				world.scheduleBlockUpdate(pos.worldX(x), pos.worldY(y), pos.worldZ(z), time);
			}
		}
	}
	
	public void drawEntitys(){
		for(int d = 0; d < entitys.size(); ++d){
			entitys.get(d).draw();
		}
	}

	@Override
	public void addEntity(Entity entity) {
		entitys.add(entity);
		
	}

}
