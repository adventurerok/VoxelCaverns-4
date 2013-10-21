package vc4.api.item;


import java.awt.Color;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.jnbt.CompoundTag;
import org.jnbt.ListTag;

import vc4.api.block.Block;
import vc4.api.crafting.CraftingStack;
import vc4.api.entity.EntityPlayer;
import vc4.api.itementity.ItemEntity;
import vc4.api.itementity.ItemEntityEnchantment;
import vc4.api.logging.Logger;
import vc4.api.stats.Stats;
import vc4.api.tool.Tool;
import vc4.api.util.ColorUtils;
import vc4.api.util.NumberUtils;
import vc4.api.world.World;


/**
 * A stack of Items in the inventory
 * 
 * @author Paul Durbaba
 *
 */
public class ItemStack implements Comparable<ItemStack>, Serializable{


	public static DecimalFormat[] dps = new DecimalFormat[]{
		new DecimalFormat("#"),
		new DecimalFormat("#.#"),
		new DecimalFormat("#.##"),
		new DecimalFormat("#.###"),
		new DecimalFormat("#.####"),
		new DecimalFormat("#.#####")
	};

	/**
	 * 
	 */
	private static final long serialVersionUID = -1925756862880565696L;


	@Override
	public int compareTo(ItemStack o) {
		if(o ==  null)return 0 - getId();
		if(o.getId() != getId()){
			return getId() - o.getId();
		} else {
			return getDamage() - o.getDamage();
		}

	}
	protected short itemId = 0;
	protected short damage = 0;
	protected int amount = 1;

	public ArrayList<ItemEntity> entities = new ArrayList<ItemEntity>();

	public int getId(){
		return itemId & 0xffff;
	}
	public int getMaxStack(){
		return getItem().getMaxStack(getDamage());
	}

	public ItemStack(int id){
		setId(id);

	}
	public ItemStack(){
		setId(0);
	}
//	public ItemStats getStats(){
//		return Item.itemList[getId()].getItemStats(getDamage());
//	}
	public String getName(){
		return getRarityColorCode() + getItem().getLocalizazedItemName(this);
	}
	
	public String getUnformattedName(){
		String s = getItem().getLocalizazedItemName(this);
		if(s == null) s = "null";
		return s;
	}

	public boolean idEquals(ItemStack s){
		if(s == null || !s.checkIsNotEmpty()) return false;
		return getId() == s.getId();
	}

	protected String getRarityColorCode(){
		int r = 0; //TASK quality level
		switch(r){
		case -1: return "{c:7}";
		case 1: return "{c:a}";
		case 2: return "{c:1}";
		case 3: return "{c:4}";
		case 4: return "{c:5}";
		}

		return "{c:f}";
	}
	
	public Stats getStats(){
		return getItem().getStats(this);
	}
	
	public Tool getTool(){
		return getItem().getTool(this);
	}

	public String getTooltipText(){
		String extra = getAmount() > 1 ? " (" + NumberUtils.doDigitGrouping(getAmount()) + ")" : "";
		StringBuilder text = new StringBuilder();
		text.append(getName() + "{c:f}").append(extra);
		if(getItem().isDamagedOnUse()){
			double d = 1D - (getDamage() / (double)getItem().maxDamage);
			Color c = ColorUtils.differColors(Color.red, Color.green, (float)d);
			String s = Integer.toHexString(c.getRGB());
			int len = 6 - s.length();
			for(int dofor = 0; dofor < len; ++dofor){
				s = "0" + s;
			}
			text.append(" ({c:").append(s).append("}").append(getDamage()).append("/").append(getItem().maxDamage).append("{c:f})");
		}
		//text.append(getEquipText());
		text.append(getDescription());
		text.append("\n" + getStatsText());
		text.append(getEnchantmentText());
		return text.toString();
	}

