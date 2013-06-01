package org.jnbt;

/*
 * JNBT License
 * 
 * Copyright (c) 2010 Graham Edgecombe
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *       
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *       
 *     * Neither the name of the JNBT team nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;

/**
 * The <code>TAG_Compound</code> tag.
 * @author Graham Edgecombe
 *
 */
public final class CompoundTag extends Tag {
	
	@SuppressWarnings("resource")
	public static CompoundTag readFrom(BitInputStream in) throws IOException{
		return (CompoundTag) new NBTInputStream(in).readTag();
	}
	
	/**
	 * The value.
	 */
	private Map<String, Tag> value;
	
	public CompoundTag(String name){
		this(name, new HashMap<String, Tag>());
	}
	
	/**
	 * Creates the tag.
	 * @param name The name.
	 * @param value The value.
	 */
	public CompoundTag(String name, Map<String, Tag> value) {
		super(name);
		this.value = value;
	}
	public void addTag(Tag t){
		value.put(t.getName(), t);
	}
	public boolean getBoolean(String name){
		return getBooleanTag(name).getValue();
	}
	public boolean getBoolean(String name, boolean def){
		try{
			return getBooleanTag(name).getValue();
		} catch(Exception e){
			setBoolean(name, def);
		}
		return def;
	}
	public BooleanTag getBooleanTag(String name){
		return (BooleanTag) getTag(name);
	}
	public ByteArrayTag getByteArrayTag(String name){
		return (ByteArrayTag) getTag(name);
	}
	public ByteTag getByteTag(String name){
		return (ByteTag) getTag(name);
	}
	public CompoundTag getCompoundTag(String name){
		return (CompoundTag) getTag(name);
	}
	public double getDouble(String name){
		return getDoubleTag(name).getValue();
	}
	public byte getByte(String name){
		return getByteTag(name).getValue();
	}
	public DoubleTag getDoubleTag(String name){
		return (DoubleTag) getTag(name);
	}
	public EByteTag getEByteTag(String name){
		return (EByteTag) getTag(name);
	}
	public EIntArrayTag getEIntArrayTag(String name){
		return (EIntArrayTag) getTag(name);
	}
	public EIntTag getEIntTag(String name){
		return (EIntTag) getTag(name);
	}
	public EShortArrayTag getEShortArrayTag(String name){
		return (EShortArrayTag) getTag(name);
	}
	public EShortTag getEShortTag(String name){
		return (EShortTag) getTag(name);
	}
	public float getFloat(String name){
		return getFloatTag(name).getValue();
	}
	public FloatTag getFloatTag(String name){
		return (FloatTag) getTag(name);
	}
	public int getInt(String name){
		return getIntTag(name).getValue();
	}
	public int getInt(String name, int def){
		try{
			return getIntTag(name).getValue();
		} catch(Exception e){
			setInt(name, def);
		}
		return def;
	}
	public IntArrayTag getIntArrayTag(String name){
		return (IntArrayTag) getTag(name);
	}
	public IntTag getIntTag(String name){
		return (IntTag) getTag(name);
	}
	public ListTag getListTag(String name){
		return (ListTag) getTag(name);
	}
	public long getLong(String name){
		return getLongTag(name).getValue();
	}
	public LongArrayTag getLongArrayTag(String name){
		return (LongArrayTag) getTag(name);
	}
	public LongTag getLongTag(String name){
		return (LongTag) getTag(name);
	}
	public NibbleTag getNibbleTag(String name){
		return (NibbleTag) getTag(name);
	}
	public short getShort(String name){
		return getShortTag(name).getValue();
	}
	public short getShort(String name, short def){
		try{
			return getShortTag(name).getValue();
		} catch(Exception e){
			setShort(name, def);
			return def;
		}
	}
	public ShortArrayTag getShortArrayTag(String name){
		return (ShortArrayTag) getTag(name);
	}
	public ShortTag getShortTag(String name){
		return (ShortTag) getTag(name);
	}
	public String getString(String name){
		return getStringTag(name).getValue();
	}
	public String getString(String name, String def){
		try{
			return getStringTag(name).getValue();
		} catch(Exception e){
			setString(name, def);
		}
		return def;
	}
	public StringTag getStringTag(String name){
		return (StringTag) getTag(name);
	}
	public Tag getTag(String name){
		return value.get(name);
	}
	@Override
	public Map<String, Tag> getValue() {
		return value;
	}
	public boolean hasKey(String key){
		return value.containsKey(key);
	}
	public void remove(String name){
		value.remove(name);
	}

	public void setBoolean(String name, boolean b){
		addTag(new BooleanTag(name, b));
	}
	
	public void setDouble(String name, double i){
		addTag(new DoubleTag(name, i));
	}
	
	public void setInt(String name, int i){
		addTag(new IntTag(name, i));
	}
	
	public void setLong(String name, long value) {
		addTag(new LongTag(name, value));
	}
	
	public void setShort(String name, short i){
		addTag(new ShortTag(name, i));
	}

	public void setString(String name, String s) {
		addTag(new StringTag(name, s));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object o) {
		if(o instanceof Map) value = (Map<String, Tag>) o;
		
	}
	
	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if(name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		StringBuilder bldr = new StringBuilder();
		bldr.append("TAG_Compound" + append + ": " + value.size() + " entries\r\n{\r\n");
		for(Map.Entry<String, Tag> entry : value.entrySet()) {
			bldr.append("   " + entry.getValue().toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
		}
		bldr.append("}");
		return bldr.toString();
	}
	
	@SuppressWarnings("resource")
	public void writeTo(BitOutputStream out) throws IOException{
		//Not closed because this is useful for entitys
		new NBTOutputStream(out).writeTag(this);
	}
	
	public byte getNibble(String name){
		return getNibbleTag(name).getValue();
	}

	public byte getNibble(String name, byte def) {
		try{
			return getNibbleTag(name).getValue();
		} catch(Exception e){
			setNibble(name, def);
			return def;
		}
	}

	public void setNibble(String name, byte def) {
		addTag(new NibbleTag(name, def));
	}

	public void setByte(String name, byte b) {
		addTag(new ByteTag(name, b));
	}

	public static CompoundTag createVector3dTag(String name, Vector3d vect) {
		CompoundTag tag = new CompoundTag(name);
		tag.setDouble("x", vect.x);
		tag.setDouble("y", vect.y);
		tag.setDouble("z", vect.z);
		return tag;
	}
	
	public Vector3d readVector3d(){
		Vector3d res = new Vector3d();
		res.x = getDouble("x");
		res.y = getDouble("y");
		res.z = getDouble("z");
		return res;
	}
	
	public Vector3l readVector3l(){
		Vector3l res = new Vector3l();
		res.x = getLong("x");
		res.y = getLong("y");
		res.z = getLong("z");
		return res;
	}

	public static CompoundTag createVector3lTag(String name, Vector3l vect) {
		CompoundTag tag = new CompoundTag(name);
		tag.setLong("x", vect.x);
		tag.setLong("y", vect.y);
		tag.setLong("z", vect.z);
		return tag;
	}

}
