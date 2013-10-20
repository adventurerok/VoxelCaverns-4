package vc4.vanilla;

import vc4.api.Resources;
import vc4.api.graphics.texture.AnimatedTexture;

public class ItemTexture {
	
	public static int toolHandle, pickaxeHead, axeHead, hoeHead, shovelHead;
	public static int cheese, bread, harvestFront, harvestBack, stick, metalBar, seeds;

	public static void update(){
		AnimatedTexture items = Resources.getAnimatedTexture("items");
		toolHandle = items.getArrayIndex("toolhandle");
		pickaxeHead = items.getArrayIndex("pickaxehead");
		axeHead = items.getArrayIndex("axehead");
		hoeHead = items.getArrayIndex("hoehead");
		shovelHead = items.getArrayIndex("shovelhead");
		cheese = items.getArrayIndex("cheese");
		bread = items.getArrayIndex("bread");
		harvestFront = items.getArrayIndex("harvestfront");
		harvestBack = items.getArrayIndex("harvestback");
		stick = items.getArrayIndex("stick");
		metalBar = items.getArrayIndex("metalbar");
		seeds = items.getArrayIndex("seeds");
	}
}
