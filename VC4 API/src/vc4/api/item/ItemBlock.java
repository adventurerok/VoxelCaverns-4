/**
 * 
 */
package vc4.api.item;

import java.awt.Color;
import java.util.Collection;

import vc4.api.block.Block;
import vc4.api.entity.EntityPlayer;
import vc4.api.util.Direction;

/**
 * @author paul
 * 
 */
public class ItemBlock extends Item {

	/**
	 * @param id
	 */
	public ItemBlock(int id) {
		super(id);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.item.Item#getTextureIndex(vc4.api.item.ItemStack)
	 */
	@Override
	public int getTextureIndex(ItemStack current) {
		return Block.byId(current.getId()).getTextureIndex(current, 0);
	}
	
	@Override
	public Collection<ItemStack> getCreativeItems() {
		return Block.byId(id).getCreativeItems();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.item.Item#getColor(vc4.api.item.ItemStack)
	 */
	@Override
	public Color getColor(ItemStack item) {
		return Block.byId(item.getId()).getColor(item, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.item.Item#getLocalizazedItemName(vc4.api.item.ItemStack)
	 */
	@Override
	public String getLocalizazedItemName(ItemStack stack) {
		return Block.byId(stack.getId()).getLocalizedName(stack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.item.Item#getLocalizedItemDescription(vc4.api.item.ItemStack)
	 */
	@Override
	public String getLocalizedItemDescription(ItemStack stack) {
		return Block.byId(stack.getId()).getLocalizedDescription(stack);
	}
	
	

	@Override
	public void onRightClick(EntityPlayer player, ItemStack item) {
		if (player.getRays() == null || player.getRays().isEntity) return;
		if (player.getCoolDown() > 0.1) return;
		long x = player.getRays().x;
		long y = player.getRays().y;
		long z = player.getRays().z;
		if (player.getWorld().getBlockType(x, y, z).canBeReplaced(item.getId(), item.getData())) {
			Block.byId(item.getId()).place(player.getWorld(), x, y, z, player, item);
		} else {
			Direction dir = Direction.getDirection(player.getRays().side);
			if(player.getWorld().getNearbyBlockType(x, y, z, dir).canBeReplaced(item.getId(), item.getData())){
				Block.byId(item.getId()).place(player.getWorld(), x + dir.getX(), y + dir.getY(), z + dir.getZ(), player, item);
			}
		}
		player.setCoolDown(200);
	}
}
