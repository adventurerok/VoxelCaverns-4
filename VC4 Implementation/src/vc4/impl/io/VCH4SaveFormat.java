package vc4.impl.io;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import vc4.api.area.Area;
import vc4.api.entity.Entity;
import vc4.api.io.*;
import vc4.api.logging.Logger;
import vc4.api.profile.Profiler;
import vc4.api.tileentity.TileEntity;
import vc4.api.util.DirectoryLocator;
import vc4.api.vbt.*;
import vc4.api.vector.Vector2l;
import vc4.api.vector.Vector3l;
import vc4.api.world.*;
import vc4.impl.world.*;

/**
 * This class uses VBT for all chunk data except blocks and data. It should work with a VBT loader, but modifying it would delete the blocks and data.
 * 
 * @author paul
 * 
 */
public class VCH4SaveFormat implements SaveFormat {

	public static boolean ENABLED = true;

	public static VCH4SaveFormat VCH4_SAVE_FORMAT = new VCH4SaveFormat();

	@SuppressWarnings("resource")
	@Override
	public Chunk readChunk(World world, long x, long y, long z) throws FileNotFoundException, IOException {
		if (!ENABLED) return null;
		String path = DirectoryLocator.getPath() + "/worlds/" + world.getSaveName() + "/chunks/" + y + "/" + z + "/" + x + ".vch4";
		File file = new File(path);
		if (!file.exists()) return null;
		ImplChunk chunk = new ImplChunk((ImplWorld) world, ChunkPos.create(x, y, z));
		try (DataInputStream in = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))))) {
			TagCompound root = (TagCompound) new NBTInputStream(in, false).readTag();
			chunk.setPopulated(root.getBoolean("populated", false));
			if (root.hasKey("entitys")) {
				TagList elist = root.getListTag("entitys");
				while (elist.hasNext()) {
					TagCompound ent = (TagCompound) elist.getNextTag();
					try {
						chunk.entitys.add(Entity.loadEntity(world, ent));
					} catch (Exception e) {
						Logger.getLogger("VC4").warning("Failed to load entity " + world.getEntityName(ent.getShort("id")), e);
					}
				}
			}
			if (root.hasKey("tiles")) {
				TagList tlist = root.getListTag("tiles");
				while (tlist.hasNext()) {
					TagCompound ent = (TagCompound) tlist.getNextTag();
					try {
						TileEntity tile = TileEntity.loadTileEntity(chunk, ent);
						chunk.tileEntitys.put(tile.getPositionInChunk(), tile);
					} catch (Exception e) {
						Logger.getLogger("VC4").warning("Failed to load tile entity " + world.getTileEntityName(ent.getShort("id")), e);
					}
				}
			}
			if (root.hasKey("areas")) {
				TagList tlist = root.getListTag("areas");
				while (tlist.hasNext()) {
					TagCompound ent = (TagCompound) tlist.getNextTag();
					try {
						Area tile = Area.loadArea(world, ent);
						chunk.areas.add(tile);
					} catch (Exception e) {
						Logger.getLogger("VC4").warning("Failed to load area " + world.getTileEntityName(ent.getShort("id")), e);
					}
				}
			}
			if (root.hasKey("bluds")) {
				TagList bluds = root.getListTag("bluds");
				loadBlockUpdateTag(chunk, bluds);
			}
			for (int d = 0; d < 8; ++d) {
				BlockStore store = chunk.getBlockStore(d);
				int bt = in.readByte();
				if (bt != 0) store.blocks = new short[4096];
				if (bt == 1) {
					Arrays.fill(store.getBlocks(), in.readShort());
				} else if (bt == 2) {
					for (int a = 0; a < 4096; ++a) {
						store.blocks[a] = (short) in.readShort();
					}
				}
				int dt = in.readByte();
				if (dt != 0) store.data = new byte[4096];
				if (dt == 1) {
					Arrays.fill(store.getData(), in.readByte());
				} else if (dt == 2) {
					for (int a = 0; a < 4096; ++a) {
						store.data[a] = in.readByte();
					}
				}
			}
		}
		return chunk;
	}

	@SuppressWarnings({ "resource", "unchecked" })
	@Override
	public void writeChunk(Chunk c) throws IOException {
		if (!ENABLED) return;
		Profiler.start("writechunk");
		ImplChunk chunk = (ImplChunk) c;
		String path = DirectoryLocator.getPath() + "/worlds/" + chunk.getWorld().getSaveName() + "/chunks/" + chunk.getChunkPos().y + "/" + chunk.getChunkPos().z + "/" + chunk.getChunkPos().x + ".vch4";
		File file = new File(path);
		file.getParentFile().mkdirs();
		TagCompound root = new TagCompound("root");
		root.setBoolean("populated", chunk.isPopulated());
		TagList elist = new TagList("entitys", TagCompound.class);
		List<Entity> lis = (List<Entity>) c.getEntityList().clone();
		for (int d = 0; d < lis.size(); ++d) {
			if (!lis.get(d).persistent()) continue;
			TagCompound tag = lis.get(d).getSaveCompound();
			elist.addTag(tag);
		}
		root.addTag(elist);
		TagList tlist = new TagList("tiles", TagCompound.class);
		for (TileEntity t : chunk.getTileEntitys().values()) {
			if (!t.persistent()) continue;
			TagCompound tag = t.getSaveCompound();
			tlist.addTag(tag);
		}
		root.addTag(tlist);
		TagList alist = new TagList("areas", TagCompound.class);
		for (Area a : chunk.getAreas()) {
			if (!a.persistent()) continue;
			TagCompound tag = a.getSaveCompound();
			alist.addTag(tag);
		}
		root.addTag(alist);
		root.addTag(getBlockUpdateTag(chunk));
		short[] blocks;
		byte[] data;
		try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(path))))) {
			BitOutputStream bos = new BitOutputStream(out);
			new NBTOutputStream(bos).writeTag(root);
			bos.flush();
			for (int d = 0; d < 8; ++d) {
				Profiler.start("blockstore");
				BlockStore store = chunk.getBlockStore(d);
				blocks = store.blocks;
				if (blocks == null) out.writeByte(0); // 0 = Blocks is null
				else {
					short only = getOnlyElement(blocks);
					if (only != -1) {
						out.writeByte(1); // 1 = Only 1 Block type
						out.writeShort(only);
					} else {
						Profiler.start("allblocks");
						out.writeByte(2); // 2 = Different block types
						for (int a = 0; a < blocks.length; ++a) {
							out.writeShort(blocks[a]);
						}
						Profiler.stop();
					}
				}
				data = store.data;
				if (data == null) out.writeByte(0); // 0 = Data is null
				else {
					byte only = getOnlyElement(data);
					if (only != -1) {
						out.writeByte(1); // 1 = Only 1 Data type
						out.writeByte(only);
					} else {
						Profiler.start("alldata");
						out.writeByte(2); // 2 = Different data types
						for (int a = 0; a < data.length; ++a) {
							out.writeByte(data[a]);
						}
						Profiler.stop();
					}
				}
				Profiler.stop();
			}
			out.flush();
		}
		Profiler.stop();
		// Files.move(new File(path + ".err").toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	public short getOnlyElement(short[] check) {
		short s = check[0];
		for (int d = 1; d < check.length; ++d) {
			if (check[d] != s) return -1;
		}
		return s;
	}

	public byte getOnlyElement(byte[] check) {
		byte s = check[0];
		for (int d = 1; d < check.length; ++d) {
			if (check[d] != s) return -1;
		}
		return s;
	}

	@SuppressWarnings("resource")
	@Override
	public MapData readMap(World world, long x, long z) throws IOException {
		if (!ENABLED) return null;
		String path = DirectoryLocator.getPath() + "/worlds/" + world.getSaveName() + "/map/" + z + "/" + x + ".vmd4";
		File file = new File(path);
		if (!file.exists()) return null;
		MapData data = new ImplMapData(new Vector2l(x, z));
		try (DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(file)))) {
			BitInputStream bis = new BitInputStream(in);
			TagCompound tag = (TagCompound) new NBTInputStream(bis).readTag();
			if (tag.getBoolean("genheights")) {
				int[] heights = new int[32 * 32];
				for (int d = 0; d < heights.length; ++d) {
					heights[d] = in.readInt();
				}
				data.setGenHeightMap(heights);
			}
			if (tag.getBoolean("biomes")) {
				short[] biomes = new short[32 * 32];
				for (int d = 0; d < biomes.length; ++d) {
					biomes[d] = in.readShort();
				}
				data.setBiomeMap(biomes);
			}
			if (tag.getBoolean("skyheights")) {
				int[] heights = new int[32 * 32];
				for (int d = 0; d < heights.length; ++d) {
					heights[d] = in.readInt();
				}
				data.setHeightMap(heights);
			}
		}
		return data;
	}

	@SuppressWarnings("resource")
	@Override
	public void writeMap(World world, MapData map) throws IOException {
		if (!ENABLED) return;
		String path = DirectoryLocator.getPath() + "/worlds/" + world.getSaveName() + "/map/" + map.getPosition().y + "/" + map.getPosition().x + ".vmd4";
		File file = new File(path);
		file.getParentFile().mkdirs();
		TagCompound root = new TagCompound("root");
		root.setBoolean("genheights", true);
		root.setBoolean("skyheights", true);
		root.setBoolean("biomes", true);
		try (DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(file)))) {
			BitOutputStream bos = new BitOutputStream(out);
			new NBTOutputStream(bos).writeTag(root);
			bos.flush();
			int[] genHeights = map.getGenHeightMap();
			for (int d = 0; d < genHeights.length; ++d) {
				out.writeInt(genHeights[d]);
			}
			short[] biomes = map.getBiomeMap();
			for (int d = 0; d < biomes.length; ++d) {
				out.writeShort(biomes[d]);
			}
			int[] skyHeights = map.getHeightMap();
			for (int d = 0; d < skyHeights.length; ++d) {
				out.writeInt(skyHeights[d]);
			}
		}
	}

	public TagList getBlockUpdateTag(ImplChunk chunk) {
		TagList tag = new TagList("bluds", TagCompound.class);
		for (BlockUpdate u : chunk.getBlockUpdates()) {
			TagCompound p = new TagCompound("u");
			p.setByte("x", (byte) u.x);
			p.setByte("y", (byte) u.y);
			p.setByte("z", (byte) u.z);
			p.setInt("t", u.time);
			p.setInt("i", u.id);
			tag.addTag(p);
		}
		return tag;

	}

	public void loadBlockUpdateTag(ImplChunk chunk, TagList tag) {
		while (tag.hasNext()) {
			TagCompound c = (TagCompound) tag.getNextTag();
			BlockUpdate ud = new BlockUpdate(0, 0, 0, 0, 0);
			ud.x = c.getByte("x");
			ud.y = c.getByte("y");
			ud.z = c.getByte("z");
			ud.time = c.getInt("t");
			ud.id = c.getInt("i");
			chunk.getBlockUpdates().add(ud);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeChunk(Chunk c, BitOutputStream bos) {
		// if (!ENABLED) return;
		Profiler.start("writechunk");
		ImplChunk chunk = (ImplChunk) c;
		String path = DirectoryLocator.getPath() + "/worlds/" + chunk.getWorld().getSaveName() + "/chunks/" + chunk.getChunkPos().y + "/" + chunk.getChunkPos().z + "/" + chunk.getChunkPos().x + ".vch4";
		File file = new File(path);
		file.getParentFile().mkdirs();
		TagCompound root = new TagCompound("root");
		root.addTag(TagCompound.createVector3lTag("pos", chunk.getChunkPos().toVector3l()));
		root.setByte("populated", chunk.isPopulated() ? 1 : 0);
		TagList elist = new TagList("entitys", TagCompound.class);
		List<Entity> lis = (List<Entity>) c.getEntityList().clone();
		for (int d = 0; d < lis.size(); ++d) {
			if (!lis.get(d).persistent()) continue;
			TagCompound tag = lis.get(d).getSaveCompound();
			elist.addTag(tag);
		}
		root.addTag(elist);
		TagList tlist = new TagList("tiles", TagCompound.class);
		for (TileEntity t : chunk.getTileEntitys().values()) {
			if (!t.persistent()) continue;
			TagCompound tag = t.getSaveCompound();
			tlist.addTag(tag);
		}
		root.addTag(tlist);
		TagList alist = new TagList("areas", TagCompound.class);
		for (Area a : chunk.getAreas()) {
			if (!a.persistent()) continue;
			TagCompound tag = a.getSaveCompound();
			alist.addTag(tag);
		}
		root.addTag(alist);
		root.addTag(getBlockUpdateTag(chunk));
		short[] blocks;
		byte[] data;
		try {
			DataOutputStream out = new DataOutputStream(bos.getOutputStream());
			bos.writeNbt(root);
			// Logger.getLogger("TST").fine("IBC: " + bos.getBitCount());
			for (int d = 0; d < 8; ++d) {
				Profiler.start("blockstore");
				BlockStore store = chunk.getBlockStore(d);
				blocks = store.blocks;
				if (blocks == null) out.writeByte(0); // 0 = Blocks is null
				else {
					short only = getOnlyElement(blocks);
					if (only != -1) {
						out.writeByte(1); // 1 = Only 1 Block type
						out.writeShort(only);
					} else {
						Profiler.start("allblocks");
						out.writeByte(2); // 2 = Different block types
						for (int a = 0; a < blocks.length; ++a) {
							out.writeShort(blocks[a]);
						}
						Profiler.stop();
					}
				}
				data = store.data;
				if (data == null) out.writeByte(0); // 0 = Data is null
				else {
					byte only = getOnlyElement(data);
					if (only != -1) {
						out.writeByte(1); // 1 = Only 1 Data type
						out.writeByte(only);
					} else {
						Profiler.start("alldata");
						out.writeByte(2); // 2 = Different data types
						for (int a = 0; a < data.length; ++a) {
							out.writeByte(data[a]);
						}
						Profiler.stop();
					}
				}
			}
			// out.flush();
		} catch (Exception e) {
			Logger.getLogger(VCH4SaveFormat.class).severe("Failed to write chunk", e);
		}
		Profiler.stop();
	}

	@SuppressWarnings("resource")
	@Override
	public Chunk readChunk(World world, BitInputStream bis) {
		// if (!ENABLED) return null;
		ImplChunk chunk;
		try {
			DataInputStream in = new DataInputStream(bis.getInputStream());
			TagCompound root = (TagCompound) new NBTInputStream(bis).readTag();
			Vector3l chunkPos = root.getCompoundTag("pos").readVector3l();
			chunk = new ImplChunk((ImplWorld) world, ChunkPos.create(chunkPos.x, chunkPos.y, chunkPos.z));
			chunk.setPopulated(root.getByte("populated", 0) == 1 ? true : false);
			if (root.hasKey("entitys")) {
				TagList elist = root.getListTag("entitys");
				while (elist.hasNext()) {
					TagCompound ent = (TagCompound) elist.getNextTag();
					try {
						chunk.entitys.add(Entity.loadEntity(world, ent));
					} catch (Exception e) {
						Logger.getLogger("VC4").warning("Failed to load entity " + world.getEntityName(ent.getShort("id")), e);
					}
				}
			}
			if (root.hasKey("tiles")) {
				TagList tlist = root.getListTag("tiles");
				while (tlist.hasNext()) {
					TagCompound ent = (TagCompound) tlist.getNextTag();
					try {
						TileEntity tile = TileEntity.loadTileEntity(chunk, ent);
						chunk.tileEntitys.put(tile.getPositionInChunk(), tile);
					} catch (Exception e) {
						Logger.getLogger("VC4").warning("Failed to load tile entity " + world.getTileEntityName(ent.getShort("id")), e);
					}
				}
			}
			if (root.hasKey("areas")) {
				TagList tlist = root.getListTag("areas");
				while (tlist.hasNext()) {
					TagCompound ent = (TagCompound) tlist.getNextTag();
					try {
						Area tile = Area.loadArea(world, ent);
						chunk.areas.add(tile);
					} catch (Exception e) {
						Logger.getLogger("VC4").warning("Failed to load area " + world.getTileEntityName(ent.getShort("id")), e);
					}
				}
			}
			if (root.hasKey("bluds")) {
				TagList bluds = root.getListTag("bluds");
				loadBlockUpdateTag(chunk, bluds);
			}
			for (int d = 0; d < 8; ++d) {
				BlockStore store = chunk.getBlockStore(d);
				int bt = in.readByte();
				if (bt != 0) store.blocks = new short[4096];
				if (bt == 1) {
					Arrays.fill(store.getBlocks(), in.readShort());
				} else if (bt == 2) {
					for (int a = 0; a < 4096; ++a) {
						store.blocks[a] = (short) in.readShort();
					}
				}
				int dt = in.readByte();
				if (dt != 0) store.data = new byte[4096];
				if (dt == 1) {
					Arrays.fill(store.getData(), in.readByte());
				} else if (dt == 2) {
					for (int a = 0; a < 4096; ++a) {
						store.data[a] = in.readByte();
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger(VCH4SaveFormat.class).severe("Failed to read chunk", e);
			return null;
		}
		return chunk;
	}

	@Override
	public void writeNetworkBytes(Chunk chunk, SwitchOutputStream out) throws IOException {
		out.startDeflateMode();
		ImplChunk c = (ImplChunk) chunk;
		int idat;
		for (int d = 0; d < 8; ++d) {
			BlockStore store = c.getBlockStore(d);
			int dinfo = 0;
			if (store.blocks != null) dinfo |= 1;
			if (store.data != null) dinfo |= 2;
			out.writeByte((byte) dinfo);
			if (store.blocks != null && store.data != null) {
				for (int i = 0; i < 4096; ++i) {
					idat = store.blocks[i] + (store.data[i] << 11);
					out.writeShort(idat);
				}
			} else if (store.blocks != null) {
				for (int i = 0; i < 4096; ++i) {
					out.writeShort(store.blocks[i]);
				}
			}
		}
		out.endDeflateMode();
	}

	@Override
	public Chunk readNetworkBytes(World world, long x, long y, long z, SwitchInputStream in) throws IOException {
		ImplChunk c = new ImplChunk((ImplWorld) world, ChunkPos.create(x, y, z));
		in.readDeflated();
		short idat;
		for (int d = 0; d < 8; ++d) {
			BlockStore s = c.getBlockStore(d);
			int dinfo = in.readByte();
			if ((dinfo & 1) != 0) s.blocks = new short[4096];
			if ((dinfo & 2) != 0) s.data = new byte[4096];
			if ((dinfo & 3) == 3) {
				for (int i = 0; i < 4096; ++i) {
					idat = in.readShort();
					s.blocks[i] = (short) (idat & 2047);
					s.data[i] = (byte) (idat >> 11);
				}
			} else if ((dinfo & 1) == 1) {
				for (int i = 0; i < 4096; ++i) {
					s.blocks[i] = in.readShort();
				}
			}
		}
		c.setPopulated(true);
		return c;
	}

}
