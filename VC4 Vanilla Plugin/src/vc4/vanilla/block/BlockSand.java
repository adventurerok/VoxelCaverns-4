/**
 * 
 */
package vc4.vanilla.block;

import vc4.api.block.*;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;

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
		super(uid, BlockTexture.sand, Material.getMaterial("sand"));
		
	}
	
	@Override
	public boolean canGrowPlant(Plant plant) {
		return plant.getUid() == Vanilla.plantCactus.getUid();
	}

}
