package vc4.vanilla.item;

import vc4.api.entity.EntityPlayer;
import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.vanilla.ItemTexture;
import vc4.vanilla.block.IBlockWrenchable;

public class ItemWrench extends Item {

	public ItemWrench(int id) {
		super(id, ItemTexture.wrench);
		overrideLeftClick = true;
	}

	@Override
	public void onRightClick(EntityPlayer player, ItemStack item) {
		if(player.getCoolDown() > 0.1) return;
		if(player.getRays() == null || player.getRays().isEntity) return;
		player.setCoolDown(200);
		long x = player.getRays().x;
		long y = player.getRays().y;
		long z = player.getRays().z;
		if(!(player.getWorld().getBlockType(x, y, z) instanceof IBlockWrenchable)) return;
		((IBlockWrenchable)player.getWorld().getBlockType(x, y, z)).wrench(player.getWorld(), x, y, z, player.getRays().side, 1);
		
	}
	
	@Override
	public void onLeftClick(EntityPlayer player, ItemStack item) {
		if(player.getCoolDown() > 0.1) return;
		if(player.getRays() == null || player.getRays().isEntity) return;
		player.setCoolDown(200);
		long x = player.getRays().x;
		long y = player.getRays().y;
		long z = player.getRays().z;
		if(!(player.getWorld().getBlockType(x, y, z) instanceof IBlockWrenchable)) return;
		((IBlockWrenchable)player.getWorld().getBlockType(x, y, z)).wrench(player.getWorld(), x, y, z, player.getRays().side, 0);
	}
	
}
