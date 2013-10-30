package vc4.vanilla.item;

import java.util.Random;

import vc4.api.block.Block;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.api.util.XORShiftRandom;
import vc4.vanilla.ItemTexture;
import vc4.vanilla.Vanilla;
import vc4.vanilla.block.BlockCrop;

public class ItemPlantHelp extends Item {

	static Random rand = new XORShiftRandom();
	int type;
	
	public ItemPlantHelp(int id, int type) {
		super(id);
		this.type = type;
		damageOnUse = true;
		maxDamage = 64;
	}
	
	@Override
	public int getTextureIndex(ItemStack current) {
		switch(type){
			case 0: return ItemTexture.fertilizer;
			case 1: return ItemTexture.cure;
			default: return ItemTexture.fertilizer;
		}
	}
	
	@Override
	public void onRightClick(EntityPlayer player, ItemStack item) {
		if(player.getCoolDown() > 0.1) return;
		if(player.getRays() == null || player.getRays().isEntity) return;
		long x = player.getRays().x;
		long y = player.getRays().y;
		long z = player.getRays().z;
		int id = player.getWorld().getBlockId(x, y, z);
		if(!(Block.byId(id) instanceof BlockCrop) || id == Vanilla.deadCrop.uid) return;
		byte data = player.getWorld().getBlockData(x, y, z);
		if(type == 0){
			if((data & 8) != 0) return;
			int icr = data & 7;
			if(icr == 7) return;
			while(item.exists() && icr < 7){
				icr += 1 + rand.nextInt(rand.nextInt(2) + 1);
				if(icr > 7) icr = 7;
				item.damage();
			}
			player.getWorld().setBlockData(x, y, z, (data & 24) + icr);
		} else {
			if((data & 8) != 8) return;
			player.getWorld().setBlockData(x, y, z, data & 23);
			item.damage();
		}
		player.setCoolDown(200);
	}

}
