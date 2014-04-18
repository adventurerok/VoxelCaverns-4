package vc4.api.tileentity;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import vc4.api.entity.Entity;
import vc4.api.logging.Logger;
import vc4.api.vbt.TagCompound;
import vc4.api.vector.Vector3i;
import vc4.api.vector.Vector3l;
import vc4.api.world.*;

public abstract class TileEntity {

	public Vector3l position;
	public World world;
	public boolean remove = false;

	private static HashMap<String, Constructor<? extends TileEntity>> types = new HashMap<>();

	public static void registerEntity(String name, Class<? extends TileEntity> clz) {
		try {
			types.put(name, clz.getConstructor(World.class));
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.getLogger(Entity.class).warning("TileEntity class does not have correct constructor", e);
		}
	}

	public static Constructor<? extends TileEntity> getTileEntityType(String name) {
		return types.get(name);
	}

	public static TileEntity loadTileEntity(Chunk chunk, TagCompound tag) {
		short id = tag.getShort("id");
		String name = chunk.getWorld().getTileEntityName(id);
		Constructor<? extends TileEntity> clz = getTileEntityType(name);
		try {
			TileEntity e = clz.newInstance(chunk.getWorld());
			e.position = new Vector3l();
			TagCompound pos = tag.getCompoundTag("pos");
			e.position.x = chunk.getChunkPos().worldX(pos.getByteTag("x").getValue());
			e.position.y = chunk.getChunkPos().worldY(pos.getByteTag("y").getValue());
			e.position.z = chunk.getChunkPos().worldZ(pos.getByteTag("z").getValue());
			e.loadSaveCompound(tag);
			return e;
		} catch (Exception e) {
			Logger.getLogger(Entity.class).warning("Exception while loading tileentity: " + name, e);
		}
		return null;
	}

	public boolean persistent() {
		return !remove;
	}

	public TileEntity(World world, Vector3l pos) {
		this.world = world;
		position = pos;
	}

	public TileEntity(World world) {
		super();
		this.world = world;
	}

	public void addToWorld() {
		world.setTileEntity(position.x, position.y, position.z, this);
	}

	public void moveTileEntity(Vector3l n) {
		world.setTileEntity(position.x, position.y, position.z, null);
		world.setTileEntity(n.x, n.y, n.z, this);
		position = n;
	}

	public Chunk getChunk() {
		return world.getChunk(ChunkPos.createFromWorldPos(position));
	}

	public Vector3i getPositionInChunk() {
		return new Vector3i((int) (position.x & 31), (int) (position.y & 31), (int) (position.z & 31));
	}

	public short getId() {
		return world.getRegisteredTileEntity(getName());
	}

	public abstract String getName();

	public void updateTick() {

	}

	public void draw() {

	}

	public void setChunkRedraw() {
		world.setDirty(position.x, position.y, position.z);
	}

	static {
		// registerEntity((short) 1, TileEntityChest.class);
		// registerEntity((short) 2, TileEntityFurnace.class);
	}

	static class Entry<K, V> implements Map.Entry<K, V> {
		final K key;
		V value;

		Entry(K key, V value) {
			this.value = value;
			this.key = key;
		}

		@Override
		public final K getKey() {
			return this.key;
		}

		@Override
		public final V getValue() {
			return this.value;
		}

		@Override
		public final V setValue(V paramV) {
			V localObject = this.value;
			this.value = paramV;
			return localObject;
		}

		@Override
		public final boolean equals(Object paramObject) {
			if (!(paramObject instanceof Map.Entry)) return false;
			@SuppressWarnings("rawtypes")
			Map.Entry localEntry = (Map.Entry) paramObject;
			Object localObject1 = getKey();
			Object localObject2 = localEntry.getKey();
			if ((localObject1 == localObject2) || ((localObject1 != null) && (localObject1.equals(localObject2)))) {
				Object localObject3 = getValue();
				Object localObject4 = localEntry.getValue();
				if ((localObject3 == localObject4) || ((localObject3 != null) && (localObject3.equals(localObject4)))) return true;
			}
			return false;
		}

		@Override
		public final int hashCode() {
			return (((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode()));
		}

		@Override
		public final String toString() {
			return getKey() + "=" + getValue();
		}

		void recordAccess(HashMap<K, V> paramHashMap) {
		}

		void recordRemoval(HashMap<K, V> paramHashMap) {
		}
	}

	public static void initClass() {
	}

	public void setUnsavedChanges() {
		try {
			getChunk().setModified(true);
		} catch (Exception e) {
		}
	}

	public void loadSaveCompound(TagCompound tag) {

	}

	public TagCompound getSaveCompound() {
		TagCompound root = new TagCompound("root");
		root.setShort("id", getId());
		TagCompound pos = new TagCompound("pos");
		pos.setByte("x", (byte) (position.x & 31));
		pos.setByte("y", (byte) (position.y & 31));
		pos.setByte("z", (byte) (position.z & 31));
		root.addTag(pos);
		return root;
	}

}
