package vc4.impl.world;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import vc4.api.vector.Vector2l;
import vc4.api.vector.Vector3d;
import vc4.api.world.ChunkPos;
import vc4.api.world.ComparatorClosestChunkPos;

public class GeneratorThread extends Thread {
	
	public static HashMap<ChunkPos, ImplChunk> chunks;
	public static HashMap<Vector2l, ImplMapData> heights;
	public static ImplWorld world;
	public static boolean stop;
	public static Vector3d location;
	public ConcurrentLinkedQueue<ImplChunk> generated = new ConcurrentLinkedQueue<>();
	public ConcurrentLinkedQueue<ImplMapData> newData = new ConcurrentLinkedQueue<>();
	public int num;
	public static int maxNum;
	
	public GeneratorThread(int num) {
		this.num = num;
		setDaemon(true);
	}

	@Override
	public void run() {
		while(!stop){
			long start = System.nanoTime();
			if(world.loaded) generate(location);
			long time = System.nanoTime() - start;
			time /= 1000000;
			if(time > 30) time = 30;
			try {
				Thread.sleep(35 - time);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void generate(Vector3d loc){
		ArrayList<ChunkPos> closestToLoad = new ArrayList<>();
		ChunkPos me = ChunkPos.createFromWorldVector(loc);
		for (int x = -7; x < 8; ++x) {
			for (int y = -7; y < 8; ++y) {
				for (int z = -7; z < 8; ++z) {
					
					checkLoad(loc, me, x, y, z, closestToLoad);
				}
			}
		}
		Collections.sort(closestToLoad, new ComparatorClosestChunkPos(loc));
		for (int d = 0; d < 2; ++d) {
			if (closestToLoad.size() <= d) break;
			generateChunk(closestToLoad.get(d));
		}
	}
	
	public ImplChunk generateChunk(ChunkPos pos) {
		ImplChunk chunk = new ImplChunk(world, pos);
		chunk.setData(world.getGenerator().generate(world, pos.x, pos.y, pos.z, getOrGenerate(new Vector2l(pos.x, pos.z))));
		generated.add(chunk);
		return chunk;
	}
	
	public ImplMapData getOrGenerate(Vector2l pos){
		ImplMapData data = heights.get(pos);
		if(data == null){
			data = new ImplMapData(pos);
			world.getGenerator().generateMapData(world, data);
			heights.put(pos, data);
			newData.add(data);
		}
		return data;
	}
	
	private void checkLoad(Vector3d loc, ChunkPos start, int x, int y, int z, ArrayList<ChunkPos> toLoad) {
		start = start.add(x, y, z);
		if((start.z & maxNum) != num) return;
		if (toLoad.contains(start)) return;
		if (chunks.get(start) != null) return;
		if(start.distanceSquared(loc) > 60000) return;
		toLoad.add(start);
	}
}
