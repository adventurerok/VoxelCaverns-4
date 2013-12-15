/**
 * 
 */
package vc4.api.generator;

/**
 * @author paul
 *
 */
public class GeneratorOutput {

	public short[] blocks = new short[32*32*32];
	public byte[] data = new byte[32*32*32];
	
	public void setBlockId(int x, int y, int z, int id){
		blocks[(y*32+x)*32+z] = (short) id;
	}
	
	public void setBlockData(int x, int y, int z, int data){
		this.data[(y*32+x)*32+z] = (byte) data;
	}
	
	public void setBlockData(int x, int y, int z, byte data){
		this.data[(y*32+x)*32+z] = data;
	}
	
	public static int arrayCalc(int x, int y, int z){
		return (y*32+x)*32+z;
	}

	public int getBlockId(int x, int y, int z) {
		return blocks[arrayCalc(x, y, z)];
	}
	

}
