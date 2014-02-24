package vc4.api.entity.spawn;

import java.util.ArrayList;
import java.util.Random;

import vc4.api.entity.EntityPlayer;
import vc4.api.world.World;

public class SpawnControl {

	
	static ArrayList<SpawnEntry> spawnEntrys = new ArrayList<>();
	
	public static void addSpawnEntry(SpawnEntry e){
		spawnEntrys.add(e);
	}
	
	static Random rand = new Random();
	
	static public void updateTick(World world){
		if(world.getPlayers().isEmpty()) return;
		for(EntityPlayer p : world.getPlayers()){
			for(int d = 0; d < 20; ++d) if(spawnAttempt(p)) break;
		}
	}
	
	static public boolean spawnAttempt(EntityPlayer player){
		int xOff = rand.nextInt(96) + 24;
		if(rand.nextBoolean()) xOff *= -1;
		int yOff = rand.nextInt(96) + 24;
		if(rand.nextBoolean()) yOff *= -1;
		int zOff = rand.nextInt(96) + 24;
		if(rand.nextBoolean()) zOff *= -1;
		long x = player.getBlockPos().x + xOff;
		long y = player.getBlockPos().y + yOff;
		long z = player.getBlockPos().z + zOff;
		ArrayList<SpawnEntry> spawnable = new ArrayList<>();
		int weight = 0;
		for(SpawnEntry s : spawnEntrys){
			if(s.canSpawn(player.getWorld(), x, y, z, rand)){
				spawnable.add(s);
				weight += s.getWeight();
			}
		}
		if(spawnable.isEmpty()) return false;
		double dx = x + 0.5d;
		double dy = y + 0.95d;
		double dz = z + 0.5d;
		weight = rand.nextInt(weight);
		for(SpawnEntry s : spawnable){
			weight -= s.getWeight();
			if(weight <= 0){
				s.spawnMob(player.getWorld(), dx, dy, dz, rand);
				break;
			}
		}
		return true;
	}
	
}
