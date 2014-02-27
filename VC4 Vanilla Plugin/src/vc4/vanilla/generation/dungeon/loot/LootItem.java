package vc4.vanilla.generation.dungeon.loot;

import java.util.Random;

import vc4.api.item.ItemStack;

public class LootItem {

	ItemStack item;
	int min = 1;
	int max;
	int number;
	double chance = 0.46;

	public LootItem(ItemStack item, int min, int max, int number, double chance) {
		super();
		this.item = item;
		this.min = min;
		this.max = max;
		this.number = number;
		this.chance = chance;
	}

	public LootItem(ItemStack item, int min, int max, int number) {
		super();
		this.item = item;
		this.min = min;
		this.max = max;
		this.number = number;
	}

	public ItemStack getRandomLoot(Random rand) {
		if (rand.nextDouble() > chance) return null;
		ItemStack stack = item.clone();
		int diff = max - min;
		stack.setAmount(min + (diff > 0 ? rand.nextInt(max - min) : 0));
		return stack;
	}

	public int getNumber() {
		return number;
	}

}
