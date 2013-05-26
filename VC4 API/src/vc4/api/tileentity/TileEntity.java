package vc4.api.tileentity;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;
import vc4.api.vector.Vector3i;
import vc4.api.vector.Vector3l;
import vc4.api.world.*;

public abstract class TileEntity {

	public Vector3l position;
	public World world;
	public boolean remove = false;

	public static HashMap<Short, Class<? extends TileEntity>> types = new HashMap<Short, Class<? extends TileEntity>>();

	public TileEntity(World world, Vector3l pos){
		this.world = world;
		position = pos;
		if(pos.x != Long.MAX_VALUE)world.setTileEntity(pos.x, pos.y, pos.z, this);
	}

	public void moveTileEntity(Vector3l n){
		world.setTileEntity(position.x, position.y, position.z, null);
		world.setTileEntity(n.x, n.y, n.z, this);
		position = n;
	}
	
	public Chunk getChunk(){
		return world.getChunk(ChunkPos.createFromWorldPos(position));
	}
	
	public Vector3i getPositionInChunk(){
		return new Vector3i((int)(position.x & 31), (int)(position.y & 31), (int)(position.z & 31));
	}

	public abstract short getId();

	public void updateTick(){

	}
	public abstract void writeAdditionalData(BitOutputStream out) throws IOException;
	public abstract void readAdditionalData(BitInputStream in) throws IOException;
	public void draw(){

	}
	
	public void setChunkRedraw(){
		//world.setChunkBlockDirty(position.x, position.y, position.z);
	}

	public static void writeTileEntity(TileEntity e, BitOutputStream out){
		Vector3i p = new Vector3i((int)(e.position.x & 31), (int)(e.position.y & 31), (int) (e.position.z & 31));
		try{
			out.writeShort(e.getId());
			out.writeByte((byte) p.x);
			out.writeByte((byte) p.y);
			out.writeByte((byte) p.z);
			e.writeAdditionalData(out);
		} catch(IOException exc){
			exc.printStackTrace();
		}
	}

	public static Map.Entry<Vector3i, TileEntity> readTileEntity(BitInputStream in, World world, ChunkPos c){
		try{
			short id = in.readShort();
			Vector3i cpos = new Vector3i();
			cpos.x = in.readByte();
			cpos.y = in.readByte();
			cpos.z = in.readByte();
			Vector3l pos = new Vector3l();
			pos.x = c.worldX(cpos.x);
			pos.y = c.worldY(cpos.y);
			pos.z = c.worldZ(cpos.z);
			TileEntity e;
			try{
				Class<? extends TileEntity> ecls = types.get(id);
				Constructor<? extends TileEntity> econs = ecls.getConstructor(World.class, Vector3l.class);
				e = econs.newInstance(world, new Vector3l(Long.MAX_VALUE, 0, 0));
			} catch(NullPointerException exc){
				throw new RuntimeException("TileEntity ID " + id + " Exception", exc);
			}
			e.position = pos;
			e.readAdditionalData(in);
			return new Entry<Vector3i, TileEntity>(cpos, e);
		} catch(IOException e){
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}


	public static void registerEntity(short id, Class<? extends TileEntity> c){
		types.put(id, c);
	}
	
	static{
//		registerEntity((short) 1, TileEntityChest.class);
//		registerEntity((short) 2, TileEntityFurnace.class);
	}

	static class Entry<K, V>
	implements Map.Entry<K, V>
	{
		final K key;
		V value;

		Entry(K key, V value)
		{
			this.value = value;
			this.key = key;
		}

		@Override
		public final K getKey()
		{
			return this.key;
		}

		@Override
		public final V getValue()
		{
			return this.value;
		}

		@Override
		public final V setValue(V paramV)
		{
			V localObject = this.value;
			this.value = paramV;
			return localObject;
		}

		@Override
		public final boolean equals(Object paramObject)
		{
			if (!(paramObject instanceof Map.Entry))
				return false;
			@SuppressWarnings("rawtypes")
			Map.Entry localEntry = (Map.Entry)paramObject;
			Object localObject1 = getKey();
			Object localObject2 = localEntry.getKey();
			if ((localObject1 == localObject2) || ((localObject1 != null) && (localObject1.equals(localObject2))))
			{
				Object localObject3 = getValue();
				Object localObject4 = localEntry.getValue();
				if ((localObject3 == localObject4) || ((localObject3 != null) && (localObject3.equals(localObject4))))
					return true;
			}
			return false;
		}

		@Override
		public final int hashCode()
		{
			return (((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode()));
		}

		@Override
		public final String toString()
		{
			return getKey() + "=" + getValue();
		}

		void recordAccess(HashMap<K, V> paramHashMap)
		{
		}

		void recordRemoval(HashMap<K, V> paramHashMap)
		{
		}
	}
	
	public static void initClass(){}
	
	public void setUnsavedChanges(){
//		try{
//			getChunk().setUnsavedChanges(true);
//		} catch(NullPointerException e){}
	}

}
