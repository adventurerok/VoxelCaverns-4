package vc4.api.item;

import java.awt.Color;

public class ItemMultitexture extends Item implements IItemMultitexture {

	protected int mtIndex;
	protected Color mtColor = Color.white;

	public ItemMultitexture(int id) {
		super(id);
	}

	public ItemMultitexture(int id, int textureIndex) {
		super(id, textureIndex);
	}

	@Override
	public int getMultitextureTextureIndex(ItemStack item) {
		return mtIndex;
	}

	@Override
	public Color getMultitextureColor(ItemStack item) {
		return mtColor;
	}

}
