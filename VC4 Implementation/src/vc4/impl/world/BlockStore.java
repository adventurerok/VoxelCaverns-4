/**
 * 
 */
package vc4.impl.world;

import vc4.api.block.Block;
import vc4.api.render.ChunkRenderer;
import vc4.api.vector.Vector3d;
import vc4.api.world.Chunk;
import vc4.api.world.MapData;

/**
 * @author paul
 *
 */
public class BlockStore {
	
	

	public short[] blocks;
	public byte[] data;
	public byte[] light = new byte[4096];
	
//	public DataRenderer oldData[];
	public ChunkRenderer currentData[] = new ChunkRenderer[3];
	int compileState = 0;
	
	public int xMod, yMod, zMod;
	int oldCompile;
	
	
	public short[] getBlocks() {
		return blocks;
	}
	
	public void setBlocks(short[] blocks) {
		this.blocks = blocks;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public BlockStore setLight(byte[] light) {
		this.light = light;
		return this;
	}
	
	public byte[] getLight() {
		return light;
	}
	
	BlockStore(int xMod, int yMod, int zMod) {
		super();
		this.xMod = xMod;
		this.yMod = yMod;
		this.zMod = zMod;
	}

	public short getBlockId(int x, int y, int z){
		return blocks != null ? blocks[arrayCalc(x, y, z)] : 0;
	}
	
	public byte getBlockData(int x, int y, int z){
		return data != null ? data[arrayCalc(x, y, z)] : 0;
	}
	
	public byte getBlockLight(int x, int y, int z){
		return light != null ? light[arrayCalc(x, y, z)] : 0;
	}
	
	public double distance(Vector3d pos, Chunk chunk){
		double x = pos.x - chunk.getChunkPos().worldX(xMod);
		double y = pos.y - chunk.getChunkPos().worldY(yMod);
		double z = pos.z - chunk.getChunkPos().worldZ(zMod);
		return x*x + y*y + z*z;
	}
	
	public static int arrayCalc(int x, int y, int z){
		return (x * 16 + z) * 16 + y;
	}
	
	public boolean setBlockId(int x, int y, int z, short id){
		if(blocks == null && id != 0){
			blocks = new short[4096];
			blocks[arrayCalc(x, y, z)] = id;
			clearRenderers();
			return true;
		} else {
			int ac = arrayCalc(x, y, z);
			if(blocks != null && blocks[ac] != id){
				blocks[ac] = id;
				clearRenderers();
				return true;
			}
		}
		return false;
	}
	

	public boolean setBlockData(int x, int y, int z, byte d){
		if(data == null && d != 0){
			data = new byte[4096];
			data[arrayCalc(x, y, z)] = d;
			clearRenderers();
			return true;
		} else {
			int ac = arrayCalc(x, y, z);
			if(data != null && data[ac] != d){
				data[ac] = d;
				clearRenderers();
				return true;
			}
		}
		return false;
	}
	
	public boolean setBlockLight(int x, int y, int z, byte d){
		if(light == null && d != 0){
			light = new byte[4096];
			light[arrayCalc(x, y, z)] = d;
			clearRenderers();
			return true;
		} else {
			int ac = arrayCalc(x, y, z);
			if(light != null && light[ac] != d){
				light[ac] = d;
				clearRenderers();
				return true;
			}
		}
		return false;
	}
	
	public boolean setBlockIdData(int x, int y, int z, short id, byte d){
		boolean ret = false;
		if(blocks == null && id != 0){
			blocks = new short[4096];
			blocks[arrayCalc(x, y, z)] = id;
			ret = true;
		} else {
			int ac = arrayCalc(x, y, z);
			if(blocks != null && blocks[ac] != id){
				blocks[ac] = id;
				ret = true;
			}
		}
		if(data == null && d != 0){
			data = new byte[4096];
			data[arrayCalc(x, y, z)] = d;
			ret = true;
		} else {
			int ac = arrayCalc(x, y, z);
			if(data != null && data[ac] != d){
				data[ac] = d;
				ret = true;
			}
		}
		if(ret) clearRenderers();
		return ret;
	}
	
	public void calculateData(Chunk c, MapData m){
		compileState = 1;
		currentData[0] = new ChunkRenderer();
		currentData[1] = new ChunkRenderer();
		currentData[2] = new ChunkRenderer();
		boolean allAir = true;
		boolean noData = true;
		int y, z, i;
		byte data;
		for(int x = 0; x < 16; ++x){
			for(y = 0; y < 16; ++y){
				for(z = 0; z < 16; ++z){
					i = getBlockId(x, y, z);
					if(i < 1) continue;
					allAir = false;
					data = getBlockData(x, y, z);
					if(data != 0) noData = false;
					Block.byId(i).getRenderer().renderBlock(c, m, x | xMod, y | yMod, z | zMod, Block.byId(i), data, currentData);
				}
			}
			
		}
		if(allAir){
			blocks = null;
			this.data = null;
		} else if(noData) this.data = null;
		
		compileState = 2;
	}
	
	public void compileRenderData(){
		compileState = 3;
		for(int d = 0; d < 3; ++d) currentData[d].compile();
		compileState = 4;
	}
	
	public void clearRenderers(){
		oldCompile = compileState;
		compileState = 0;
		//oldData = currentData;
	}

	/**
	 * 
	 */
	public void empty() {
		removeData();
		removeGraphics();
	}

	public void removeGraphics() {
		if(currentData != null){
			for(ChunkRenderer r : currentData){
				if(r != null) r.destroy();
			}
		}
	}
	
	public void removeData(){
		blocks = null;
		data = null;
	}

}
