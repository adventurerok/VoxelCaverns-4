package vc4.api.vbt;

/*
 * JNBT License
 * 
 * Copyright (c) 2010 Graham Edgecombe All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * * Neither the name of the JNBT team nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;
import vc4.api.util.AABB;
import vc4.api.vector.*;

/**
 * The <code>TAG_Compound</code> tag.
 * 
 * @author Graham Edgecombe
 * 
 */
public final class TagCompound extends Tag {

	@SuppressWarnings("resource")
	public static TagCompound readFrom(BitInputStream in) throws IOException {
		return (TagCompound) new NBTInputStream(in).readTag();
	}

	/**
	 * The value.
	 */
	private Map<String, Tag> value;

	public TagCompound(String name) {
		this(name, new HashMap<String, Tag>());
	}

	/**
	 * Creates the tag.
	 * 
	 * @param name
	 *            The name.
	 * @param value
	 *            The value.
	 */
	public TagCompound(String name, Map<String, Tag> value) {
		super(name);
		this.value = value;
	}

	public void addTag(Tag t) {
		value.put(t.getName(), t);
	}

	public boolean getBoolean(String name) {
		return getBooleanTag(name).getValue();
	}

	public boolean getBoolean(String name, boolean def) {
		try {
			return getBooleanTag(name).getValue();
		} catch (Exception e) {
			setBoolean(name, def);
		}
		return def;
	}

	public TagBoolean getBooleanTag(String name) {
		return (TagBoolean) getTag(name);
	}

	public TagByteArray getByteArrayTag(String name) {
		return (TagByteArray) getTag(name);
	}

	public TagByte getByteTag(String name) {
		return (TagByte) getTag(name);
	}

	public TagCompound getCompoundTag(String name) {
		return (TagCompound) getTag(name);
	}

	public double getDouble(String name) {
		return getDoubleTag(name).getValue();
	}

	public byte getByte(String name) {
		return getByteTag(name).getValue();
	}

	public TagDouble getDoubleTag(String name) {
		return (TagDouble) getTag(name);
	}

	public TagEByte getEByteTag(String name) {
		return (TagEByte) getTag(name);
	}

	public TagEIntArray getEIntArrayTag(String name) {
		return (TagEIntArray) getTag(name);
	}

	public TagEInt getEIntTag(String name) {
		return (TagEInt) getTag(name);
	}

	public TagEShortArray getEShortArrayTag(String name) {
		return (TagEShortArray) getTag(name);
	}

	public TagEShort getEShortTag(String name) {
		return (TagEShort) getTag(name);
	}

	public float getFloat(String name) {
		return getFloatTag(name).getValue();
	}

	public TagFloat getFloatTag(String name) {
		return (TagFloat) getTag(name);
	}

	public int getInt(String name) {
		return getIntTag(name).getValue();
	}

	public int getInt(String name, int def) {
		try {
			return getIntTag(name).getValue();
		} catch (Exception e) {
			setInt(name, def);
		}
		return def;
	}

	public TagIntArray getIntArrayTag(String name) {
		return (TagIntArray) getTag(name);
	}

	public TagInt getIntTag(String name) {
		return (TagInt) getTag(name);
	}

	public TagList getListTag(String name) {
		return (TagList) getTag(name);
	}

	public long getLong(String name) {
		return getLongTag(name).getValue();
	}

	public TagLongArray getLongArrayTag(String name) {
		return (TagLongArray) getTag(name);
	}

	public TagLong getLongTag(String name) {
		return (TagLong) getTag(name);
	}

	public TagNibble getNibbleTag(String name) {
		return (TagNibble) getTag(name);
	}

	public short getShort(String name) {
		return getShortTag(name).getValue();
	}

	public short getShort(String name, short def) {
		try {
			return getShortTag(name).getValue();
		} catch (Exception e) {
			setShort(name, def);
			return def;
		}
	}

	public TagShortArray getShortArrayTag(String name) {
		return (TagShortArray) getTag(name);
	}

	public TagShort getShortTag(String name) {
		return (TagShort) getTag(name);
	}

	public String getString(String name) {
		return getStringTag(name).getValue();
	}

	public String getString(String name, String def) {
		try {
			return getStringTag(name).getValue();
		} catch (Exception e) {
			setString(name, def);
		}
		return def;
	}

	public TagString getStringTag(String name) {
		return (TagString) getTag(name);
	}

	public Tag getTag(String name) {
		return value.get(name);
	}

