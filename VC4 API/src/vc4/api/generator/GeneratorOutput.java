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
	
	public static int arrayCalc(int x, int y, int z){
		return (y*32+x)*32+z;
	}

	public int getBlockId(int x, int y, int z) {
		return blocks[arrayCalc(x, y, z)];
	}
	

}
