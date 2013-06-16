package vc4.vanilla.generation.dungeon.style;

import java.util.ArrayList;

import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.generation.dungeon.WeightedRoom;
import vc4.vanilla.generation.dungeon.loot.LootChest;
import vc4.vanilla.generation.dungeon.loot.LootItem;

public class DungeonStyle {

	protected int brickId, mossId, type;
	private ArrayList<WeightedRoom> rooms = new ArrayList<>();
	private double roomFailChance = 0.25;
	private int maxRooms = 125;
	
	protected LootChest loot = new LootChest();
	
	public void setRoomFailChance(double roomFailChance) {
		this.roomFailChance = roomFailChance;
	}
	
	public double getRoomFailChance() {
		return roomFailChance;
	}
	
	public boolean canGenerate(World world, long x, long y, long z){
		return y < -32;
	}
	
	public int getBrickId() {
		return brickId;
	}
	
	public int getMaxRooms() {
		return maxRooms;
	}
	
	public void setMaxRooms(int maxRooms) {
		this.maxRooms = maxRooms;
	}

	public int getMossId() {
		return mossId;
	}

	public int getType() {
		return type;
	}
	
	public void addRoom(WeightedRoom room){
		rooms.add(room);
	}
	
	public ArrayList<WeightedRoom> getRooms() {
		return rooms;
	}

	public DungeonStyle() {
		type = 4;
	}
	
	public void setBrickId(int brickId) {
		this.brickId = brickId;
	}
	
	public void setMossId(int mossId) {
		this.mossId = mossId;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public LootChest getLootChest(){
		return loot;
	}
	
	public void onWorldLoad(World world){
		setBrickId(Vanilla.brick.uid);
		setMossId(Vanilla.mossBrick.uid);
		loot.clear();
		loot.addLoot(new LootItem(new ItemStack(Vanilla.food.id, 0), 1, 3, 6));
		loot.addLoot(new LootItem(new ItemStack(world.getRegisteredItem("vanilla.pick.copper"), 0), 1, 1, 2));
		loot.addLoot(new LootItem(new ItemStack(world.getRegisteredItem("vanilla.axe.copper"), 0), 1, 1, 2));
		loot.addLoot(new LootItem(new ItemStack(world.getRegisteredItem("vanilla.shovel.copper"), 0), 1, 1, 2));
		loot.addLoot(new LootItem(new ItemStack(world.getRegisteredItem("vanilla.hoe.copper"), 0), 1, 1, 2));
		loot.addLoot(new LootItem(new ItemStack(world.getRegisteredItem("vanilla.pick.bronze"), 0), 1, 1, 1));
		loot.addLoot(new LootItem(new ItemStack(world.getRegisteredItem("vanilla.axe.bronze"), 0), 1, 1, 1));
		loot.addLoot(new LootItem(new ItemStack(world.getRegisteredItem("vanilla.shovel.bronze"), 0), 1, 1, 1));
		loot.addLoot(new LootItem(new ItemStack(world.getRegisteredItem("vanilla.hoe.bronze"), 0), 1, 1, 1));
	}
	
	public int getWeight(){
		return 100;
	}
}
