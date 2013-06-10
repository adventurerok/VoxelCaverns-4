package vc4.vanilla.block;

import vc4.api.block.Block;
import vc4.api.tool.MiningData;
import vc4.api.tool.ToolType;
import vc4.vanilla.BlockTexture;

public class BlockGravel extends Block {

	public BlockGravel(int uid) {
		super(uid, BlockTexture.gravel, "gravel");
		mineData = new MiningData().setRequired(ToolType.spade).setPowers(0, 1, 20).setTimes(0.45, 0.01, 0.22);
	}

}
