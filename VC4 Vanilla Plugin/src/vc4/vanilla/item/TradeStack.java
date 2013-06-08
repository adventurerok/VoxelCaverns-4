package vc4.vanilla.item;

import vc4.api.item.ItemStack;

public class TradeStack{

	int playerStock; //Items sold to vendor by players don't count against default
	int defaultStock; //Max usual stock
	int currentStock; //Current non-player item stock
	int restoreRate; //How fast stock restores
	int cost; //Cost in
	ItemStack item; //The item being sold
	
	public TradeStack(ItemStack item, int stock, int restore, int cost) {
		super();
		this.item = item;
		this.defaultStock = stock;
		this.restoreRate = restore;
		this.cost = cost;
	}
	
	public void updateTick(long time){
		if(currentStock == defaultStock) return;
		if(restoreRate < 0) currentStock -= restoreRate;
		else if(restoreRate > 0 && time % restoreRate == 0) currentStock += 1;
		
		if(currentStock > defaultStock) currentStock = defaultStock;
	}
	
	public int getPlayerStock() {
		return playerStock;
	}
	
	public int getDefaultStock() {
		return defaultStock;
	}
	
	public int getCurrentStock() {
		return currentStock;
	}
	
	public int getRestoreRate() {
		return restoreRate;
	}
	
	
	
}
