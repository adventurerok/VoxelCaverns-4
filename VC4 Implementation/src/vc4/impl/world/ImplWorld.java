/**
 * 
 */
package vc4.impl.world;

import java.io.*;
import java.util.*;

import org.jnbt.*;

import vc4.api.Resources;
import vc4.api.biome.Biome;
import vc4.api.block.Block;
import vc4.api.client.Client;
import vc4.api.crafting.CraftingManager;
import vc4.api.entity.*;
import vc4.api.generator.*;
import vc4.api.graphics.*;
import vc4.api.io.SaveFormat;
import vc4.api.io.SaveFormats;
import vc4.api.item.Item;
import vc4.api.logging.Logger;
import vc4.api.math.MathUtils;
import vc4.api.profile.Profiler;
import vc4.api.sound.Music;
import vc4.api.tileentity.TileEntity;
import vc4.api.util.*;
import vc4.api.vector.*;
import vc4.api.world.*;
import vc4.api.io.Dictionary;
import vc4.impl.plugin.PluginLoader;

/**
 * @author paul
 * 
 */
public class ImplWorld implements World {

	Biome fakeBiome = Biome.createFake();

	private static class BlockStoreDist implements Comparable<BlockStoreDist> {
		BlockStore s;
		ImplChunk c;
		double dist;

		public BlockStoreDist(BlockStore s, ImplChunk c, double dist) {
			super();
			this.s = s;
			this.c = c;
			this.dist = dist;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(BlockStoreDist o) {
			if (dist > o.dist) return 1;
			else if (dist < o.dist) return -1;
			else return 0;
		}
	}

	private static BlockStore fake = new BlockStore(0, 0, 0);
	public HashMap<ChunkPos, ImplChunk> chunks = new HashMap<>();
	public HashMap<Vector2l, ImplMapData> heights = new HashMap<>();
	private Dictionary registeredBlocks = new Dictionary(2).put("air", 0).put("stone", 1);
	private Dictionary registeredItems = new Dictionary(2048);
	private Dictionary registeredCrafting = new Dictionary(1);
	private Dictionary registeredEntitys = new Dictionary(10).put("item", 0).put("player", 5);
	private Dictionary registeredBiomes = new Dictionary(0);
	private Dictionary registeredTileEntitys = new Dictionary(5);
	private Dictionary registeredItemEntitys = new Dictionary(5).put("enchantment", 0);
	private Dictionary registeredContainers = new Dictionary(5);
	private Dictionary registeredTraits = new Dictionary(25).put("inventory", 0);
	private Dictionary registeredAreas = new Dictionary(10);
	private long seed = new Random().nextLong();
	private long time;
	private String generatorName = "overworld";
	private String name = "World";
	private String saveName;
	private CompoundTag generatorTag = new CompoundTag("gen");
	protected boolean loaded = false;
	private LinkedList<Vector4l> blockUpdates = new LinkedList<>();
	private int visibleChunks = 0;
	private String saveFormatName = "VCH4";
	private SaveFormat saveFormat = SaveFormats.getSaveFormat(saveFormatName);

	private double tickTime;
	private List<EntityPlayer> players = new ArrayList<>();

	private GeneratorThread[] genThreads;

	private static double WORLD_SECOND = 50;

	/**
	 * 
	 */
	public ImplWorld(String saveName) {
		this.saveName = saveName;
	}
	
	public void loadWorld(){
		try {
			loadInfo();
		} catch (IOException e) {
			Logger.getLogger(ImplWorld.class).warning("Failed to load world details", e);
		}
		onLoad();
		startGenThreads(4);
	}

	public void loadInfo() throws IOException {
		String dirPath = DirectoryLocator.getPath() + "/worlds/" + saveName + "/";
		File wld = new File(dirPath + "world.vbt"); // VBT format, not bnbt (Binary NBT VC3)
		if (!wld.exists()) return;
		loadDict(registeredBiomes, dirPath + "biomes.dictionary");
		loadDict(registeredBlocks, dirPath + "blocks.dictionary");
		loadDict(registeredItems, dirPath + "items.dictionary");
		loadDict(registeredEntitys, dirPath + "entitys.dictionary");
		loadDict(registeredCrafting, dirPath + "crafting.dictionary");
		loadDict(registeredContainers, dirPath + "containers.dictionary");
		loadDict(registeredTileEntitys, dirPath + "tileentitys.dictionary");
		loadDict(registeredItemEntitys, dirPath + "itementitys.dictionary");
		loadDict(registeredTraits, dirPath + "traits.dictionary");
		loadDict(registeredAreas, dirPath + "areas.dictionary");
		try (NBTInputStream in = new NBTInputStream(new FileInputStream(wld), true)) {
			loadSaveCompound((CompoundTag) in.readTag());
		}
	}
	
	private void loadDict(Dictionary dict, String path) throws FileNotFoundException{
		File file = new File(path);
		if(!file.exists()) return;
		dict.load(new FileInputStream(file));
	}
	
	public SaveFormat getSaveFormat() {
		return saveFormat;
	}
	

	@SuppressWarnings("unchecked")
	private void startGenThreads(int num) {
		GeneratorThread.world = this;
		GeneratorThread.chunks = (HashMap<ChunkPos, ImplChunk>) chunks.clone();
		GeneratorThread.heights = (HashMap<Vector2l, ImplMapData>) heights.clone();
		GeneratorThread.location = new Vector3d(0, 0, 0);
		GeneratorThread.maxNum = num - 1;
		genThreads = new GeneratorThread[num];
		for (int d = 0; d < num; ++d) {
			genThreads[d] = new GeneratorThread(d);
			genThreads[d].start();
		}
	}
	
	@Override
	public void setDirty(long x, long y, long z){
		Chunk c = getChunk(ChunkPos.createFromWorldPos(x, y, z));
		if (c != null) {
			c.setDirty((int) (x & 31), (int) (y & 31), (int) (z & 31));
		}
	}

	// private void checkLoad(ChunkPos start, int x, int y, int z, ArrayList<ChunkPos> toLoad) {
	// start = start.add(x, y, z);
	// if (toLoad.contains(start)) return;
	// if (chunks.get(start) != null) return;
	// toLoad.add(start);
	// }

