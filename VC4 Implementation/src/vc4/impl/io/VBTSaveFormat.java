package vc4.impl.io;

import java.io.*;
import java.util.Arrays;

import org.jnbt.*;

import vc4.api.io.*;
import vc4.api.util.DirectoryLocator;
import vc4.api.world.*;
import vc4.impl.world.*;

public class VBTSaveFormat implements SaveFormat {

	public static VBTSaveFormat VBT_SAVE_FORMAT = new VBTSaveFormat();

	@Override
	public Chunk readChunk(World world, long x, long y, long z) throws FileNotFoundException, IOException {
		String path = DirectoryLocator.getPath() + "/worlds/" + world.getSaveName() + "/chunks/" + y + "/" + z + "/" + x + ".vbt";
		File file = new File(path);
		if (!file.exists()) return null;
		//long start = System.nanoTime();
		try (NBTInputStream in = new NBTInputStream(new FileInputStream(file), true)) {
			CompoundTag root = (CompoundTag) in.readTag();

			ImplChunk chunk = new ImplChunk((ImplWorld) world, ChunkPos.create(x, y, z));
			chunk.setPopulated(root.getBoolean("populated", false));
			ListTag bs = root.getListTag("stores");
			int d = 0;
			while (bs.hasNext()) {
				CompoundTag stag = (CompoundTag) bs.getNextTag();
				BlockStore store = chunk.getBlockStore(d++);
				if (stag.hasKey("onlyBlock")) {
					store.setBlocks(new short[4096]);
					Arrays.fill(store.getBlocks(), stag.getShort("onlyBlock"));
				} else if (stag.hasKey("blocks")) {
					byte[] blocks = stag.getByteArrayTag("blocks").getValue();
					store.setBlocks(byteToShort(blocks, (short) 11));
				}
				if (stag.hasKey("onlyData")) {
					store.setData(new byte[4096]);
					Arrays.fill(store.getData(), stag.getByteTag("onlyData").getValue());
				} else if (stag.hasKey("data")) {
					byte[] data = stag.getByteArrayTag("data").getValue();
					store.setData(byteToByte(data, (short) 5));
				}
			}

			//System.out.println("Took " + ((System.nanoTime() - start) / 1000000) + "ms");
			return chunk;
		}
	}

	@Override
	public void writeChunk(Chunk c) throws IOException {
		ImplChunk chunk = (ImplChunk) c;
		String path = DirectoryLocator.getPath() + "/worlds/" + chunk.getWorld().getSaveName() + "/chunks/" + chunk.getChunkPos().y + "/" + chunk.getChunkPos().z + "/" + chunk.getChunkPos().x + ".vbt";
		File file = new File(path);
		file.getParentFile().mkdirs();
		CompoundTag root = new CompoundTag("root");
		root.setBoolean("populated", chunk.isPopulated());
		ListTag bs = new ListTag("stores", CompoundTag.class);
		for (int d = 0; d < 8; ++d) {
			BlockStore store = chunk.getBlockStore(d);
			CompoundTag stag = new CompoundTag("store: " + d);
			if (store.getBlocks() != null) {
				short shor = getOnlyElement(store.getBlocks());
				if (shor != -1) {
					stag.setShort("onlyBlock", shor);
				} else stag.addTag(new ByteArrayTag("blocks", toByteArray(store.getBlocks(), (short) 11)));
			}
			if (store.getData() != null) {
				byte shor = getOnlyElement(store.getData());
				if (shor != -1) {
					stag.addTag(new ByteTag("onlyData", shor));
				} else stag.addTag(new ByteArrayTag("data", toByteArray(store.getData(), (short) 5)));
			}
			bs.addTag(stag);
		}
		root.addTag(bs);
		try (NBTOutputStream out = new NBTOutputStream(new FileOutputStream(path), true)) {
			out.writeTag(root);
		}
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
	public byte[] toByteArray(short[] arr, short bits) throws IOException {
		ByteArrayOutputStream o = new ByteArrayOutputStream(bits * arr.length / 8);
		BitOutputStream bo = new BitOutputStream(o);
		for (int d = 0; d < arr.length; ++d) {
			bo.writeBits(arr[d], bits);
		}
		bo.flush();
		return o.toByteArray();
	}

	@SuppressWarnings("resource")
	public short[] byteToShort(byte[] in, short bits) throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(in);
		BitInputStream bis = new BitInputStream(is);
		short[] res = new short[4096];
		for (int d = 0; d < 4096; ++d) {
			res[d] = (short) bis.readBits(bits);
		}
		return res;
	}

	@SuppressWarnings("resource")
	public byte[] byteToByte(byte[] in, short bits) throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(in);
		BitInputStream bis = new BitInputStream(is);
		byte[] res = new byte[4096];
		for (int d = 0; d < 4096; ++d) {
			res[d] = (byte) bis.readBits(bits);
		}
		return res;
	}

	@SuppressWarnings("resource")
	public byte[] toByteArray(byte[] arr, short bits) throws IOException {
		ByteArrayOutputStream o = new ByteArrayOutputStream(bits * arr.length / 8);
		BitOutputStream bo = new BitOutputStream(o);
		for (int d = 0; d < arr.length; ++d) {
			bo.writeBits(arr[d], bits);
		}
		bo.flush();
		return o.toByteArray();
	}


	@Override
	public void writeMap(World world, MapData map) throws IOException {
		// TASK Auto-generated method stub
		
	}

	@Override
	public MapData readMap(World world, long x, long z) throws IOException {
		// TASK Auto-generated method stub
		return null;
	}

	@Override
	public void writeChunk(Chunk chunk, BitOutputStream out) {
	}

	@Override
	public Chunk readChunk(World world, BitInputStream in) {
		return null;
	}


	@Override
	public void writeNetworkBytes(Chunk chunk, SwitchOutputStream out) throws IOException {
	}

	@Override
	public Chunk readNetworkBytes(World world, long x, long y, long z, SwitchInputStream in) throws IOException {
		return null;
	}

	

}
