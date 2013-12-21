package vc4.api.util;

import java.util.*;

import vc4.api.block.IBlockJoinable;
import vc4.api.world.World;

public class PipeUtils {

	public static Random random = new Random();
	
	public static boolean[] getPipeAttachedSides(World world, long x, long y, long z){
		try {
			boolean[] result = new boolean[6];
			IBlockJoinable j = (IBlockJoinable)world.getBlockType(x, y, z);
			for(int dofor = 0; dofor < 6; ++dofor){
				result[dofor] = j.joinTo(world, x, y, z, dofor);
			}
			return result;
		} catch (Exception e) {
			return new boolean[]{false, false, false, false, false, false};
		}
	}
	
	public static List<Integer> getAvaliableSides(boolean[] sides, int from){
		from = Direction.getOpposite(from).id();
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int dofor = 0; dofor < 6; ++dofor){
			if(dofor == from) continue;
			if(sides[dofor]) result.add(dofor);
		}
		return result;
	}
	
	public static int pickRandomSide(boolean[] sides, int from){
		List<Integer> res = getAvaliableSides(sides, from);
		if(res.size() < 1) return -1;
		return res.get(random.nextInt(res.size()));
	}
	
	public static boolean pipeClosed(World world, long x, long y, long z){
		return BooleanUtils.areAllFalse(getPipeAttachedSides(world, x, y, z));
	}
	public static boolean hasOpenEnd(boolean[] sides){
		return getOpenEnd(sides) != -1;
	}
	public static int getOpenEnd(boolean[] sides){
		int onlySide = -1;
		for(int dofor = 0; dofor < 6; ++dofor){
			if(sides[dofor]){
				if(onlySide == -1) onlySide = dofor;
				else{
					onlySide = -1;
					break;
				}
			}
		}
		return onlySide;
	}
}
