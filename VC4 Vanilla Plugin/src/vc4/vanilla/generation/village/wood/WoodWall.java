package vc4.vanilla.generation.village.wood;

import vc4.api.math.MathUtils;
import vc4.api.world.World;
import vc4.vanilla.generation.village.Village;
import vc4.vanilla.generation.village.Wall;

public class WoodWall implements Wall {

	@Override
	public void generate(World world, long x, long z, Village ville) {
		long sx = x - 45;
		long ex = x - 42;
		long sz = z - 42;
		long ez = z + 42;
		long heights[] = new long[89];
		for(int d = 0; d < 2; ++d){
			if(d == 1){
				sx = x + 42;
				ex = x + 45;
			}
			for(long pz = sz - 2; pz <= ez + 2; ++pz){
				long y = 0;
				for(long px = sx; px <= ex; ++px){
					y += world.getMapData(px >> 5, pz >> 5).getGenHeight((int) (px & 31), (int)(pz & 31));
				}
				y = MathUtils.ceil(y /4f);
				heights[(int) (pz - sz + 2)] = y;
			}
			for(long pz = sz; pz <= ez; ++pz){
				int ind = (int) (pz - sz + 2);
				long y = MathUtils.floor((heights[ind - 2] + heights[ind - 1] + heights[ind] + heights[ind + 1] + heights[ind + 2]) / 5);
				if(y < 0) continue;
				for(long px = sx; px <= ex; ++px){
					boolean par = px == sx || px == ex;
					for(long py = y - 2; py <= y + 5; ++ py){
						if(!par && py != y + 4 && py > y){
							if(py < y + 4 && (pz == z - 1 || pz == z + 2)) ville.setPlankBlock(px, py, pz);
							else ville.setEmptyBlock(px, py, pz);
						}
						else if(par && (py == y + 4 || py == y)) ville.setLogBlock(px, py, pz);
						else if(par && py > y && py < y + 4 && (pz == z || pz == z + 1)) ville.setEmptyBlock(px, py, pz);
						else ville.setPlankBlock(px, py, pz);
					}
				}
			}
		}
		sx = x - 42;
		ex = x + 42;
		sz = z - 45;
		ez = z - 42;
		for(int d = 0; d < 2; ++d){
			if(d == 1){
				sz = z + 42;
				ez = z + 45;
			}
			for(long px = sx - 2; px <= ex + 2; ++px){
				long y = 0;
				for(long pz = sz; pz <= ez; ++pz){
					y += world.getMapData(px >> 5, pz >> 5).getGenHeight((int) (px & 31), (int)(pz & 31));
				}
				y = MathUtils.ceil(y /4f);
				heights[(int) (px - sx + 2)] = y;
			}
			for(long px = sx; px <= ex; ++px){
				int ind = (int) (px - sx + 2);
				long y = MathUtils.floor((heights[ind - 2] + heights[ind - 1] + heights[ind] + heights[ind + 1] + heights[ind + 2]) / 5);
				if(y < 0) continue;
				for(long pz = sz; pz <= ez; ++pz){
					boolean par = pz == sz || pz == ez;
					for(long py = y - 2; py <= y + 5; ++ py){
						if(!par && py != y + 4 && py > y){
							if(py < y + 4 && (px == x - 1 || px == x + 2)) ville.setPlankBlock(px, py, pz);
							else ville.setEmptyBlock(px, py, pz);
						}
						else if(par && (py == y + 4 || py == y)) ville.setLogBlock(px, py, pz);
						else if(par && py > y && py < y + 4 && (px == x || px == x + 1)) ville.setEmptyBlock(px, py, pz);
						else ville.setPlankBlock(px, py, pz);
					}
				}
			}
		}
		sx = x - 45;
		ex = x - 42;
		sz = z - 45;
		ez = z - 42;
		for(int d = 0; d < 4; ++d){
			if (d == 1) {
				sx = x + 42;
				ex = x + 45;
				sz = z - 45;
				ez = z - 42;
			} else if (d == 2) {
				sx = x + 42;
				ex = x + 45;
				sz = z + 42;
				ez = z + 45;
			} else if (d == 3) {
				sx = x - 45;
				ex = x - 42;
				sz = z + 42;
				ez = z + 45;
			}
			long y = world.getMapData((sx + 2) >> 5, (sz + 2) >> 5).getGenHeight((int) ((sx + 2) & 31), (int) ((sz + 2) & 31));
			for (long px = sx; px <= ex; ++px) {
				boolean xSide = px == sx || px == ex;
				for (long pz = sz; pz <= ez; ++pz) {
					boolean zSide = pz == sz || pz == ez;
					for (long py = y - 2; py <= y + 9; ++py) {
						if (xSide && zSide) ville.setLogBlock(px, py, pz);
						else if (xSide || zSide) {
							if (py == y || py == y + 4 || py == y + 8) ville.setLogBlock(px, py, pz);
							else ville.setPlankBlock(px, py, pz);
						} else if (py == y || py == y + 4 || py == y + 8) ville.setPlankBlock(px, py, pz);
						else ville.setEmptyBlock(px, py, pz);
					}
				}
			}
		}
		

	}

}
