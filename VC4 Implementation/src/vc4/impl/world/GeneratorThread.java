package vc4.impl.world;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import vc4.api.logging.Logger;
import vc4.api.profile.Profiler;
import vc4.api.vector.Vector2l;
import vc4.api.vector.Vector3d;
import vc4.api.world.*;

public class GeneratorThread extends Thread {

	public static HashMap<ChunkPos, ImplChunk> chunks;
	public static HashMap<Vector2l, ImplMapData> heights;
	public static ImplWorld world;
	public static boolean stop;
	public static boolean generate = true;
	public static Vector3d[] locations;
	public ConcurrentLinkedQueue<ImplChunk> generated = new ConcurrentLinkedQueue<>();
	public ConcurrentLinkedQueue<ImplMapData> newData = new ConcurrentLinkedQueue<>();
	public ConcurrentLinkedQueue<ImplChunk> toSave = new ConcurrentLinkedQueue<>();
	public int num;
	public static int maxNum;

	public GeneratorThread(int num) {
		this.num = num;
		setDaemon(true);
	}

	@Override
	public void run() {
		long start, time;
		while (!stop) {
			Profiler.clear();
			start = System.nanoTime();
			if (world.loaded) {
				save();
				if (generate) generate(locations);
			}
			time = System.nanoTime() - start;
			time /= 1000000;
			if (time > 30) time = 30;
			try {
				Thread.sleep(35 - time);
			} catch (InterruptedException e) {
			}
		}
	}

	public void save() {
		Profiler.start("save");
		ImplChunk c;
		while ((c = toSave.poll()) != null) {
			try {
				Profiler.start("savechunk");
				c.setModified(false);
				world.getSaveFormat().writeChunk(c);
				if (c.isUnloading()) c.removeData();
				Profiler.stop();
			} catch (IOException e) {
				Logger.getLogger(GeneratorThread.class).warning("Exception occured", e);
			}
		}
		Profiler.stop();
	}

	public void generate(Vector3d... loc) {
		if (loc == null || loc.length == 0) return;
		Profiler.start("load");
		Profiler.start("find");
		ArrayList<ChunkPos> closestToLoad = new ArrayList<>();
		int li = 0;
		for (li = 0; li < loc.length; ++li) {
			ChunkPos me = ChunkPos.createFromWorldVector(loc[li]);
			int y, z;
			for (int x = -ImplWorld.CHUNK_RANGE; x <= ImplWorld.CHUNK_RANGE; ++x) {
				for (y = -ImplWorld.CHUNK_RANGE; y <= ImplWorld.CHUNK_RANGE; ++y) {
					for (z = -ImplWorld.CHUNK_RANGE; z <= ImplWorld.CHUNK_RANGE; ++z) {
						checkLoad(loc[li], me, x, y, z, closestToLoad);
					}
				}
			}
		}
		Profiler.stop();
		Collections.sort(closestToLoad, new ComparatorClosestChunkPos(loc));
		for (int d = 0; d < 2; ++d) {
			if (closestToLoad.size() <= d) break;
			loadOrGenerate(closestToLoad.get(d));
		}
		Profiler.stop();
	}

	public void loadOrGenerate(ChunkPos pos) {
		ImplChunk chunk = null;
		Profiler.start("read");
		getOrGenerate(new Vector2l(pos.x, pos.z));
		try {
			chunk = (ImplChunk) world.getSaveFormat().readChunk(world, pos.x, pos.y, pos.z);
		} catch (IOException e) {
		}
		Profiler.stop();
		if (chunk != null) {
			chunks.put(chunk.pos, chunk);
			generated.add(chunk);
		} else generateChunk(pos);
	}

	public Chunk generateChunk(ChunkPos pos) {
		Profiler.start("generate");
		ImplChunk chunk = new ImplChunk(world, pos);
		chunk.setData(world.getGenerator().generate(world, pos.x, pos.y, pos.z, getOrGenerate(new Vector2l(pos.x, pos.z))));
		chunks.put(chunk.pos, chunk);
		generated.add(chunk);
		try {
			world.getSaveFormat().writeChunk(chunk);
		} catch (IOException e) {
			Logger.getLogger(GeneratorThread.class).warning("Failed to save new chunk", e);
		}
		Profiler.stop();
		return chunk;
	}

	public MapData getOrGenerate(Vector2l pos) {
		Profiler.start("mapdata");
		ImplMapData data = heights.get(pos);
		if (data == null) {
			try {
				data = (ImplMapData) world.getSaveFormat().readMap(world, pos.x, pos.y);
			} catch (IOException e) {
				Logger.getLogger(GeneratorThread.class).warning("Error while loading map data", e);
			}
			if (data == null) {
				data = new ImplMapData(pos);
				world.getGenerator().generateMapData(world, data);
				try {
					world.getSaveFormat().writeMap(world, data);
				} catch (IOException e) {
					Logger.getLogger(GeneratorThread.class).warning("Error while saving map data", e);
				}
			}
			heights.put(pos, data);
			newData.add(data);
		}
		Profiler.stop();
		return data;
	}

	private void checkLoad(Vector3d loc, ChunkPos start, int x, int y, int z, ArrayList<ChunkPos> toLoad) {
		start = start.add(x, y, z);
		if ((start.z & maxNum) != num) return;
		if (toLoad.contains(start)) return;
		if (chunks.get(start) != null) return;
		if (start.distanceSquared(loc) > ImplWorld.LOAD_LIMIT_SQUARED) return;
		toLoad.add(start);
	}
}
