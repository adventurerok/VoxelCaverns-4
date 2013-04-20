/**
 * 
 */
package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.block.Material;

/**
 * @author paul
 *
 */
public class BlockSand extends Block {

	/**
	 * @param uid
	 * @param texture
	 * @param m
	 */
	public BlockSand(short uid) {
		super(uid, 15, Material.getMaterial("sand"));
		
	}

}