	/*public String getArmourSlotText(int slot){
		String stats;
		if(getStats().getVanity()){
			stats = getStatsText();
		}
		else if(ContainerArmour.isVanitySlot(slot)){
			stats = "\r\n{c:3}" + Localization.getLocalization("item.invanity");
		} else {
			stats = getStatsText();
		}
		StringBuilder text = new StringBuilder();
		text.append(getName() + "{c:f}");
		if(getItem().isDamagedOnUse){
			double d = 1D - (getDamage() / (double)getItem().maxDamage);
			Color c = ColorUtils.differColors(Color.red, Color.green, (float)d);
			String s = Integer.toHexString(c.getRGB());
			int len = 6 - s.length();
			for(int dofor = 0; dofor < len; ++dofor){
				s = "0" + s;
			}
			text.append(" ({c:").append(s).append("}").append(getDamage()).append("/").append(getItem().maxDamage).append("�f)");
		}
		text.append(getDescription());
		text.append(stats);
		text.append(getEnchantmentText());
		return text.toString();
	}*/

//	public String getEquipText(){
//		ArmourType t = getArmourType();
//		if(t == ArmourType.NONE) return "";
//		String name = ContainerArmour.getArmourEquipName(ContainerArmour.getArmourSlot(t, getStats().getVanity()));
//		return "\r\n" + Localization.getLocalization(name);
//	}

	public String getCreativeTooltipText(){
		StringBuilder text = new StringBuilder();
		text.append(getName() + "{c:f}");
		text.append(" (").append(NumberUtils.doDigitGrouping(getId())).append(":");
		text.append(NumberUtils.doDigitGrouping(getDamage())).append(")");
		//text.append(getEquipText());
		text.append(getDescription());
		text.append("\n" + getStatsText());
		text.append(getEnchantmentText());
		return text.toString();
	}

	public String getStatsText(){
		return getStats().getStatsText();
	}
	public ItemStack(int id, int damage){
		setId(id);
		setDamage(damage);
	}
	public ItemStack(int id, short amount){
		setId(id);
		setAmount(amount);
	}
	public ItemStack(int id, int damage, int amount){
		setId(id);
		setDamage(damage);
		setAmount(amount);
	}
	public ItemStack setEntities(ArrayList<ItemEntity> etys){
		this.entities = etys;
		return this;
	}
//	public ItemStats getEnchantmentStats(){
//		ItemEntityEnchantment e = null;
//		for(int dofor = 0; dofor < entities.size(); ++dofor){
//			if(entities.get(dofor) instanceof ItemEntityEnchantment){
//				e = (ItemEntityEnchantment) entities.get(dofor);
//				break;
//			}
//		}
//		if(e == null) return new ItemStats();
//		HashMap<String, Double> stuff = new HashMap<String, Double>();
//		for(int dofor = 0; dofor < e.enchantments.size(); ++dofor){
//			ArrayList<Entry<String, Double>> ns = Item.enchantment.getItemStats(e.enchantments.get(dofor)).getData();
//			for(int pos = 0; pos < ns.size(); ++pos){
//				if(stuff.containsKey(ns.get(pos).getKey())){
//					stuff.put(ns.get(pos).getKey(), stuff.get(ns.get(pos).getKey() + ns.get(pos).getValue()));
//				} else stuff.put(ns.get(pos).getKey(), ns.get(pos).getValue());
//			}
//		}
//		Object[] nst = new Object[stuff.size() * 2];
//		ArrayList<Entry<String, Double>> tAdd = new ArrayList<Entry<String, Double>>(stuff.entrySet());
//		for(int dofor = 0; dofor < tAdd.size(); ++dofor){
//			nst[dofor * 2] = tAdd.get(dofor).getKey();
//			nst[dofor * 2 + 1] = tAdd.get(dofor).getValue();
//		}
//		return new ItemStats(nst);
//	}

//	public ItemStats getItemStatsWithEnchantments(){
//		ItemStats s = getStats().clone();
//		ItemStats ench = getEnchantmentStats();
//		ArrayList<Entry<String, Double>> sta = ench.getData();
//		for(int dofor = 0; dofor < sta.size(); ++dofor){
//			s.addPercentToStat(sta.get(dofor).getKey().substring(12), sta.get(dofor).getValue());
//		}
//		return s;
//	}

