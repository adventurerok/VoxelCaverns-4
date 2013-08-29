package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.block.render.BlockRendererFace;
import vc4.api.util.AABB;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;

public class BlockAlgae extends Block {
	
	Color col = new Color(255, 255, 255, 192);

	public BlockAlgae(int uid) {
		super(uid, BlockTexture.algae, "algae");
		renderer = new BlockRendererFace();
		blockOpacity[uid] = 2;
	}
	
	@Override
	public boolean isSolid(World world, long x, long y, long z, int side) {
		return false;
	}
	
	@Override
	public boolean render3d(byte data) {
		return false;
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return col;
	}
	
	@Override
	public int getRendererToUse(byte data, int side) {
		return 1;
	}
	
	@Override
	public boolean renderSide(World world, long x, long y, long z, int side) {
		return side == 5;
	}
	
	@Override
	public AABB[] getCollisionBounds(World world, long x, long y, long z) {
		return new AABB[0];
	}
	
	@Override
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		return AABB.getBoundingBox(0, 1, 0, 0.05, 0, 1);
	}
	
	@Override
	public boolean replacableBy(World world, long x, long y, long z, int bid, byte data) {
		return true;
	}
	

}
