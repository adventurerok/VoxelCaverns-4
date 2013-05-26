package vc4.api.item;

import java.awt.Color;
import java.util.*;

import vc4.api.entity.EntityPlayer;
import vc4.api.stats.*;

public class ItemFood extends Item {
	
	int[] textures = new int[10];
	Color[] colors = new Color[10];
	Stats[] data = new Stats[10];
	String[] names = new String[10];
	
	ArrayList<ItemStack> creative = new ArrayList<>();

	public ItemFood(int id) {
		super(id, 0);
	}
	
	@Override
	public void onRightClick(EntityPlayer player, ItemStack item) {
		if (player.getCoolDown() > 0.1) return;
		Stats stats = getStats(item);
		player.heal(stats.getStatInt("hp_consume", 0));
		Stat st = stats.getStatHealing("he_consume");
		if(st instanceof StatHealing){
			StatHealing heal = (StatHealing) st;
			int amt = heal.getAmount();
			if(heal.isNegative()){
				amt = -amt;
				amt = Math.max(amt, heal.getMax() - player.getHealing());
			} else amt = Math.min(amt, heal.getMax() - player.getHealing());
			player.healing += amt;
			if(player.healing > player.getMaxHealing()) player.setMaxHealing(player.healing);
		}
		item.decrementAmount();
		player.setCoolDown(350);
	}
	
	public void setFoodData(int damage, int texture, String name, int instant, int healing, int healmax){
		setFoodData(damage, texture, name, instant, healing, healmax, Color.white);
	}
	
	public void setFoodData(int damage, int texture, String name, int instant, int healing, int healmax, Color color){
		if(damage >= data.length){
			data = Arrays.copyOf(data, damage + 10);
			textures = Arrays.copyOf(textures, damage + 10);
			colors = Arrays.copyOf(colors, damage + 10);
			names = Arrays.copyOf(names, damage + 10);
		}
		Stats stat = new Stats();
		if(instant != 0) stat.setStat("hp_consume", new StatInt(instant));
		if(healing != 0) stat.setStat("he_consume", new StatHealing(false, healing, healmax));
		data[damage] = stat;
		textures[damage] = texture;
		colors[damage] = color;
		names[damage] = name;
		creative.add(new ItemStack(id, damage, 1));
	}
	
	@Override
	public int getTextureIndex(ItemStack current) {
		return textures[current.getDamage()];
	}
	
	@Override
	public Color getColor(ItemStack item) {
		return colors[item.getDamage()];
	}
	
	@Override
	public Collection<ItemStack> getCreativeItems() {
		return creative;
	}
	
	@Override
	public Stats getStats(ItemStack item) {
		return data[item.getDamage()];
	}
	
	@Override
	public String getModifiedItemName(ItemStack stack) {
		return "food." + names[stack.getDamage()];
	}
	
	

}
