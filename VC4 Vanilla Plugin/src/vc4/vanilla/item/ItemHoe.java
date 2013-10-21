package vc4.vanilla.item;

import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.tool.ToolMaterial;
import vc4.api.tool.ToolType;
import vc4.api.util.RayTraceResult;
import vc4.vanilla.Vanilla;

public class ItemHoe extends ItemTool {

	public ItemHoe(int id, ToolMaterial material) {
		super(id, ToolType.hoe, material);
	}
	
	@Override
	public void onRightClick(EntityPlayer player, ItemStack item) {
		RayTraceResult rays = player.getRays();
		if(rays == null || rays.isEntity) return;
		int id = player.getWorld().getBlockId(rays.x, rays.y, rays.z);
		if(id != Vanilla.dirt.uid && id != Vanilla.grass.uid) return;
		player.getWorld().setBlockId(rays.x, rays.y, rays.z, Vanilla.farmland.uid);
		item.damage();
	}

}