	public String getEnchantmentText(){
		ItemEntityEnchantment e = null;
		for(int dofor = 0; dofor < entities.size(); ++dofor){
			if(entities.get(dofor) instanceof ItemEntityEnchantment){
				e = (ItemEntityEnchantment) entities.get(dofor);
				break;
			}
		}
		if(e == null) return "";
		StringBuilder b = new StringBuilder("{c:5}");
		for(int dofor = 0; dofor < e.enchantments.size(); ++dofor){
			b.append("\n");
			//b.append(Item.enchantment.getEnchantmentName(e.enchantments.get(dofor))); TASK
		}
		return b.toString();
	}

	@Override
	public ItemStack clone() {
		ItemStack c = new ItemStack(getId(), getDamage(), getAmount());
		for(int dofor = 0; dofor < entities.size(); ++dofor){
			if(entities.get(dofor) == null) continue;
			c.entities.add(entities.get(dofor).clone());
		}
		return c;
	}
	@Override
	public int hashCode() {
		int i = getId();
		int j = getDamage();
		return (i + (1 << 16)) * j;
	}
	public void setId(int id){
		itemId = (short)id;
	}
//	public ArmourType getArmourType(){
//		return Item.itemList[getId()].getArmourType(getDamage());
//	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ItemStack))
			return false;
		if(obj instanceof CraftingStack) return ((CraftingStack)obj).equals(this);
		ItemStack other = (ItemStack) obj;
		if (damage != other.damage)
			return false;
		if (itemId != other.itemId)
			return false;
		if(entities.size() != other.entities.size()) return false;
		for(int dofor = 0; dofor < entities.size(); ++dofor){
			if(!entities.get(dofor).equals(other.entities.get(dofor))) return false;
		}
		return true;
	}

	public void setIdasItem(int id){
		setId(id + 4096);
	}
	public void setIdasBlock(Block set){
		setId(set.uid);
	}
	public void setIdasBlock(int id){
		setId(id);
	}

	/**
	 * Gets the description of the item, if any
	 * 
	 * @return The localized description of the Item
	 */
	public String getDescription(){
		String result = getItem().getLocalizedItemDescription(this);
		return (result != null) ? "\n{c:8}\"" + result + "\"" : "";
	}
	public int getDamage(){
		return damage & 0xffff;
	}
	public ItemStack setDamage(int damage){
		this.damage = (short) damage;
		return this;
	}
	public void damage(int amount){
		if(!checkIsNotEmpty()) return;
		if(getItem().isDamagedOnUse()) setDamage(getDamage() + amount);
		if(getDamage() > getItem().getMaxDamage() && getItem().isDamagedOnUse()) {
			setAmount(getAmount() - 1);
			setDamage(0);
		}
	}

	public void damage(){
		damage(1);
	}

	public boolean isBlock(){
		return getId() < 2048;
	}
	public boolean isItem(){
		return getId() >= 2048;
	}
	public Item getItem(){
		return Item.byId(getId());
	}
	public int getAmount(){
		return amount;
	}
	public ItemStack setAmount(int amount){
		this.amount = amount;
		return this;
	}
	public boolean incrementAmount(){
		if(getAmount() < getMaxStack()){
			setAmount(getAmount() + 1);
			return true;
		}
		return false;
	}
	public ItemStack combineItemStack(ItemStack combine){
		if(combine == null) return null;
		combine = combine.clone();
		if(!this.equals(combine)){
			ItemStack ret = this.clone();
			setId(combine.getId());
			setDamage(combine.getDamage());
			setAmount(combine.getAmount());
			entities.clear();
			for(int dofor = 0; dofor < combine.entities.size(); ++dofor){
				entities.add(combine.entities.get(dofor));
			}
			return ret;
		}
		if(getAmount() == -1 || combine.getAmount() == -1){
			setAmount(-1);
			return null;
		}
		long amount = getAmount();
		long otherAmount = combine.getAmount();
		long max = getMaxStack();
		if(amount + otherAmount > max){
			setAmount((int)max);
			combine.setAmount((int)((amount + otherAmount) - max));
			return combine;
		} else {
			setAmount((int)(amount + otherAmount));
			return null;
		}
	}
	public void decrementAmount(){
		if(amount > 0) this.amount--;

	}
