package vc4.vanilla.item;

import java.awt.Color;

import vc4.api.item.ItemMultitexture;
import vc4.api.item.ItemStack;
import vc4.api.tool.*;
import vc4.vanilla.ItemTexture;
import vc4.vanilla.Vanilla;

public class ItemTool extends ItemMultitexture {

	Tool tool;
	Color handle;
	Color metal;

	public ItemTool(int id, ToolType type, ToolMaterial material) {
		super(id);
		maxStack = 1;
		textureIndex = ItemTexture.toolHandle;
		damageOnUse = true;
		maxDamage = material.getDurability();
		tool = new Tool(type, material);
		if (tool.getPower() < Vanilla.materialMithril.getPower()) handle = new Color(114, 71, 15);
		else if (tool.getPower() < Vanilla.materialAdamantite.getPower()) handle = new Color(158, 139, 139);
		else handle = new Color(225, 50, 0);

		if (tool.getType() == ToolType.pickaxe) mtIndex = ItemTexture.pickaxeHead;
		else if (tool.getType() == ToolType.axe) mtIndex = ItemTexture.axeHead;
		else if (tool.getType() == ToolType.spade) mtIndex = ItemTexture.shovelHead;
		else if (tool.getType() == ToolType.hoe) mtIndex = ItemTexture.hoeHead;

		if (tool.getMaterial().getId() == Vanilla.materialWood.getId()) metal = new Color(114, 71, 15);
		else if (tool.getMaterial().getId() == Vanilla.materialStone.getId()) metal = new Color(40, 40, 40);
		else if (tool.getMaterial().getId() == Vanilla.materialCopper.getId()) metal = new Color(127, 51, 0);
		else if (tool.getMaterial().getId() == Vanilla.materialBronze.getId()) metal = new Color(107, 21, 0);
		else if (tool.getMaterial().getId() == Vanilla.materialIron.getId()) metal = new Color(158, 139, 139);
		else if (tool.getMaterial().getId() == Vanilla.materialKradonium.getId()) metal = new Color(139, 0, 255);
		else if (tool.getMaterial().getId() == Vanilla.materialSilver.getId()) metal = new Color(192, 192, 192);
		else if (tool.getMaterial().getId() == Vanilla.materialGold.getId()) metal = new Color(255, 226, 102);
		else if (tool.getMaterial().getId() == Vanilla.materialMithril.getId()) metal = new Color(0, 38, 255);
		else if (tool.getMaterial().getId() == Vanilla.materialTitanium.getId()) metal = new Color(133, 105, 94);
		else if (tool.getMaterial().getId() == Vanilla.materialHellish.getId()) metal = new Color(225, 50, 0);
		else if (tool.getMaterial().getId() == Vanilla.materialPlatinum.getId()) metal = new Color(168, 167, 165);
		else if (tool.getMaterial().getId() == Vanilla.materialAdamantite.getId()) metal = new Color(60, 93, 60);
		else if (tool.getMaterial().getId() == Vanilla.materialUnholy.getId()) metal = new Color(16, 16, 16);
		else if (tool.getMaterial().getId() == Vanilla.materialSacred.getId()) metal = new Color(238, 238, 238);
	}

	@Override
	public int getTextureIndex(ItemStack current) {
		return textureIndex;
	}

	@Override
	public Color getColor(ItemStack item) {
		return handle;
	}

	@Override
	public int getMultitextureTextureIndex(ItemStack item) {
		return mtIndex;
	}

	@Override
	public Color getMultitextureColor(ItemStack item) {
		return metal;
	}
	
	@Override
	public String getModifiedItemName(ItemStack stack) {
		return tool.getType().getName() + "." + tool.getMaterial().getName();
	}

	@Override
	public Tool getTool(ItemStack item) {
		return tool;
	}

}
