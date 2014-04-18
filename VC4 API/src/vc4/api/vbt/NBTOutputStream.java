package vc4.api.vbt;

import java.io.*;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import vc4.api.io.BitOutput;
import vc4.api.io.BitOutputStream;

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

/**
 * <p>
 * This class writes <strong>NBT</strong>, or <strong>Named Binary Tag</strong> <code>Tag</code> objects to an underlying <code>OutputStream</code>.
 * </p>
 * 
 * <p>
 * The NBT format was created by Markus Persson, and the specification may be found at <a href="http://www.minecraft.net/docs/NBT.txt"> http://www.minecraft.net/docs/NBT.txt</a>.
 * </p>
 * 
 * @author Graham Edgecombe
 * 
 */
public final class NBTOutputStream implements Closeable {

	/**
	 * The output stream.
	 */
	private final BitOutput os;

	public NBTOutputStream(BitOutput os) {
		this.os = os;
	}

	/**
	 * Creates a new <code>NBTOutputStream</code>, which will write data to the specified underlying output stream.
	 * 
	 * @param os
	 *            The output stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public NBTOutputStream(OutputStream os) throws IOException {
		this.os = new BitOutputStream(new GZIPOutputStream(os));
	}

	public NBTOutputStream(OutputStream os, boolean zip) throws IOException {
		if (zip) this.os = new BitOutputStream(new GZIPOutputStream(os));
		else this.os = new BitOutputStream(os);
	}

	@Override
	public void close() throws IOException {
		os.close();
	}

	private void writeBooleanTagPayload(TagBoolean tag) throws IOException {
		os.writeBoolean(tag.getValue());

	}

	/**
	 * Writes a <code>TAG_Byte_Array</code> tag.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeByteArrayTagPayload(TagByteArray tag) throws IOException {
		byte[] bytes = tag.getValue();
		os.writeInt(bytes.length);
		os.writeBytes(bytes);
	}

	/**
	 * Writes a <code>TAG_Byte</code> tag.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeByteTagPayload(TagByte tag) throws IOException {
		os.writeByte(tag.getValue());
	}

	/**
	 * Writes a <code>TAG_Compound</code> tag.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeCompoundTagPayload(TagCompound tag) throws IOException {
		for (Tag childTag : tag.getValue().values()) {
			writeTag(childTag);
		}
		os.writeBits((byte) 0, (short) 8);
	}

	/**
	 * Writes a <code>TAG_Double</code> tag.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeDoubleTagPayload(TagDouble tag) throws IOException {
		os.writeDouble(tag.getValue());
	}

	private void writeEByteTagPayLoad(TagEByte tag) throws IOException {
		os.writeBits(tag.getValue(), (short) 12);

	}

	private void writeEIntArrayTagPayload(TagEIntArray tag) throws IOException {
		long[] bytes = tag.getValue();
		os.writeInt(bytes.length);
		for (int d = 0; d < bytes.length; ++d)
			os.writeBits(bytes[d], (short) 48);
	}

	private void writeEIntTagPayLoad(TagEInt tag) throws IOException {
		os.writeBits(tag.getValue(), (short) 48);

	}

	/**
	 * Writes a <code>TAG_Empty</code> tag.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeEndTagPayload(TagEnd tag) {
		/* empty */
	}

	private void writeEShortArrayTagPayload(TagEShortArray tag) throws IOException {
		int[] bytes = tag.getValue();
		os.writeInt(bytes.length);
		for (int d = 0; d < bytes.length; ++d)
			os.writeBits(bytes[d], (short) 24);
	}

	private void writeEShortTagPayLoad(TagEShort tag) throws IOException {
		os.writeBits(tag.getValue(), (short) 24);

	}

	/**
	 * Writes a <code>TAG_Float</code> tag.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeFloatTagPayload(TagFloat tag) throws IOException {
		os.writeFloat(tag.getValue());
	}

	private void writeIntArrayTagPayload(TagIntArray tag) throws IOException {
		int[] bytes = tag.getValue();
		os.writeInt(bytes.length);
		for (int d = 0; d < bytes.length; ++d)
			os.writeInt(bytes[d]);
	}

	/**
	 * Writes a <code>TAG_Int</code> tag.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeIntTagPayload(TagInt tag) throws IOException {
		os.writeInt(tag.getValue());
	}

	/**
	 * Writes a <code>TAG_List</code> tag.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeListTagPayload(TagList tag) throws IOException {
		Class<? extends Tag> clazz = tag.getType();
		List<Tag> tags = tag.getValue();
		int size = tags.size();

		os.writeByte((byte) NBTUtils.getTypeCode(clazz));
		os.writeInt(size);
		for (int i = 0; i < size; i++) {
			writeTagPayload(tags.get(i));
		}
	}

	private void writeLongArrayTagPayload(TagLongArray tag) throws IOException {
		long[] bytes = tag.getValue();
		os.writeInt(bytes.length);
		for (int d = 0; d < bytes.length; ++d)
			os.writeLong(bytes[d]);
	}

	/**
	 * Writes a <code>TAG_Long</code> tag.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeLongTagPayload(TagLong tag) throws IOException {
		os.writeLong(tag.getValue());
	}

	private void writeNibbleTagPayload(TagNibble tag) throws IOException {
		os.writeBits(tag.getValue(), (short) 4);

	}

	private void writeShortArrayTagPayload(TagShortArray tag) throws IOException {
		short[] bytes = tag.getValue();
		os.writeInt(bytes.length);
		for (int d = 0; d < bytes.length; ++d)
			os.writeShort(bytes[d]);
	}

	/**
	 * Writes a <code>TAG_Short</code> tag.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeShortTagPayload(TagShort tag) throws IOException {
		os.writeShort(tag.getValue());
	}

	/**
	 * Writes a <code>TAG_String</code> tag.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeStringTagPayload(TagString tag) throws IOException {
		byte[] bytes = tag.getValue().getBytes(NBTConstants.CHARSET);
		os.writeShort((short) bytes.length);
		os.writeBytes(bytes);
	}

	/**
	 * Writes a tag.
	 * 
	 * @param tag
	 *            The tag to write.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void writeTag(Tag tag) throws IOException {
		int type = NBTUtils.getTypeCode(tag.getClass());
		String name = tag.getName();
		byte[] nameBytes = name.getBytes(NBTConstants.CHARSET);

		os.writeBits((byte) type, (short) 8);
		os.writeShort((short) nameBytes.length);
		os.writeBytes(nameBytes);

		if (type == NBTConstants.TYPE_END) { throw new IOException("Named TAG_End not permitted."); }

		writeTagPayload(tag);
	}

	/**
	 * Writes tag payload.
	 * 
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void writeTagPayload(Tag tag) throws IOException {
		int type = NBTUtils.getTypeCode(tag.getClass());
		switch (type) {
			case NBTConstants.TYPE_END:
				writeEndTagPayload((TagEnd) tag);
				break;
			case NBTConstants.TYPE_BYTE:
				writeByteTagPayload((TagByte) tag);
				break;
			case NBTConstants.TYPE_SHORT:
				writeShortTagPayload((TagShort) tag);
				break;
			case NBTConstants.TYPE_INT:
				writeIntTagPayload((TagInt) tag);
				break;
			case NBTConstants.TYPE_LONG:
				writeLongTagPayload((TagLong) tag);
				break;
			case NBTConstants.TYPE_FLOAT:
				writeFloatTagPayload((TagFloat) tag);
				break;
			case NBTConstants.TYPE_DOUBLE:
				writeDoubleTagPayload((TagDouble) tag);
				break;
			case NBTConstants.TYPE_BYTE_ARRAY:
				writeByteArrayTagPayload((TagByteArray) tag);
				break;
			case NBTConstants.TYPE_STRING:
				writeStringTagPayload((TagString) tag);
				break;
			case NBTConstants.TYPE_LIST:
				writeListTagPayload((TagList) tag);
				break;
			case NBTConstants.TYPE_COMPOUND:
				writeCompoundTagPayload((TagCompound) tag);
				break;
			case NBTConstants.TYPE_BOOLEAN:
				writeBooleanTagPayload((TagBoolean) tag);
				break;
			case NBTConstants.TYPE_NIBBLE:
				writeNibbleTagPayload((TagNibble) tag);
				break;
			case NBTConstants.TYPE_EBYTE:
				writeEByteTagPayLoad((TagEByte) tag);
				break;
			case NBTConstants.TYPE_ESHORT:
				writeEShortTagPayLoad((TagEShort) tag);
				break;
			case NBTConstants.TYPE_EINT:
				writeEIntTagPayLoad((TagEInt) tag);
				break;
			case NBTConstants.TYPE_SHORT_ARRAY:
				writeShortArrayTagPayload((TagShortArray) tag);
				break;
			case NBTConstants.TYPE_INT_ARRAY:
				writeIntArrayTagPayload((TagIntArray) tag);
				break;
			case NBTConstants.TYPE_LONG_ARRAY:
				writeLongArrayTagPayload((TagLongArray) tag);
				break;
			case NBTConstants.TYPE_ESHORT_ARRAY:
				writeEShortArrayTagPayload((TagEShortArray) tag);
				break;
			case NBTConstants.TYPE_EINT_ARRAY:
				writeEIntArrayTagPayload((TagEIntArray) tag);
				break;
			default:
				throw new IOException("Invalid tag type: " + type + ".");
		}
	}

}
