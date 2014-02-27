package vc4.vanilla.generation.dungeon.loot;

import java.util.ArrayList;
import java.util.Random;

import vc4.api.container.Container;
import vc4.api.item.ItemStack;

public class LootChest {

	private ArrayList<LootItem> loot = new ArrayList<>();

	public LootChest addLoot(LootItem loot) {
		this.loot.add(loot);
		return this;
	}

	public void clear() {
		loot.clear();
	}

	public void generate(Random rand, Container container) {
		generate(rand, container, 0, container.getSize());
	}

	public void generate(Random rand, Container container, int min, int max) {
		for (LootItem l : loot) {
			for (int d = 0; d < l.getNumber(); ++d) {
				ItemStack itm = l.getRandomLoot(rand);
				if (itm == null || !itm.exists()) continue;
				int slot = min + ((max - min) > 0 ? rand.nextInt(max - min) : 0);
				container.setItem(slot, itm);
			}
		}
	}
}
