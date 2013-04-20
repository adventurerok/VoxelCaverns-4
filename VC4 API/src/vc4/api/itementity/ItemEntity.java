package vc4.api.itementity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.jnbt.CompoundTag;
import org.jnbt.ShortTag;

public abstract class ItemEntity {

	
	public static HashMap<Short, Class<? extends ItemEntity>> types = new HashMap<Short, Class<? extends ItemEntity>>();
	
	public abstract void writeAdditionalData(CompoundTag tag) throws IOException;
	public abstract void readAdditionalData(CompoundTag tag) throws IOException;
	public abstract short getId();
	
	public boolean canCombine(ItemEntity entity){
		return false;
	}
	@Override
	public abstract ItemEntity clone();
	
	public static void writeItemEntity(ItemEntity e, CompoundTag tag) throws IOException{
		tag.addTag(new ShortTag("id", e.getId()));
		e.writeAdditionalData(tag);
	}
	public static ItemEntity readItemEntity(CompoundTag tag) throws IOException{
		try {
			ItemEntity e = types.get(tag.getShortTag("id").getValue()).getConstructor().newInstance();
			e.readAdditionalData(tag);
			return e;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void registerEntity(short id, Class<? extends ItemEntity> c){
		types.put(id, c);
	}
	
	public static void initClass(){}
	
	static{
		registerEntity((short)0, ItemEntityEnchantment.class);
	}
}
