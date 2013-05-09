package vc4.api.block.render;

import vc4.api.block.Block;
import vc4.api.block.IBlockMultitexture;
import vc4.api.graphics.Renderer;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.util.Direction;
import vc4.api.world.Chunk;
import vc4.api.world.World;

public class BlockRendererStairs extends BlockRendererDefault {

	
	
	@Override
	public void renderBlock(Chunk c, int cx, int cy, int cz, Block block, byte data, Renderer[] renderers) {
		long x = c.getChunkPos().worldX(cx);
		long y = c.getChunkPos().worldY(cy);
		long z = c.getChunkPos().worldZ(cz);
		if (block.isAir()) return;
		int facing = data & 3;
		boolean upside = (data & 16) != 0;
		AABB bounds = block.getRenderSize(c.getWorld(), x, y, z);
		IBlockMultitexture mt = null;
		if(block instanceof IBlockMultitexture) mt = (IBlockMultitexture) block;
		if(upside){
			if(block.renderSide(c.getWorld(), x, y, z, 4)){
				renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, 4);
				if(mt != null) renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, 4);
			}
		} else {
			if(block.renderSide(c.getWorld(), x, y, z, 5)){
				renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, 5);
				if(mt != null) renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, 5);
			}
		}
		int opp = Direction.getDirection(facing).opposite().getId();
		if(block.renderSide(c.getWorld(), x, y, z, opp)){
			renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, opp);
			if(mt != null)  renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, opp);
		}
		int left = Direction.getDirection(facing).counterClockwise().getId();
		int right = Direction.getDirection(facing).clockwise().getId();
		boolean renderLeft = block.renderSide(c.getWorld(), x, y, z, left);
		boolean renderRight = block.renderSide(c.getWorld(), x, y, z, right);
		boolean renderForwards = block.renderSide(c.getWorld(), x, y, z, facing);
		
		bounds = AABB.getBoundingBox(0, 1, upside ? 2/3d : 0, upside ? 1 : 1/3d, 0, 1);
		if(renderLeft)renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, left);
		if(renderRight)renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, right);
		if(renderForwards)renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, facing);
		if(renderLeft && mt != null)renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, left);
		if(renderRight && mt != null)renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, right);
		if(renderForwards && mt != null)renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, facing);
		
		bounds = AABB.getBoundingBox(facing == 0 ? 2/3d : 0, facing == 2 ? 1/3d : 1, upside ? 2/3d : 0, upside ? 1 : 1/3d, facing == 1 ? 2/3d : 0, facing == 3 ? 1/3d : 1);
		renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, upside ? 5 : 4);
		if(mt != null)renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, upside ? 5 : 4);
		
		bounds = AABB.getBoundingBox(facing == 2 ? 1/3d : 0, facing == 0 ? 2/3d : 1, 1/3d, 2/3d, facing == 3 ? 1/3d : 0, facing == 1 ? 2/3d : 1);
		if(renderLeft)renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, left);
		if(renderRight)renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, right);
		renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, facing);
		if(renderLeft && mt != null)renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, left);
		if(renderRight && mt != null)renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, right);
		if(mt != null)renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, facing);
		
		bounds = AABB.getBoundingBox(facing % 2 == 0 ? 1/3d : 0, facing % 2 == 0 ? 2/3d : 1, 1/3d, 2/3d, facing % 2 == 1 ? 1/3d : 0, facing % 2 == 1 ? 2/3d : 1);
		renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, upside ? 5 : 4);
		if(mt != null)renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, upside ? 5 : 4);
		
		bounds = AABB.getBoundingBox(facing == 2 ? 2/3d : 0, facing == 0 ? 1/3d : 1, upside ? 0 : 2/3d, upside ? 1/3d : 1, facing == 3 ? 2/3d : 0, facing == 1 ? 1/3d : 1);
		if(renderLeft)renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, left);
		if(renderRight)renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, right);
		renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, facing);
		if(renderLeft && mt != null)renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, left);
		if(renderRight && mt != null)renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, right);
		if(mt != null) renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, facing);
		
		bounds = AABB.getBoundingBox(facing == 2 ? 2/3d : 0, facing == 0 ? 1/3d : 1, upside ? 0 : 2/3d, upside ? 1/3d : 1, facing == 3 ? 2/3d : 0, facing == 1 ? 1/3d : 1);
		renderBlockFace(c.getWorld(), x, y, z, block, renderers[1], bounds, upside ? 5 : 4);
		if(mt != null)renderBlockFace(c.getWorld(), x, y, z, mt, renderers[1], bounds, upside ? 5 : 4);
	}
	
	@Override
	public void renderBlockCracks(World world, long x, long y, long z, Renderer render, double amount){
		Block block = world.getBlockType(x, y, z);
		byte data = world.getBlockData(x, y, z);
		int facing = data & 3;
		boolean upside = (data & 16) != 0;
		AABB bounds = block.getRenderSize(world, x, y, z);
		if(upside){
			renderBlockFaceCracks(world, x, y, z, block, render, bounds, 4, amount);
		} else {
			renderBlockFaceCracks(world, x, y, z, block, render, bounds, 5, amount);
		}
		int opp = Direction.getDirection(facing).opposite().getId();
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, opp, amount);
		int left = Direction.getDirection(facing).counterClockwise().getId();
		int right = Direction.getDirection(facing).clockwise().getId();
		
		bounds = AABB.getBoundingBox(0, 1, upside ? 2/3d : 0, upside ? 1 : 1/3d, 0, 1);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, left, amount);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, right, amount);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, facing, amount);
		
		bounds = AABB.getBoundingBox(facing == 0 ? 2/3d : 0, facing == 2 ? 1/3d : 1, upside ? 2/3d : 0, upside ? 1 : 1/3d, facing == 1 ? 2/3d : 0, facing == 3 ? 1/3d : 1);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, upside ? 5 : 4, amount);
		
		bounds = AABB.getBoundingBox(facing == 2 ? 1/3d : 0, facing == 0 ? 2/3d : 1, 1/3d, 2/3d, facing == 3 ? 1/3d : 0, facing == 1 ? 2/3d : 1);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, left, amount);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, right, amount);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, facing, amount);
		
		bounds = AABB.getBoundingBox(facing % 2 == 0 ? 1/3d : 0, facing % 2 == 0 ? 2/3d : 1, 1/3d, 2/3d, facing % 2 == 1 ? 1/3d : 0, facing % 2 == 1 ? 2/3d : 1);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, upside ? 5 : 4, amount);
		
		bounds = AABB.getBoundingBox(facing == 2 ? 2/3d : 0, facing == 0 ? 1/3d : 1, upside ? 0 : 2/3d, upside ? 1/3d : 1, facing == 3 ? 2/3d : 0, facing == 1 ? 1/3d : 1);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, left, amount);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, right, amount);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, facing, amount);
		
		bounds = AABB.getBoundingBox(facing == 2 ? 2/3d : 0, facing == 0 ? 1/3d : 1, upside ? 0 : 2/3d, upside ? 1/3d : 1, facing == 3 ? 2/3d : 0, facing == 1 ? 1/3d : 1);
		renderBlockFaceCracks(world, x, y, z, block, render, bounds, upside ? 5 : 4, amount);
	}

	

	@Override
	public void renderBlock(ItemStack block, float x, float y, float z, Renderer render) {
		// TASK Auto-generated method stub

	}

}
