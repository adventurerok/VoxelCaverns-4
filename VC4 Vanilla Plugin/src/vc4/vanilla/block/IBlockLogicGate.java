package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.block.IBlockJoinable;
import vc4.api.item.ItemStack;
import vc4.api.util.AABB;
import vc4.api.world.World;

public interface IBlockLogicGate extends IBlockJoinable {

	public AABB getGateBounds(World world, long x, long y, long z);

	public int getTextureIndexGate(World world, long x, long y, long z, int side);

	public int getTextureIndexGate(ItemStack item, int side);

	public Color getColorGate(World world, long x, long y, long z, int side);

	public Color getColorGate(ItemStack item, int side);

}
