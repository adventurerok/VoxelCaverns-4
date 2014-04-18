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

/**
 * A class which contains NBT-related utility methods.
 * 
 * @author Graham Edgecombe
 * 
 */
public final class NBTUtils {

	public static String getSimpleName(Class<? extends Tag> clazz) {
		String name = getTypeName(clazz);
		return name.substring(4).replace("_", " ").replace("E", "Extended ");
	}

	/**
	 * Gets the class of a type of tag.
	 * 
	 * @param type
	 *            The type.
	 * @return The class.
	 * @throws IllegalArgumentException
	 *             if the tag type is invalid.
	 */
	public static Class<? extends Tag> getTypeClass(int type) {
		switch (type) {
			case NBTConstants.TYPE_END:
				return TagEnd.class;
			case NBTConstants.TYPE_BYTE:
				return TagByte.class;
			case NBTConstants.TYPE_SHORT:
				return TagShort.class;
			case NBTConstants.TYPE_INT:
				return TagInt.class;
			case NBTConstants.TYPE_LONG:
				return TagLong.class;
			case NBTConstants.TYPE_FLOAT:
				return TagFloat.class;
			case NBTConstants.TYPE_DOUBLE:
				return TagDouble.class;
			case NBTConstants.TYPE_BYTE_ARRAY:
				return TagByteArray.class;
			case NBTConstants.TYPE_STRING:
				return TagString.class;
			case NBTConstants.TYPE_LIST:
				return TagList.class;
			case NBTConstants.TYPE_COMPOUND:
				return TagCompound.class;
			case NBTConstants.TYPE_BOOLEAN:
				return TagBoolean.class;
			case NBTConstants.TYPE_NIBBLE:
				return TagNibble.class;
			case NBTConstants.TYPE_EBYTE:
				return TagEByte.class;
			case NBTConstants.TYPE_ESHORT:
				return TagEShort.class;
			case NBTConstants.TYPE_EINT:
				return TagEInt.class;
			case NBTConstants.TYPE_SHORT_ARRAY:
				return TagShortArray.class;
			case NBTConstants.TYPE_INT_ARRAY:
				return TagIntArray.class;
			case NBTConstants.TYPE_LONG_ARRAY:
				return TagLongArray.class;
			case NBTConstants.TYPE_ESHORT_ARRAY:
				return TagEShortArray.class;
			case NBTConstants.TYPE_EINT_ARRAY:
				return TagEIntArray.class;
			default:
				throw new IllegalArgumentException("Invalid tag type : " + type + ".");
		}
	}

	/**
	 * Gets the type code of a tag class.
	 * 
	 * @param clazz
	 *            The tag class.
	 * @return The type code.
	 * @throws IllegalArgumentException
	 *             if the tag class is invalid.
	 */
	public static int getTypeCode(Class<? extends Tag> clazz) {
		if (clazz.equals(TagByteArray.class)) {
			return NBTConstants.TYPE_BYTE_ARRAY;
		} else if (clazz.equals(TagByte.class)) {
			return NBTConstants.TYPE_BYTE;
		} else if (clazz.equals(TagCompound.class)) {
			return NBTConstants.TYPE_COMPOUND;
		} else if (clazz.equals(TagDouble.class)) {
			return NBTConstants.TYPE_DOUBLE;
		} else if (clazz.equals(TagEnd.class)) {
			return NBTConstants.TYPE_END;
		} else if (clazz.equals(TagFloat.class)) {
			return NBTConstants.TYPE_FLOAT;
		} else if (clazz.equals(TagInt.class)) {
			return NBTConstants.TYPE_INT;
		} else if (clazz.equals(TagList.class)) {
			return NBTConstants.TYPE_LIST;
		} else if (clazz.equals(TagLong.class)) {
			return NBTConstants.TYPE_LONG;
		} else if (clazz.equals(TagShort.class)) {
			return NBTConstants.TYPE_SHORT;
		} else if (clazz.equals(TagString.class)) {
			return NBTConstants.TYPE_STRING;
		} else if (clazz.equals(TagBoolean.class)) {
			return NBTConstants.TYPE_BOOLEAN;
		} else if (clazz.equals(TagNibble.class)) {
			return NBTConstants.TYPE_NIBBLE;
		} else if (clazz.equals(TagEByte.class)) {
			return NBTConstants.TYPE_EBYTE;
		} else if (clazz.equals(TagEShort.class)) {
			return NBTConstants.TYPE_ESHORT;
		} else if (clazz.equals(TagEInt.class)) {
			return NBTConstants.TYPE_EINT;
		} else if (clazz.equals(TagShortArray.class)) {
			return NBTConstants.TYPE_SHORT_ARRAY;
		} else if (clazz.equals(TagIntArray.class)) {
			return NBTConstants.TYPE_INT_ARRAY;
		} else if (clazz.equals(TagLongArray.class)) {
			return NBTConstants.TYPE_LONG_ARRAY;
		} else if (clazz.equals(TagEShortArray.class)) {
			return NBTConstants.TYPE_ESHORT_ARRAY;
		} else if (clazz.equals(TagEIntArray.class)) {
			return NBTConstants.TYPE_EINT_ARRAY;
		} else {
			throw new IllegalArgumentException("Invalid tag classs (" + clazz.getName() + ").");
		}
	}

	/**
	 * Gets the type name of a tag.
	 * 
	 * @param clazz
	 *            The tag class.
	 * @return The type name.
	 */
	public static String getTypeName(Class<? extends Tag> clazz) {
		if (clazz.equals(TagByteArray.class)) {
			return "TAG_Byte_Array";
		} else if (clazz.equals(TagByte.class)) {
			return "TAG_Byte";
		} else if (clazz.equals(TagCompound.class)) {
			return "TAG_Compound";
		} else if (clazz.equals(TagDouble.class)) {
			return "TAG_Double";
		} else if (clazz.equals(TagEnd.class)) {
			return "TAG_End";
		} else if (clazz.equals(TagFloat.class)) {
			return "TAG_Float";
		} else if (clazz.equals(TagInt.class)) {
			return "TAG_Int";
		} else if (clazz.equals(TagList.class)) {
			return "TAG_List";
		} else if (clazz.equals(TagLong.class)) {
			return "TAG_Long";
		} else if (clazz.equals(TagShort.class)) {
			return "TAG_Short";
		} else if (clazz.equals(TagString.class)) {
			return "TAG_String";
		} else if (clazz.equals(TagBoolean.class)) {
			return "TAG_Boolean";
		} else if (clazz.equals(TagNibble.class)) {
			return "TAG_Nibble";
		} else if (clazz.equals(TagEByte.class)) {
			return "TAG_EByte";
		} else if (clazz.equals(TagEShort.class)) {
			return "TAG_EShort";
		} else if (clazz.equals(TagEInt.class)) {
			return "TAG_EInt";
		} else if (clazz.equals(TagShortArray.class)) {
			return "Tag_Short_Array";
		} else if (clazz.equals(TagIntArray.class)) {
			return "Tag_Int_Array";
		} else if (clazz.equals(TagLongArray.class)) {
			return "Tag_Long_Array";
		} else if (clazz.equals(TagEShortArray.class)) {
			return "Tag_EShort_Array";
		} else if (clazz.equals(TagEIntArray.class)) {
			return "Tag_EInt_Array";
		} else {
			throw new IllegalArgumentException("Invalid tag classs (" + clazz.getName() + ").");
		}
	}

	/**
	 * Default private constructor.
	 */
	private NBTUtils() {

	}

}
