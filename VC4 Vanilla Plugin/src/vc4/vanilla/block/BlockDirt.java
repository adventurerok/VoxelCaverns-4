package vc4.vanilla.block;

import vc4.api.block.*;

public class BlockDirt extends Block{

	public BlockDirt(int uid, int texture, Material m) {
		super(uid, texture, m);
		// TASK Auto-generated constructor stub
	}

	public BlockDirt(int uid, int texture, String material) {
		super(uid, texture, material);
		// TASK Auto-generated constructor stub
	}

	public BlockDirt(short uid, int texture, Material m) {
		super(uid, texture, m);
		// TASK Auto-generated constructor stub
	}
	
	@Override
	public boolean canGrowPlant(Plant plant) {
		return true;
	}

}
