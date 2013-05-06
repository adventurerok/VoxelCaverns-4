/**
 * 
 */
package vc4.api.item;

import java.awt.Color;

import vc4.api.entity.EntityPlayer;
import vc4.api.text.Localization;
import vc4.api.tool.Tool;

/**
 * @author paul
 *
 */
public class Item {

	public final int id;
	protected int textureIndex;
	protected String name;
	protected boolean damageOnUse;
	protected int maxDamage;
	protected int maxStack = 99;
	protected boolean overrideLeftClick;
	
	protected static Item[] itemsList = new Item[32768];
	public Item(int id) {
		super();
		this.id = id;
		itemsList[id] = this;
	}
	public Item(int id, int textureIndex) {
		this(id);
		this.textureIndex = textureIndex;
	}
	
	public static Item byId(int id){
		return itemsList[id];
	}
	
	public static void addItemBlock(int id){
		new ItemBlock(id);
	}
	
	public Tool getTool(ItemStack item){
		return null;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public Item setName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * @return the damageOnUse
	 */
	public boolean isDamagedOnUse() {
		return damageOnUse;
	}
	
	/**
	 * @return the maxDamage
	 */
	public int getMaxDamage() {
		return maxDamage;
	}
	
	public int getMaxStack(int damage){
		return maxStack;
	}
	
	public String getItemName(ItemStack stack){
		return "item." + getModifiedItemName(stack) + ".name";
	}
	public String getItemDescription(ItemStack stack){
		return "item." + getModifiedItemName(stack) + ".desc";
	}
	public String getModifiedItemName(ItemStack stack){
		return name;
	}
	public String getLocalizazedItemName(ItemStack stack){
		return Localization.getLocalization(getItemName(stack));
	}
	public String getLocalizedItemDescription(ItemStack stack) {
		return Localization.getLocalization(getItemDescription(stack));
	}
	
	/**
	 * @return the overrideLeftClick
	 */
	public boolean overrideLeftClick() {
		return overrideLeftClick;
	}
	
	public void onLeftClick(EntityPlayer player, ItemStack item){
		
	}
	
	public void onRightClick(EntityPlayer player, ItemStack item){
		
	}
	
	/**
	 * @param current
	 * @return The items texture index
	 */
	public int getTextureIndex(ItemStack current) {
		return textureIndex;
	}
	
	public Color getColor(ItemStack item){
		return Color.white;
	}
	
	public static void clearItems(){
		itemsList = new Item[32768];
	}
	public ItemStack[] getCreativeItems() {
		return new ItemStack[]{new ItemStack(id, 0, 1)};
	}
	
}
