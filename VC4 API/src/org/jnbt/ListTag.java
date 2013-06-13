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

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>TAG_List</code> tag.
 * @author Graham Edgecombe
 *
 */
public final class ListTag extends Tag {

	/**
	 * The type.
	 */
	private Class<? extends Tag> type;

	/**
	 * The value.
	 */
	private List<Tag> value;
	

	
	
	public ListTag(String name, Class<? extends Tag> type){
		this(name, type, new ArrayList<Tag>());
	}
	
	/**
	 * Creates the tag.
	 * @param name The name.
	 * @param type The type of item in the list.
	 * @param value The value.
	 */
	@SuppressWarnings("unchecked")
	public ListTag(String name, Class<? extends Tag> type, List<? extends Tag> value) {
		super(name);
		this.type = type;
		this.value = (List<Tag>) value;
	}
	
	public void addTag(Tag t){
		if(!t.getClass().equals(type)) throw new RuntimeException("A " + NBTUtils.getTypeName(t.getClass()) + " tag may not be added to a list of " + NBTUtils.getTypeName(type) + "tags");
		value.add(t);
	}
	
	public Tag getNextTag(){
		if(!hasNext()) throw new RuntimeException("No tags left");
		return value.remove(0);
	}
	
	/**
	 * Gets the type of item in this list.
	 * @return The type of item in this list.
	 */
	public Class<? extends Tag> getType() {
		return type;
	}
	
	@Override
	public List<Tag> getValue() {
		return value;
	}
	
	public boolean hasNext(){
		return !value.isEmpty();
	}
	
	public void setType(Class<? extends Tag> type) {
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object o) {
		if(o instanceof List) value = (List<Tag>) o;
	}
	
	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if(name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}
		StringBuilder bldr = new StringBuilder();
		bldr.append("TAG_List" + append + ": " + value.size() + " entries of type " + NBTUtils.getTypeName(type) + "\r\n{\r\n");
		for(Tag t : value) {
			bldr.append("   " + t.toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
		}
		bldr.append("}");
		return bldr.toString();
	}

}