	@Override
	public Map<String, Tag> getValue() {
		return value;
	}

	public boolean hasKey(String key) {
		return value.containsKey(key);
	}

	public void remove(String name) {
		value.remove(name);
	}

	public void setBoolean(String name, boolean b) {
		addTag(new TagBoolean(name, b));
	}

	public void setDouble(String name, double i) {
		addTag(new TagDouble(name, i));
	}

	public void setInt(String name, int i) {
		addTag(new TagInt(name, i));
	}

	public void setLong(String name, long value) {
		addTag(new TagLong(name, value));
	}

	public void setShort(String name, short i) {
		addTag(new TagShort(name, i));
	}

	public void setString(String name, String s) {
		addTag(new TagString(name, s));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object o) {
		if (o instanceof Map) value = (Map<String, Tag>) o;

	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		StringBuilder bldr = new StringBuilder();
		bldr.append("TAG_Compound" + append + ": " + value.size() + " entries\n[\n");
		for (Map.Entry<String, Tag> entry : value.entrySet()) {
			bldr.append("   " + entry.getValue().toString().replaceAll("\n", "\n   ") + "\n");
		}
		bldr.append("]");
		return bldr.toString();
	}

	@SuppressWarnings("resource")
	public void writeTo(BitOutputStream out) throws IOException {
		// Not closed because this is useful for entitys
		new NBTOutputStream(out).writeTag(this);
	}

	public byte getNibble(String name) {
		return getNibbleTag(name).getValue();
	}

	public byte getNibble(String name, byte def) {
		try {
			return getNibbleTag(name).getValue();
		} catch (Exception e) {
			setNibble(name, def);
			return def;
		}
	}

	public void setNibble(String name, byte def) {
		addTag(new TagNibble(name, def));
	}

	public void setByte(String name, int b) {
		addTag(new TagByte(name, (byte) b));
	}

	public static TagCompound createVector3dTag(String name, Vector3d vect) {
		TagCompound tag = new TagCompound(name);
		tag.setDouble("x", vect.x);
		tag.setDouble("y", vect.y);
		tag.setDouble("z", vect.z);
		return tag;
	}

	public static TagCompound createAABBTag(String name, AABB bb) {
		TagCompound tag = new TagCompound(name);
		tag.setDouble("mix", bb.minX);
		tag.setDouble("miy", bb.minY);
		tag.setDouble("miz", bb.minZ);
		tag.setDouble("max", bb.maxX);
		tag.setDouble("may", bb.maxY);
		tag.setDouble("maz", bb.maxZ);
		return tag;
	}

	public AABB readAABB() {
		AABB res = AABB.getBoundingBox(0, 0, 0, 0, 0, 0);
		res.minX = getDouble("mix");
		res.minY = getDouble("miy");
		res.minZ = getDouble("miz");
		res.maxX = getDouble("max");
		res.maxY = getDouble("may");
		res.maxZ = getDouble("maz");
		return res;
	}

	public Vector3d readVector3d() {
		Vector3d res = new Vector3d();
		res.x = getDouble("x");
		res.y = getDouble("y");
		res.z = getDouble("z");
		return res;
	}

	public Vector3l readVector3l() {
		Vector3l res = new Vector3l();
		res.x = getLong("x");
		res.y = getLong("y");
		res.z = getLong("z");
		return res;
	}

	public Vector3i readVector3i() {
		Vector3i res = new Vector3i();
		res.x = getInt("x");
		res.y = getInt("y");
		res.z = getInt("z");
		return res;
	}

	public static TagCompound createVector3lTag(String name, Vector3l vect) {
		TagCompound tag = new TagCompound(name);
		tag.setLong("x", vect.x);
		tag.setLong("y", vect.y);
		tag.setLong("z", vect.z);
		return tag;
	}

	public static TagCompound createVector3iTag(String name, Vector3i vect) {
		TagCompound tag = new TagCompound(name);
		tag.setInt("x", vect.x);
		tag.setInt("y", vect.y);
		tag.setInt("z", vect.z);
		return tag;
	}

	public double getDouble(String name, double def) {
		try {
			return getDoubleTag(name).getValue();
		} catch (Exception e) {
			setDouble(name, def);
			return def;
		}
	}

	public byte getByte(String name, int def) {
		try {
			return getByteTag(name).getValue();
		} catch (Exception e) {
			setByte(name, (byte) def);
			return (byte) def;
		}
	}

}
