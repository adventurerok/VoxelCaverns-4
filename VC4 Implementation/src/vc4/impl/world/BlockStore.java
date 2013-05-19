/**
 * 
 */
package vc4.impl.world;

import vc4.api.block.Block;
import vc4.api.render.DataRenderer;
import vc4.api.vector.Vector3d;
import vc4.api.world.Chunk;

/**
 * @author paul
 *
 */
public class BlockStore {
	
	

	public short[] blocks;
	public byte[] data;
	
//	public DataRenderer oldData[];
	public DataRenderer currentData[] = new DataRenderer[3];
	int compileState = 0;
	
	public int xMod, yMod, zMod;
	int oldCompile;
	
	
	
	
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
	
	public double distance(Vector3d pos, Chunk chunk){
		double x = pos.x - chunk.getChunkPos().worldX(xMod);
		double y = pos.y - chunk.getChunkPos().worldY(yMod);
		double z = pos.z - chunk.getChunkPos().worldZ(zMod);
		return x*x + y*y + z*z;
	}
	
	public static int arrayCalc(int x, int y, int z){
		return (x * 16 + z) * 16 + y;
	}
	
	public void setBlockId(int x, int y, int z, short id){
		if(blocks == null && id != 0){
			blocks = new short[4096];
			blocks[arrayCalc(x, y, z)] = id;
		} else {
			int ac = arrayCalc(x, y, z);
			if(blocks != null && blocks[ac] != id){
				blocks[ac] = id;
			}
		}
		clearRenderers();
	}
	

	public void setBlockData(int x, int y, int z, byte d){
		if(data == null && d != 0){
			data = new byte[4096];
			data[arrayCalc(x, y, z)] = d;
		} else {
			int ac = arrayCalc(x, y, z);
			if(data != null && data[ac] != d){
				data[ac] = d;
			}
		}
		clearRenderers();
	}
	
	public void setBlockIdData(int x, int y, int z, short id, byte d){
		if(blocks == null && id != 0){
			blocks = new short[4096];
			blocks[arrayCalc(x, y, z)] = id;
		} else {
			int ac = arrayCalc(x, y, z);
			if(blocks != null && blocks[ac] != id){
				blocks[ac] = id;
			}
		}
		if(data == null && d != 0){
			data = new byte[4096];
			data[arrayCalc(x, y, z)] = d;
		} else {
			int ac = arrayCalc(x, y, z);
			if(data != null && data[ac] != d){
				data[ac] = d;
			}
		}
		clearRenderers();
	}
	
	public void calculateData(Chunk c){
		compileState = 1;
		currentData[0] = new DataRenderer();
		currentData[1] = new DataRenderer();
		currentData[2] = new DataRenderer();
		boolean allAir = true;
		boolean noData = true;
		for(int x = 0; x < 16; ++x){
			for(int y = 0; y < 16; ++y){
				for(int z = 0; z < 16; ++z){
					int i = getBlockId(x, y, z);
					if(i < 1) continue;
					allAir = false;
					byte data = getBlockData(x, y, z);
					if(data != 0) noData = false;
					Block.byId(i).getRenderer().renderBlock(c, x | xMod, y | yMod, z | zMod, Block.byId(i), data, currentData);
				}
			}
			
		}
		if(allAir){
			blocks = null;
			data = null;
		} else if(noData) data = null;
		
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
		blocks = null;
		data = null;
//		if(oldData != null){
//			for(DataRenderer r : oldData){
//				if(r != null) r.destroy();
//			}
//		}
		if(currentData != null){
			for(DataRenderer r : currentData){
				if(r != null) r.destroy();
			}
		}
//		oldData = null;
	}

}
