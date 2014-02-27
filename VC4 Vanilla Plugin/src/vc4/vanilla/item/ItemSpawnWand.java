package vc4.vanilla.item;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import vc4.api.VoxelCaverns;
import vc4.api.entity.Entity;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.api.logging.Logger;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3d;
import vc4.vanilla.ItemTexture;

public class ItemSpawnWand extends Item {

	public ItemSpawnWand(int id) {
		super(id);
		textureIndex = ItemTexture.toolHandle;
	}

	@Override
	public void onRightClick(EntityPlayer player, ItemStack item) {
		if (player.getRays() == null || player.getRays().isEntity) return;
		if (player.getCoolDown() > 0.01) return;
		Vector3d pos = player.getRays().getBlockPos().move(1, Direction.getDirection(player.getRays().side)).toVector3d().add(0.5, 1.5, 0.5);
		Constructor<? extends Entity> cons = Entity.getEntityType(player.getWorld().getEntityName(item.getDamage()));
		try {
			Entity ent = cons.newInstance(player.getWorld());
			ent.setPosition(pos);
			ent.addToWorld();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.getLogger(ItemSpawnWand.class).warning("Failed to spawn entity", e);
		}
		player.setCoolDown(150);
	}

	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> res = new ArrayList<>();
		for (String s : Entity.getEntityNames()) {
			int dmg = VoxelCaverns.getCurrentWorld().getRegisteredEntity(s);
			res.add(new ItemStack(id, dmg, 1));
		}
		return res;
	}

}