	@Override
	public boolean chunkExists(long x, long y, long z) {
		return getChunk(x, y, z) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#createRandom(int, int, int)
	 */
	@Override
	public Random createRandom(long in1, long in2, long in3) {
		long seed = in1 * 341873128712L + in2 * 132897987541L + getSeed() + in3;
		return new Random(seed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#createRandom(int, int, int)
	 */
	@Override
	public Random createRandom(long in1, long in2, long in3, long in4) {
		long seed = in1 * 725659903004L + in2 * 341873128712L + in3 * 132897987541L + getSeed() + in4;
		return new Random(seed);
	}

	public void onLoad() {
		Item.clearItems();
		Block.clearBlocks();
		Biome.clear();
		CraftingManager.emptyRecipes();
		PluginLoader.onWorldLoad(this);
		GeneratorList.onWorldLoad(this);
		loaded = true;
	}

	public void addPlayer(EntityPlayer player) {
		players.add(player);
	}

	public void removePlayer(EntityPlayer player) {
		players.remove(player);
	}

	public void draw(Vector3d pos) {
		Graphics.getClientShaderManager().bindShader("world");
		OpenGL gl = Graphics.getOpenGL();
		gl.enable(GLFlag.CULL_FACE);
		gl.cullFace(GLFace.BACK);
		gl.enable(GLFlag.DEPTH_TEST);
		gl.enable(GLFlag.ALPHA_TEST);
		gl.alphaFunc(GLCompareFunc.GREATER, .3f);
		gl.depthFunc(GLCompareFunc.LEQUAL);
		gl.bindTexture(GLTexture.TEX_2D_ARRAY, Resources.getAnimatedTexture("blocks").getTexture());
		gl.enableVertexArrribArray(0);
		gl.enableVertexArrribArray(2);
		gl.enableVertexArrribArray(3);
		gl.enableVertexArrribArray(8);
		ArrayList<BlockStoreDist> toBuild = new ArrayList<>();
		ArrayList<BlockStoreDist> toCompile = new ArrayList<>();
		ArrayList<BlockStoreDist> render = new ArrayList<>();
		for (ImplChunk c : chunks.values()) {
			if (!c.isPopulated()) continue;
			for (BlockStore b : c.stores) {
				if (b.compileState < 1) {
					toBuild.add(new BlockStoreDist(b, c, b.distance(pos, c)));
				} else if (b.compileState == 2) {
					toCompile.add(new BlockStoreDist(b, c, b.distance(pos, c)));
				} else if (b.compileState == 4) {
					render.add(new BlockStoreDist(b, c, b.distance(pos, c)));
				}
			}
		}
		Collections.sort(toBuild);
		for (int d = 0; d < 5; ++d) {
			if (toBuild.size() <= d) continue;
			BlockStoreDist ds = toBuild.get(d);
			ds.s.calculateData(ds.c);
			toCompile.add(ds);
		}
		Collections.sort(toCompile);
		for (int d = 0; d < 5; ++d) {
			if (toCompile.size() <= d) continue;
			BlockStoreDist ds = toCompile.get(d);
			ds.s.compileRenderData();
			render.add(ds);
		}
		ArrayList<BlockStoreDist> tRender = new ArrayList<>();
		Fustrum fust = Client.getGame().getViewFustrum();
		visibleChunks = 0;
		for (BlockStoreDist b : render) {
			AABB cube = AABB.getBoundingBox(b.c.pos.worldX(b.s.xMod) - 2, b.c.pos.worldX(b.s.xMod) + 18, b.c.pos.worldY(b.s.yMod) - 2, b.c.pos.worldY(b.s.yMod) + 18, b.c.pos.worldZ(b.s.zMod) - 2, b.c.pos.worldZ(b.s.zMod) + 18);
			if (fust.boxInFrustum(cube) != Fustrum.OUTSIDE) {
				tRender.add(b);
				++visibleChunks;
			}
		}
		render = tRender;
		Collections.sort(render);
		for (BlockStoreDist d : render) {
			d.s.currentData[0].render();
		}
		gl.disable(GLFlag.CULL_FACE);
		for (BlockStoreDist d : render) {
			d.s.currentData[1].render();
		}
		for (ImplChunk c : chunks.values()) {
			c.drawEntitys();
		}
		Graphics.getClientShaderManager().bindShader("world");
		gl.enable(GLFlag.CULL_FACE);
		gl.cullFace(GLFace.BACK);
		gl.enable(GLFlag.DEPTH_TEST);
		gl.enable(GLFlag.ALPHA_TEST);
		gl.alphaFunc(GLCompareFunc.GREATER, .3f);
		gl.depthFunc(GLCompareFunc.LEQUAL);
		gl.bindTexture(GLTexture.TEX_2D_ARRAY, Resources.getAnimatedTexture("blocks").getTexture());
		for (BlockStoreDist d : render) {
			d.s.currentData[2].render();
		}
		gl.disable(GLFlag.CULL_FACE);
		gl.bindTexture(GLTexture.TEX_2D_ARRAY, 0);
	}

	public void drawBackground(EntityPlayer player) {
		getGenerator().renderSkyBox(this, player);
		Graphics.getOpenGL().color(1, 1, 1, 1);
	}

	@Override
	public Chunk generateChunk(ChunkPos pos) {
		ImplChunk chunk = new ImplChunk(this, pos);
		chunk.setData(GeneratorList.getWorldGenerator(generatorName).generate(this, pos.x, pos.y, pos.z, getOrGenerate(new Vector2l(pos.x, pos.z))));
		chunks.put(pos, chunk);
		return chunk;
	}

	public ImplMapData getOrGenerate(Vector2l pos) {
		ImplMapData data = heights.get(pos);
		if (data == null) data = loadMapData(pos);
		return data;
	}

	public ImplMapData loadMapData(Vector2l pos) {
		ImplMapData data = null;
		try {
			data = (ImplMapData) getSaveFormat().readMap(this, pos.x, pos.y);
		} catch (IOException e) {
			Logger.getLogger(ImplWorld.class).warning("Error while loading map data", e);
		}
		if(data == null){
			data = new ImplMapData(pos);
			getGenerator().generateMapData(this, data);
			try {
				getSaveFormat().writeMap(this, data);
			} catch (IOException e) {
				Logger.getLogger(ImplWorld.class).warning("Error while saving map data", e);
			}
		}
		heights.put(pos, data);
		return data;
	}

	@Override
	public AABB[] getAABBsInBounds(AABB bounds, Entity exclude) {
		List<AABB> list = getBlockCollisionInBounds(bounds);
		if (list == null) list = new ArrayList<AABB>();
		List<Entity> entities = getEntitiesInBoundsExcluding(bounds, exclude);
		for (int dofor = 0; dofor < entities.size(); ++dofor) {
			if (entities.get(dofor).bounds != null && entities.get(dofor).canCollide(exclude)) {
				list.add(entities.get(dofor).bounds);
			}
		}
		while (list.contains(null))
			list.remove(null);
		return list.toArray(new AABB[list.size()]);
	}

	public List<AABB> getBlockCollisionInBounds(AABB bounds) {
		ArrayList<AABB> list = new ArrayList<AABB>();
		bounds.correctMinMax();
		long sx = MathUtils.floor(bounds.minX);
		long sy = MathUtils.floor(bounds.minY);
		long sz = MathUtils.floor(bounds.minZ);
		long ex = MathUtils.floor(bounds.maxX + 1D);
		long ey = MathUtils.floor(bounds.maxY + 1D);
		long ez = MathUtils.floor(bounds.maxZ + 1D);
		for (long x = sx; x < ex; ++x) {
			for (long y = sy; y < ey; ++y) {
				for (long z = sz; z < ez; ++z) {
					Block b = getBlockType(x, y, z);
					if (b == null) continue;
					AABB.addAABBsToList(list, b.getCollisionBounds(this, x, y, z));
				}
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#getBlockData(long, long, long)
	 */
	@Override
	public byte getBlockData(long x, long y, long z) {
		Chunk c = getChunk(ChunkPos.createFromWorldPos(x, y, z));
		if (c != null) return c.getBlockData((int) (x & 31), (int) (y & 31), (int) (z & 31));
		else return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#getBlockId(long, long, long)
	 */
	@Override
	public short getBlockId(long x, long y, long z) {
		Chunk c = getChunk(ChunkPos.createFromWorldPos(x, y, z));
		if (c != null) return c.getBlockId((int) (x & 31), (int) (y & 31), (int) (z & 31));
		else return -1;
	}

	public BlockStore getBlockStore(long x, long y, long z) {
		ImplChunk c = getChunk(x >> 1, y >> 1, z >> 1);
		if (c == null) return fake;
		return c.stores[(int) ((x & 1) * 2 + (y & 1) * 2 + (z & 1))];
	}

	public BlockStore getBlockStoreFromWorld(long x, long y, long z) {
		x = x >> 4;
		y = y >> 4;
		z = z >> 4;
		ImplChunk c = getChunk(x >> 1, y >> 1, z >> 1);
		if (c == null || c.stores == null) return fake;
		return c.stores[(int) ((x & 1) * 4 + (y & 1) * 2 + (z & 1))];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#getBlockType(long, long, long)
	 */
	@Override
	public Block getBlockType(long x, long y, long z) {
		return Block.byId(getBlockId(x, y, z));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#getChunk(vc4.api.world.ChunkPos)
	 */
	@Override
	public ImplChunk getChunk(ChunkPos pos) {
		return chunks.get(pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#getChunk(long, long, long)
	 */
	@Override
	public ImplChunk getChunk(long x, long y, long z) {
		return getChunk(ChunkPos.create(x, y, z));
	}

	@Override
	public List<Entity> getEntitiesInBoundsExcluding(AABB bounds, Entity exclude) {
		if (bounds == null) return null;

		List<Entity> list = new ArrayList<>();
		long minX = MathUtils.floor(bounds.minX - 2D) >> 5;
		long minY = MathUtils.floor(bounds.minY - 2d) >> 5;
		long minZ = MathUtils.floor(bounds.minZ - 2d) >> 5;
		long maxX = MathUtils.floor(bounds.maxX + 2d) >> 5;
		long maxY = MathUtils.floor(bounds.maxY + 2d) >> 5;
		long maxZ = MathUtils.floor(bounds.maxZ + 2d) >> 5;
		for (long x = minX; x <= maxX; ++x) {
			for (long y = minY; y <= maxY; ++y) {
				for (long z = minZ; z <= maxZ; ++z) {
					ImplChunk c = chunks.get(ChunkPos.create(x, y, z));
					if (c == null || c.entitys == null) continue;
					// long start = System.nanoTime();
					for (int dofor = 0; dofor < c.entitys.size(); ++dofor) {
						Entity e = c.entitys.get(dofor);
						if (e != null && e != exclude && bounds.intersectsWith(e.bounds)) list.add(e);
					}
					// long time = System.nanoTime() - start;
					// Logger.getLogger(getClass()).info("E in BB took " + (time / 1000000D) + "ms");
				}
			}
		}

		return list;
	}
	
	@Override
	public List<Entity> getEntitiesInBoundsExcluding(AABB bounds, Class<? extends Entity> type, Entity exclude) {
		if (bounds == null) return null;

		List<Entity> list = new ArrayList<>();
		long minX = MathUtils.floor(bounds.minX - 2D) >> 5;
		long minY = MathUtils.floor(bounds.minY - 2d) >> 5;
		long minZ = MathUtils.floor(bounds.minZ - 2d) >> 5;
		long maxX = MathUtils.floor(bounds.maxX + 2d) >> 5;
		long maxY = MathUtils.floor(bounds.maxY + 2d) >> 5;
		long maxZ = MathUtils.floor(bounds.maxZ + 2d) >> 5;
		for (long x = minX; x <= maxX; ++x) {
			for (long y = minY; y <= maxY; ++y) {
				for (long z = minZ; z <= maxZ; ++z) {
					ImplChunk c = chunks.get(ChunkPos.create(x, y, z));
					if (c == null || c.entitys == null) continue;
					// long start = System.nanoTime();
					for (int dofor = 0; dofor < c.entitys.size(); ++dofor) {
						Entity e = c.entitys.get(dofor);
						if(!type.isAssignableFrom(e.getClass())) continue;
						if (e != null && e != exclude && bounds.intersectsWith(e.bounds)) list.add(e);
					}
					// long time = System.nanoTime() - start;
					// Logger.getLogger(getClass()).info("E in BB took " + (time / 1000000D) + "ms");
				}
			}
		}

		return list;
	}

	@Override
	public List<Entity> getEntitiesInBounds(AABB bounds) {
		if (bounds == null) return null;

		List<Entity> list = new ArrayList<>();
		long minX = MathUtils.floor(bounds.minX - 2D) >> 5;
		long minY = MathUtils.floor(bounds.minY - 2d) >> 5;
		long minZ = MathUtils.floor(bounds.minZ - 2d) >> 5;
		long maxX = MathUtils.floor(bounds.maxX + 2d) >> 5;
		long maxY = MathUtils.floor(bounds.maxY + 2d) >> 5;
		long maxZ = MathUtils.floor(bounds.maxZ + 2d) >> 5;
		long y, z;
		int dofor;
		ImplChunk c;
		Entity e;
		for (long x = minX; x <= maxX; ++x) {
			for (y = minY; y <= maxY; ++y) {
				for (z = minZ; z <= maxZ; ++z) {
					c = chunks.get(ChunkPos.create(x, y, z));
					if (c == null || c.entitys == null) continue;
					// long start = System.nanoTime();
					for (dofor = 0; dofor < c.entitys.size(); ++dofor) {
						e = c.entitys.get(dofor);
						if (e != null && bounds.intersectsWith(e.bounds)) list.add(e);
					}
					// long time = System.nanoTime() - start;
					// Logger.getLogger(getClass()).info("E in BB took " + (time / 1000000D) + "ms");
				}
			}
		}

		return list;
	}
	
	@Override
	public List<Entity> getEntitiesInBounds(AABB bounds, Class<? extends Entity> type) {
		if (bounds == null) return null;

		List<Entity> list = new ArrayList<>();
		long minX = MathUtils.floor(bounds.minX - 2D) >> 5;
		long minY = MathUtils.floor(bounds.minY - 2d) >> 5;
		long minZ = MathUtils.floor(bounds.minZ - 2d) >> 5;
		long maxX = MathUtils.floor(bounds.maxX + 2d) >> 5;
		long maxY = MathUtils.floor(bounds.maxY + 2d) >> 5;
		long maxZ = MathUtils.floor(bounds.maxZ + 2d) >> 5;
		long y, z;
		int dofor;
		ImplChunk c;
		Entity e;
		for (long x = minX; x <= maxX; ++x) {
			for (y = minY; y <= maxY; ++y) {
				for (z = minZ; z <= maxZ; ++z) {
					c = chunks.get(ChunkPos.create(x, y, z));
					if (c == null || c.entitys == null) continue;
					// long start = System.nanoTime();
					for (dofor = 0; dofor < c.entitys.size(); ++dofor) {
						e = c.entitys.get(dofor);
						if(!type.isAssignableFrom(e.getClass())) continue;
						if (e != null && bounds.intersectsWith(e.bounds)) list.add(e);
					}
					// long time = System.nanoTime() - start;
					// Logger.getLogger(getClass()).info("E in BB took " + (time / 1000000D) + "ms");
				}
			}
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#getNearbyBlockData(long, long, long, vc4.api.util.Direction)
	 */
	@Override
	public byte getNearbyBlockData(long x, long y, long z, Direction dir) {
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		return getBlockData(x, y, z);
	}

	@Override
	public byte getNearbyBlockData(long x, long y, long z, int d) {
		return getNearbyBlockData(x, y, z, Direction.getDirection(d));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#getNearbyBlockId(long, long, long, vc4.api.util.Direction)
	 */
	@Override
	public short getNearbyBlockId(long x, long y, long z, Direction dir) {
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		return getBlockId(x, y, z);
	}

	@Override
	public short getNearbyBlockId(long x, long y, long z, int d) {
		return getNearbyBlockId(x, y, z, Direction.getDirection(d));
	}

	@Override
	public TileEntity getNearbyTileEntity(long x, long y, long z, Direction dir) {
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		return getTileEntity(x, y, z);
	}

	@Override
	public TileEntity getNearbyTileEntity(long x, long y, long z, int dir) {
		return getNearbyTileEntity(x, y, z, Direction.getDirection(dir));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#getNearbyBlockType(long, long, long, vc4.api.util.Direction)
	 */
	@Override
	public Block getNearbyBlockType(long x, long y, long z, Direction dir) {
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		return getBlockType(x, y, z);
	}

	@Override
	public Block getNearbyBlockType(long x, long y, long z, int dir) {
		return getNearbyBlockType(x, y, z, Direction.getDirection(dir));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#getSeed()
	 */
	@Override
	public long getSeed() {
		return seed;
	}
	
	public void saveChunk(long x, long y, long z){
		Chunk c = chunks.get(ChunkPos.createFromWorldPos(x, y, z));
		if(c != null) saveChunk(c);
	}
	
	public void saveChunk(Chunk chunk){
		genThreads[(int) (chunk.getChunkPos().z & GeneratorThread.maxNum)].toSave.add((ImplChunk)chunk);
	}

	@SuppressWarnings("unchecked")
	public void infiniteWorld(Vector3d loc) {
		Profiler.start("generation");
		Profiler.start("remove");
		ArrayList<ImplChunk> toRemove = new ArrayList<>();
		ArrayList<ImplChunk> toPopulate = new ArrayList<>();
		for (ImplChunk c : chunks.values()) {
			if (c.distanceSquared(loc) > 65565) {
				c.setUnloading(true);
				c.removeGraphics();
				toRemove.add(c);
				if (c.isModified()) saveChunk(c);
				else c.removeData();
			}
		}
		chunks.values().removeAll(toRemove);
		Profiler.stopStart("load");
		ImplChunk cnk;
		ImplMapData dta;
		GeneratorThread.location = loc.clone();
		for (int d = 0; d < genThreads.length; ++d) {
			while ((dta = genThreads[d].newData.poll()) != null) {
				if(heights.get(dta.getPosition()) != null) continue;
				heights.put(dta.getPosition(), dta);
			}
		}
		for (int d = 0; d < genThreads.length; ++d) {
			while ((cnk = genThreads[d].generated.poll()) != null) {
				if(chunks.get(cnk.pos) != null) continue;
				chunks.put(cnk.pos, cnk);
			}
		}
		GeneratorThread.chunks = (HashMap<ChunkPos, ImplChunk>) chunks.clone();
		GeneratorThread.heights = (HashMap<Vector2l, ImplMapData>) heights.clone();
		Profiler.stopStart("killmapdata");
		ArrayList<MapData> deadData = new ArrayList<>();
		for (ImplMapData d : heights.values()) {
			if (d.distanceSquared(loc) > 80000) {
				deadData.add(d);
			}
		}
		heights.values().removeAll(deadData);
		Profiler.stopStart("populate");
		for (ImplChunk c : chunks.values()) {
			if (!c.isPopulated() && c.isSurrounded()) {
				toPopulate.add(c);
			}
		}
		Collections.sort(toPopulate, new ComparatorClosestChunk(loc));
		for (int d = 0; d < 4; ++d) {
			if (toPopulate.size() <= d) break;
			ChunkPos pop = toPopulate.get(d).pos;
			boolean plt = populate(pop.x, pop.y, pop.z);
			if(plt) saveChunk(toPopulate.get(d));
			toPopulate.get(d).setPopulated(plt);
		}
		Profiler.stop();
		Profiler.stop();
	}

	private boolean populate(long x, long y, long z) {
		MapData data = getMapData(x, z);
		if (data == null) return false;
		GeneratorList.getWorldGenerator(generatorName).populate(this, x, y, z);
		if (y < -8 || y > 8) return true;
		if(!getGenerator().generatePlants(this, x, y, z)) return true;
		ArrayList<PlantGrowth> growing = new ArrayList<>();
		growing.addAll(data.getBiome(0, 0).getPlants());
		growing.addAll(data.getBiome(31, 0).getPlants());
		growing.addAll(data.getBiome(31, 31).getPlants());
		growing.addAll(data.getBiome(0, 31).getPlants());
		if (growing.size() < 1) return true;
		ArrayList<PlantGrowth> plants = new ArrayList<>();
		PlantGrowth curr, add;
		for (int d = 0; d < growing.size(); ++d) {
			curr = growing.get(d);
			if (!plants.contains(curr)) {
				plants.add(curr.clone());
				continue;
			}
			add = plants.get(plants.indexOf(curr));
			add.addAmount(curr.getAmount());
		}
		Random rand = createRandom(x, y, z, 126862622643624L);
		PlantGenerator gen;
		for (int d = 0; d < plants.size(); ++d) {
			curr = plants.get(d);
			curr.setAmount(curr.getAmount() / 4);
			gen = GeneratorList.getPlantGenerator(curr.getPlant());
			if (gen == null) continue;
			for (int i = 0; i < curr.getAmount(); ++i) {
				gen.growPlant(this, data, x, y, z, rand, curr.getPlant());
			}
		}
		return true;
	}


	public void notifyNear(long x, long y, long z) {
		Direction dir;
		long px, py, pz;
		Block b;
		for (int d = 0; d < 6; ++d) {
			dir = Direction.getDirection(d);
			px = x + dir.getX();
			py = y + dir.getY();
			pz = z + dir.getZ();
			b = getBlockType(px, py, pz);
			b.nearbyBlockChanged(this, px, py, pz, dir.opposite());
		}
	}
	
	@Override
	public RayTraceResult rayTraceEntitys(EntityLiving entity, Vector3d end, double reach) {
		Vector3d start = entity.getEyePos();
		AABB checkBounds = entity.bounds.include(end.x, end.y, end.z).expand(1F, 1F, 1F);
		List<Entity> entities = getEntitiesInBoundsExcluding(checkBounds, entity);
		Entity pointing = null;
		double closest = reach + 1;
		Vector3d vector = null;

		for (int i = 0; i < entities.size(); i++) {
			Entity ne = entities.get(i);

			if (!ne.includeInRayTrace() || ne.isDead) {
				continue;
			}

			AABB aabb = ne.bounds.expand(0.1F, 0.1F, 0.1F);
			RayTraceResult rtr = aabb.calculateIntercept(start, end);
			
			if (rtr == null) {
				continue;
			}

			if (aabb.isVecInside(start)) {
				if (0.0D < closest || closest == 0.0D) {
					pointing = ne;
					closest = 0.0D;
					vector = rtr.vector;
				}

				continue;
			}

			double dist = start.distance(rtr.vector);

			if (dist < closest || closest == 0.0D) {
				pointing = ne;
				closest = dist;
				vector = rtr.vector;
			}
		}

		if (pointing != null) { return new RayTraceResult(pointing, vector); }

		return null;
	}

	@Override
	public RayTraceResult rayTraceBlocks(Vector3d start, Vector3d end, int amount) {
		long endBlockX = MathUtils.floor(end.x);
		long endBlockY = MathUtils.floor(end.y);
		long endBlockZ = MathUtils.floor(end.z);
		long posX = MathUtils.floor(start.x);
		long posY = MathUtils.floor(start.y);
		long posZ = MathUtils.floor(start.z);
		int startBlockID = getBlockId(posX, posY, posZ);
		int startBlockData = getBlockData(posX, posY, posZ);
		Block primaryBlock = Block.byId(startBlockID);
		if (!primaryBlock.isAir() && primaryBlock.includeInRayTrace((byte) startBlockData) && primaryBlock.getRayTraceSize(this, posX, posY, posZ) != null) {
			RayTraceResult primaryResult = primaryBlock.getRayTrace(this, posX, posY, posZ, start, end);
			if (primaryResult != null) return primaryResult;
		}
		boolean xFlag, yFlag, zFlag;
		double xAmount, yAmount, zAmount;
		double xSide, ySide, zSide;
		double xDist, yDist, zDist;
		byte side;
		Vector3d other;
		int blockID, blockData;
		Block block;
		RayTraceResult newResult;
		for (int pos = amount; pos-- >= 0;) {
			if (posX == endBlockX && posY == endBlockY && posZ == endBlockZ) return null;
			xFlag = true;
			yFlag = true;
			zFlag = true;
			xAmount = 999F;
			yAmount = 999F;
			zAmount = 999F;
			if (endBlockX > posX) xAmount = posX + 1.0F;
			else if (endBlockX < posX) xAmount = posX + 0.0f;
			else xFlag = false;
			if (endBlockY > posY) yAmount = posY + 1.0f;
			else if (endBlockY < posY) yAmount = posY + 0.0f;
			else yFlag = false;
			if (endBlockZ > posZ) zAmount = posZ + 1.0f;
			else if (endBlockZ < posZ) zAmount = posZ + 0.0f;
			else zFlag = false;
			xSide = 999f;
			ySide = 999f;
			zSide = 999f;
			xDist = end.x - start.x;
			yDist = end.y - start.y;
			zDist = end.z - start.z;
			if (xFlag) xSide = (xAmount - start.x) / xDist;
			if (yFlag) ySide = (yAmount - start.y) / yDist;
			if (zFlag) zSide = (zAmount - start.z) / zDist;
			side = 0;
			if (xSide < ySide && xSide < zSide) {
				if (endBlockX > posX) side = 2; // Fixed
				else side = 0; // Fixed
				start.x = xAmount;
				start.y += yDist * xSide;
				start.z += zDist * xSide;
			} else if (ySide < zSide) {
				if (endBlockY > posY) side = 5; // Fixed
				else side = 4; // Fixed
				start.x += xDist * ySide;
				start.y = yAmount;
				start.z += zDist * ySide;
			} else {
				if (endBlockZ > posZ) side = 3; // Fixed
				else side = 1; // Fixed
				start.x += xDist * zSide;
				start.y += yDist * zSide;
				start.z = zAmount;
			}
			other = new Vector3d(start.x, start.y, start.z);
			posX = (int) (other.x = MathUtils.floor(start.x));
			if (side == 0) { // Fixed
				posX--;
				++other.x;
			}
			posY = (int) (other.y = MathUtils.floor(start.y));
			if (side == 4) { // Fixed
				posY--;
				++other.y;
			}
			posZ = (int) (other.z = MathUtils.floor(start.z));
			if (side == 1) { // Fixed
				posZ--;
				++other.z;
			}
			blockID = getBlockId(posX, posY, posZ);
			blockData = getBlockData(posX, posY, posZ);
			block = Block.byId(blockID);
			if (!block.isAir() && block.includeInRayTrace((byte) blockData) && block.getRayTraceSize(this, posX, posY, posZ) != null) {
				newResult = block.getRayTrace(this, posX, posY, posZ, start, end);
				if (newResult != null) return newResult;
			}
		}

		return null;

	}

	@Override
	public void setTileEntity(long x, long y, long z, TileEntity t) {
		ImplChunk c = getChunk(ChunkPos.createFromWorldPos(x, y, z));
		if (c != null) {
			c.setTileEntity((int) (x & 31), (int) (y & 31), (int) (z & 31), t);
			notifyNear(x, y, z);
		}
	}

	@Override
	public TileEntity getTileEntity(long x, long y, long z) {
		ImplChunk c = getChunk(ChunkPos.createFromWorldPos(x, y, z));
		if (c != null) { return c.getTileEntity((int) (x & 31), (int) (y & 31), (int) (z & 31)); }
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#setBlockData(long, long, long, int)
	 */
	@Override
	public void setBlockData(long x, long y, long z, int data) {
		Chunk c = getChunk(ChunkPos.createFromWorldPos(x, y, z));
		if (c != null) {
			c.setBlockData((int) (x & 31), (int) (y & 31), (int) (z & 31), (byte) data);
			notifyNear(x, y, z);
		}
	}

	@Override
	public void setBlockDataNoNotify(long x, long y, long z, int data) {
		Chunk c = getChunk(ChunkPos.createFromWorldPos(x, y, z));
		if (c != null) {
			c.setBlockData((int) (x & 31), (int) (y & 31), (int) (z & 31), (byte) data);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#setBlockId(long, long, long, int)
	 */
	@Override
	public void setBlockId(long x, long y, long z, int id) {
		Chunk c = getChunk(ChunkPos.createFromWorldPos(x, y, z));
		if (c != null) {
			c.setBlockId((int) (x & 31), (int) (y & 31), (int) (z & 31), (short) id);
			notifyNear(x, y, z);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#setBlockIdData(long, long, long, int, int)
	 */
	@Override
	public void setBlockIdData(long x, long y, long z, int id, int data) {
		Chunk c = getChunk(ChunkPos.createFromWorldPos(x, y, z));
		if (c != null) {
			c.setBlockIdData((int) (x & 31), (int) (y & 31), (int) (z & 31), (short) id, (byte) data);
			notifyNear(x, y, z);
		}
	}

	@Override
	public void setBlockIdDataNoNotify(long x, long y, long z, int id, int data) {
		Chunk c = getChunk(ChunkPos.createFromWorldPos(x, y, z));
		if (c != null) {
			c.setBlockIdData((int) (x & 31), (int) (y & 31), (int) (z & 31), (short) id, (byte) data);
		}
	}

	@Override
	public void setBlockIdNoNotify(long x, long y, long z, int id) {
		Chunk c = getChunk(ChunkPos.createFromWorldPos(x, y, z));
		if (c != null) {
			c.setBlockId((int) (x & 31), (int) (y & 31), (int) (z & 31), (short) id);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#setNearbyBlockData(long, long, long, int, vc4.api.util.Direction)
	 */
	@Override
	public void setNearbyBlockData(long x, long y, long z, int data, Direction dir) {
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		setBlockData(x, y, z, data);
	}

	@Override
	public void setNearbyBlockDataNoNotify(long x, long y, long z, int data, Direction dir) {
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		setBlockDataNoNotify(x, y, z, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#setNearbyBlockId(long, long, long, int, vc4.api.util.Direction)
	 */
	@Override
	public void setNearbyBlockId(long x, long y, long z, int id, Direction dir) {
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		setBlockId(x, y, z, id);
	}

	@Override
	public void setNearbyTileEntity(long x, long y, long z, TileEntity t, Direction dir) {
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		setTileEntity(x, y, z, t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.world.World#setNearbyBlockIdData(long, long, long, int, int, vc4.api.util.Direction)
	 */
	@Override
	public void setNearbyBlockIdData(long x, long y, long z, int id, int data, Direction dir) {
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		setBlockIdData(x, y, z, id, data);
	}

	@Override
	public void setNearbyBlockIdDataNoNotify(long x, long y, long z, int id, int data, Direction dir) {
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		setBlockIdDataNoNotify(x, y, z, id, data);

	}

	@Override
	public void setNearbyBlockIdNoNotify(long x, long y, long z, int id, Direction dir) {
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		setBlockIdNoNotify(x, y, z, id);

	}

	public void update(Vector3d loc, double delta) {
		tickTime += delta;
		int amt = 0;
		while (tickTime >= WORLD_SECOND && amt < 2) {
			tickTime -= WORLD_SECOND;
			++time;
			updateTick(loc);
			++amt;
			if (amt == 2) tickTime = 0;
		}
		if (amt == 10) tickTime = 0;
		if (time < 0) time = 0;
	}

	@SuppressWarnings("unchecked")
	public void updateTick(Vector3d loc) {
		for(EntityPlayer plr : players){
			plr.addTickSinceUpdate();
			if(plr.getTicksSinceUpdate() > 5){
				plr.world = this;
				plr.addToWorld();
				plr.setTicksSinceUpdate(0);
				Logger.getLogger("VC4").info("Found player " + plr.getPlayerName() + " not in world");
			}
		}
		Profiler.clear();
		infiniteWorld(loc);
		Profiler.start("update");
		Random rand = createRandom(time, (long) loc.x, (long) loc.z);
		HashMap<ChunkPos, ImplChunk> cnk = (HashMap<ChunkPos, ImplChunk>) chunks.clone();
		for (ImplChunk c : cnk.values()) {
			c.update(rand);
		}
		Profiler.start("blocks");
		LinkedList<Vector4l> current = (LinkedList<Vector4l>) blockUpdates.clone();
		blockUpdates.clear();
		int i;
		for (Vector4l b : current) {
			if (b.w == 0) {
				i = getBlockType(b.x, b.y, b.z).blockUpdate(this, rand, b.x, b.y, b.z);
				if (i > 0) blockUpdates.add(new Vector4l(b.x, b.y, b.z, i));
			} else {
				b.w--;
				blockUpdates.add(b);
			}
		}
		Profiler.stop();
		Profiler.stop();
	}

	public void scheduleBlockUpdate(long x, long y, long z, int time) {
		blockUpdates.add(new Vector4l(x, y, z, time));
	}

	@Override
	public long getTime() {
		return time;
	}

	public CompoundTag getSaveCompound() {
		CompoundTag root = new CompoundTag("root");
		root.setLong("time", time);
		root.setString("generator", generatorName);
		root.setLong("seed", seed);
		root.setString("name", name);
		root.addTag(generatorTag);
		root.setString("format", saveFormatName);
		return root;
	}

	public void loadSaveCompound(CompoundTag root) {
		time = root.getLong("time");
		generatorName = root.getString("generator");
		seed = root.getLong("seed");
		name = root.getString("name");
		generatorTag = root.getCompoundTag("gen");
		saveFormatName = root.getString("format", "VCH4");
		saveFormat = SaveFormats.getSaveFormat(saveFormatName);
	}
	
	public void savePlayer(EntityPlayer player){
		String dirPath = DirectoryLocator.getPath() + "/worlds/" + saveName + "/players/";
		File dir = new File(dirPath);
		if (!dir.exists() && !dir.mkdirs()) return;
		File wld = new File(dirPath + player.getPlayerName() + ".vbt"); // VBT format, not bnbt (Binary NBT VC3)
		try(NBTOutputStream out = new NBTOutputStream(new FileOutputStream(wld), true)){
			out.writeTag(player.getSaveCompound());
		} catch (IOException e) {
			Logger.getLogger(ImplWorld.class).warning("Failed to save player: " + player.getName(), e);
		}
	}
	
	public EntityPlayer loadPlayer(String name){
		String path = DirectoryLocator.getPath() + "/worlds/" + saveName + "/players/" + name + ".vbt";
		File file = new File(path);
		if(!file.exists()) return spawnPlayer(name);
		try (NBTInputStream in = new NBTInputStream(new FileInputStream(file), true)) {
			EntityPlayer plr = (EntityPlayer) Entity.loadEntity(this, (CompoundTag) in.readTag());
			plr.loadNearbyChunks();
			plr.addToWorld();
			addPlayer(plr);
			return plr;
		} catch(IOException e){
			Logger.getLogger("VC4").warning("Error while loading player: " + name, e);
			return spawnPlayer(name);
		}
	}
	
	private EntityPlayer spawnPlayer(String name){
		EntityPlayer plr = new EntityPlayer(this);
		MapData dat = getOrGenerate(new Vector2l(0, 0));
		plr.setSpawn(new Vector3d(16, dat.getHeight(16, 16) + 2, 16));
		plr.respawn();
		plr.addToWorld();
		addPlayer(plr);
		return plr;
	}

	public void saveInfo() throws IOException {
		String dirPath = DirectoryLocator.getPath() + "/worlds/" + saveName + "/";
		File dir = new File(dirPath);
		if (!dir.exists() && !dir.mkdirs()) return;
		registeredBlocks.save(new FileOutputStream(dirPath + "blocks.dictionary"));
		registeredItems.save(new FileOutputStream(dirPath + "items.dictionary"));
		registeredEntitys.save(new FileOutputStream(dirPath + "entitys.dictionary"));
		registeredCrafting.save(new FileOutputStream(dirPath + "crafting.dictionary"));
		registeredBiomes.save(new FileOutputStream(dirPath + "biomes.dictionary"));
		registeredContainers.save(new FileOutputStream(dirPath + "containers.dictionary"));
		registeredTileEntitys.save(new FileOutputStream(dirPath + "tileentitys.dictionary"));
		registeredItemEntitys.save(new FileOutputStream(dirPath + "itementitys.dictionary"));
		registeredTraits.save(new FileOutputStream(dirPath + "traits.dictionary"));
		registeredAreas.save(new FileOutputStream(dirPath + "areas.dictionary"));
		File wld = new File(dirPath + "world.vbt"); // VBT format, not bnbt (Binary NBT VC3)
		NBTOutputStream out = new NBTOutputStream(new FileOutputStream(wld), true);
		out.writeTag(getSaveCompound());
		out.close();
		PluginLoader.onWorldSave(this);
	}

	public void unloadChunks() {
		GeneratorThread.stop = true;
		Logger.getLogger("VC4").info("Waiting for gen threads");
		outside: while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			for (int d = 0; d < genThreads.length; ++d) {
				if (genThreads[d].isAlive()) continue outside;
			}
			break;
		}
		Logger.getLogger("VC4").info("Saving chunks");
		int saved = 0;
		for(ImplChunk c : chunks.values()){
			if(c.isModified()){
				UnloadThread.toSave.add(c);
				saved++;
			}
		}
		UnloadThread.world = this;
		UnloadThread[] unload = new UnloadThread[32];
		for(int d = 0; d < unload.length; ++d){
			unload[d] = new UnloadThread();
			unload[d].start();
		}
		outer: while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			for (int d = 0; d < unload.length; ++d) {
				if (unload[d].isAlive()) continue outer;
			}
			break;
		}
		Logger.getLogger("VC4").info("Saved " + saved + " chunks!");
		UnloadThread.world = null;
		chunks.clear();
	}

	@Override
	public short getRegisteredBlock(String name) {
		return (short) registeredBlocks.get(name);
	}

	@Override
	public int getRegisteredItem(String name) {
		return registeredItems.get(name);
	}

	@Override
	public short getRegisteredCrafting(String name) {
		return (short) registeredCrafting.get(name);
	}

	@Override
	public short getRegisteredEntity(String name) {
		return (short) registeredEntitys.get(name);
	}
	
	@Override
	public short getRegisteredArea(String name) {
		return (short) registeredAreas.get(name);
	}
	
	@Override
	public short getRegisteredTrait(String name) {
		return (short) registeredTraits.get(name);
	}
	
	@Override
	public short getRegisteredContainer(String name) {
		return (short) registeredContainers.get(name);
	}
	
	@Override
	public short getRegisteredTileEntity(String name) {
		return (short) registeredTileEntitys.get(name);
	}
	
	@Override
	public short getRegisteredItemEntity(String name) {
		return (short) registeredItemEntitys.get(name);
	}

	@Override
	public byte getRegisteredBiome(String name) {
		return (byte) registeredBiomes.get(name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public CompoundTag getGeneratorTag() {
		return generatorTag;
	}

	@Override
	public String getSaveName() {
		return saveName;
	}

	@Override
	public List<EntityPlayer> getPlayers() {
		return players;
	}

	@Override
	public double getFallAcceleration() {
		return 0.15;
	}

	@Override
	public double getFallMaxSpeed() {
		return 2.5;
	}

	@Override
	public boolean chunkExists(ChunkPos pos) {
		return chunks.get(pos) != null;
	}

	@Override
	public WorldGenerator getGenerator() {
		return GeneratorList.getWorldGenerator(generatorName);
	}

	@Override
	public Music getMusic(EntityPlayer player) {
		return getGenerator().getBiomeMusic(player);
	}

	@Override
	public MapData getMapData(long x, long z) {
		return heights.get(new Vector2l(x, z));
	}

	@Override
	public String[] getDebugInfo() {
		String[] res = new String[2];
		res[0] = "World: " + getName() + " (" + getSaveName() + ")" + ", seed:" + getSeed();
		res[1] = "Chunks: " + visibleChunks + "/" + chunks.size() + ", MapData: " + heights.size();
		return res;
	}

	@Override
	public Biome getBiome(long x, long z) {
		try {
			return getMapData(x >> 5, z >> 5).getBiome((int) (x & 31), (int) (z & 31));
		} catch (Exception e) {
			return fakeBiome;
		}
	}

	public void unload() {
		GeneratorThread.stop = true;
		try {
			saveInfo();
			unloadChunks();
			saveInfo();
		} catch (IOException e) {
			Logger.getLogger(ImplWorld.class).warning("Exception occured", e);
		}
	}

	@Override
	public Chunk loadChunk(ChunkPos pos) {
		ImplChunk c = null;
		try {
			c = (ImplChunk) getSaveFormat().readChunk(this, pos.x, pos.y, pos.z);
		} catch (IOException e) {
			Logger.getLogger(ImplWorld.class).warning("Failed to load chunk", e);
		}
		if(c != null){
			chunks.put(pos, c);
			return c;
		} else return generateChunk(pos);
	}

	@Override
	public String getEntityName(int id) {
		return registeredEntitys.getName(id);
	}
	
	@Override
	public String getTileEntityName(int id) {
		return registeredTileEntitys.getName(id);
	}
	
	@Override
	public String getItemEntityName(int id) {
		return registeredItemEntitys.getName(id);
	}
	
	@Override
	public String getTraitName(int id) {
		return registeredTraits.getName(id);
	}
	
	@Override
	public String getAreaName(int id) {
		return registeredAreas.getName(id);
	}
	
	@Override
	public String getContainerName(int id) {
		return registeredContainers.getName(id);
	}

	@Override
	public void broadcast(String message, Vector3d pos, double radius) {
		radius *= radius;
		for(EntityPlayer p : players){
			if(p.position.distanceSquared(pos) > radius) continue;
			p.message(message);
		}
	}

	@Override
	public List<Entity> getCollidableEntitiesInBounds(AABB bounds) {
		if (bounds == null) return null;

		List<Entity> list = new ArrayList<>();
		long minX = MathUtils.floor(bounds.minX - 2D) >> 5;
		long minY = MathUtils.floor(bounds.minY - 2d) >> 5;
		long minZ = MathUtils.floor(bounds.minZ - 2d) >> 5;
		long maxX = MathUtils.floor(bounds.maxX + 2d) >> 5;
		long maxY = MathUtils.floor(bounds.maxY + 2d) >> 5;
		long maxZ = MathUtils.floor(bounds.maxZ + 2d) >> 5;
		long y, z;
		int dofor;
		ImplChunk c;
		Entity e;
		for (long x = minX; x <= maxX; ++x) {
			for (y = minY; y <= maxY; ++y) {
				for (z = minZ; z <= maxZ; ++z) {
					c = chunks.get(ChunkPos.create(x, y, z));
					if (c == null || c.entitys == null) continue;
					// long start = System.nanoTime();
					for (dofor = 0; dofor < c.entitys.size(); ++dofor) {
						e = c.entitys.get(dofor);
						if (e != null && e.canCollide(null) && bounds.intersectsWith(e.bounds)) list.add(e);
					}
					// long time = System.nanoTime() - start;
					// Logger.getLogger(getClass()).info("E in BB took " + (time / 1000000D) + "ms");
				}
			}
		}

		return list;
	}

}
