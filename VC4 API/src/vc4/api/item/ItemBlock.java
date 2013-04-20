/**
 * 
 */
package vc4.api.item;

import java.awt.Color;

import vc4.api.block.Block;

/**
 * @author paul
 *
 */
public class ItemBlock extends Item{

	/**
	 * @param id
	 */
	public ItemBlock(int id) {
		super(id);
		
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.item.Item#getTextureIndex(vc4.api.item.ItemStack)
	 */
	@Override
	public int getTextureIndex(ItemStack current) {
		return Block.byId(current.getId()).getTextureIndex(current, 0);
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.item.Item#getColor(vc4.api.item.ItemStack)
	 */
	@Override
	public Color getColor(ItemStack item) {
		return Block.byId(item.getId()).getColor(item, 0);
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.item.Item#getLocalizazedItemName(vc4.api.item.ItemStack)
	 */
	@Override
	public String getLocalizazedItemName(ItemStack stack) {
		return Block.byId(stack.getId()).getLocalizedName(stack);
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.item.Item#getLocalizedItemDescription(vc4.api.item.ItemStack)
	 */
	@Override
	public String getLocalizedItemDescription(ItemStack stack) {
		return Block.byId(stack.getId()).getLocalizedDescription(stack);
	}

}