//	public void onRightClick(World world, EntityPlayer player){
//		if(!checkIsNotEmpty()) return;
//		Item.byId(getId()).onRightClick(world, player, this);
//	}
//	public float onLeftClick(World world, EntityPlayer player){
//		if(!checkIsNotEmpty()) return 0;
//		return Item.itemList[getId()].onLeftClick(world, player, this);
//	}
	public boolean checkIsNotEmpty(){
		return (amount > -2) && getId() != 0 && amount != 0;
	}
	public boolean overrideLeftClick(){
		return getItem().overrideLeftClick();
	}

	public void enchant(short enchantment){
		ItemEntityEnchantment e = null;
		for(int dofor = 0; dofor < entities.size(); ++dofor){
			if(entities.get(dofor) instanceof ItemEntityEnchantment){
				e = (ItemEntityEnchantment) entities.get(dofor);
				break;
			}
		}
		if(e == null){
			e = new ItemEntityEnchantment();
			e.enchantments.add(enchantment);
			entities.add(e);
			return;
		}
		for(int dofor = 0; dofor < e.enchantments.size(); ++dofor){
			short now = e.enchantments.get(dofor);
			int type = now & 0xFFF;
			if(type != (enchantment & 0xFFF)) continue;
			int level = ((now >> 12) & 0xF) + 1;
			int level1 = ((enchantment >> 12) & 0xF) + 1;
			e.enchantments.remove(dofor);
			short add = (short) (type + (Math.max(level, level1) << 12));
			e.enchantments.add(add);
			return;
		}
		e.enchantments.add(enchantment);
	}

	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeShort(itemId);
		out.writeShort(damage);
		out.writeInt(amount);
	}
	public static void write(World world, ItemStack s, CompoundTag tag){
		tag.setShort("id", s == null ? 0 : s.itemId);
		if(s == null || !s.checkIsNotEmpty()) return;
		tag.setShort("damage", s.damage);
		tag.setInt("amount", s.amount);
		if(s.entities.size() > 0){
			ListTag lis = new ListTag("entitys", CompoundTag.class);
			for(int d = 0; d < s.entities.size(); ++d){
				try{
					lis.addTag(s.entities.get(d).getSaveCompound(world));
				} catch(Exception e){
					Logger.getLogger("VC4").info("Failed to save ItemEntity", e);
				}
			}
			tag.addTag(lis);
		}
	}
	public static ItemStack read(World world, CompoundTag in){
		short id = in.getShort("id");
		if(id == 0) return null;
		ItemStack s = new ItemStack(id);
		s.damage = in.getShort("damage");
		s.amount = in.getInt("amount");
		if(in.hasKey("entitys")){
			ListTag lis = in.getListTag("entitys");
			while(lis.hasNext()){
				try{
					s.entities.add(ItemEntity.loadItemEntity(world, (CompoundTag) lis.getNextTag()));
				} catch(Exception e){
					Logger.getLogger("VC4").info("Failed to read ItemEntity", e);
				}
			}
		}
		return s;
	}
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		itemId = in.readShort();
		damage = in.readShort();
		amount = in.readInt();
	}
	public byte getData() {
		return (byte) getDamage();
	}
	public void onRightClick(EntityPlayer player) {
		getItem().onRightClick(player, this);
		
	}
	
	public void onLeftClick(EntityPlayer player) {
		getItem().onLeftClick(player, this);
		
	}
	public int getAttackDamage() {
		if(getStats().hasStat("attackdamage")) return getStats().getStatInt("attackdamage");
		else return 3;
	}



}
