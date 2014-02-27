package vc4.vanilla.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import vc4.api.entity.EntityPlayer;
import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.api.util.Direction;
import vc4.api.util.RayTraceResult;
import vc4.vanilla.ItemTexture;
import vc4.vanilla.Vanilla;

public class ItemSeeds extends Item {

	public ItemSeeds(int id) {
		super(id, ItemTexture.seeds);
	}

	@Override
	public String getModifiedItemName(ItemStack stack) {
		if (stack.getDamage() == 0) return "seeds.wheat";
		else if (stack.getDamage() == 1) return "seeds.barley";
		else return "seeds";
	}

	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> res = new ArrayList<>();
		res.add(new ItemStack(id, 0, 1));
		res.add(new ItemStack(id, 1, 1));
		return res;
	}

	@Override
	public Color getColor(ItemStack item) {
		return ItemCrop.colors[item.getDamage()];
	}

	@Override
	public void onRightClick(EntityPlayer player, ItemStack item) {
		if (player.getCoolDown() > 0.1) return;
		RayTraceResult rays = player.getRays();
		if (player.getWorld().getBlockId(rays.x, rays.y, rays.z) == Vanilla.stakes.uid) {
			if (item.getDamage() == 0) Vanilla.wheat.place(player.getWorld(), rays.x, rays.y, rays.z, player, item);
			else if (item.getDamage() == 1) Vanilla.barley.place(player.getWorld(), rays.x, rays.y, rays.z, player, item);
			player.getWorld().setBlockData(rays.x, rays.y, rays.z, 16);
		}
		Direction d = Direction.getDirection(rays.side);
		long x = rays.x + d.getX();
		long y = rays.y + d.getY();
		long z = rays.z + d.getZ();
		player.setCoolDown(200);
		if (rays == null || rays.isEntity) return;
		if (item.getDamage() == 0 && player.getWorld().getBlockType(x, y, z).replacableBy(player.getWorld(), x, y, z, Vanilla.wheat.uid, (byte) 0)) Vanilla.wheat.place(player.getWorld(), x, y, z, player, item);
		else if (item.getDamage() == 1 && player.getWorld().getBlockType(x, y, z).replacableBy(player.getWorld(), x, y, z, Vanilla.barley.uid, (byte) 0)) Vanilla.barley.place(player.getWorld(), x, y, z, player, item);
	}

}
