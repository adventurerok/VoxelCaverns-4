/**
 * 
 */
package vc4.api.yaml;

import java.io.*;
import java.util.*;

/**
 * @author paul
 *
 */
public class YamlMap implements Iterable<Object>{

	private Map<Object, Object> baseMap;

	public YamlMap(Map<Object, Object> baseMap) {
		super();
		this.baseMap = baseMap;
	}
	
	public YamlMap(){
		this(new HashMap<>());
	}
	
	@SuppressWarnings("unchecked")
	public YamlMap(InputStream in){
		this((Map<Object, Object>) ThreadYaml.getYamlForThread().load(in));
	}
	
	@SuppressWarnings("unchecked")
	public YamlMap(Reader in){
		this((Map<Object, Object>) ThreadYaml.getYamlForThread().load(in));
	}
	
	@SuppressWarnings("unchecked")
	public YamlMap(String in){
		this((Map<Object, Object>) ThreadYaml.getYamlForThread().load(in));
	}
	
	
	public int getInt(String key){
		return ((Number)baseMap.get(key)).intValue();
	}
	
	public long getLong(String key){
		return ((Number)baseMap.get(key)).longValue();
	}
	
	public short getShort(String key){
		return ((Number)baseMap.get(key)).shortValue();
	}
	
	public short getByte(String key){
		return ((Number)baseMap.get(key)).byteValue();
	}
	
	public double getDouble(String key){
		return ((Number)baseMap.get(key)).doubleValue();
	}
	
	@SuppressWarnings("unchecked")
	public Object[] getList(String key){
		List<Object> l = (List<Object>) baseMap.get(key);
		return l.toArray(new Object[l.size()]);
	}
	
	public float getFloat(String key){
		return ((Number)baseMap.get(key)).floatValue();
	}
	
	public String getString(String key){
		return baseMap.get(key).toString();
	}
	
	public char getChar(String key){
		return (Character) baseMap.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public YamlMap getSubMap(String key){
		YamlMap map = new YamlMap((Map<Object, Object>) baseMap.get(key));
		return map;
	}
	
	public boolean getBoolean(String key){
		return (Boolean) baseMap.get(key);
	}
	
	public boolean hasKey(String key){
		return baseMap.containsKey(key);
	}
	
	public YamlMap createSubMap(String key){
		if(hasKey(key)){
			return getSubMap(key);
		}
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		baseMap.put(key, map);
		return new YamlMap(map);
	}
	
	public void setInt(String key, int value){
		baseMap.put(key, value);
	}
	
	public void setLong(String key, long value){
		baseMap.put(key, value);
	}
	
	public void setShort(String key, short value){
		baseMap.put(key, value);
	}
	
	public void setByte(String key, byte value){
		baseMap.put(key, value);
	}
	
	public void setChar(String key, char value){
		baseMap.put(key, value);
	}
	
	public void setString(String key, String value){
		baseMap.put(key, value);
	}
	
	public void setDouble(String key, double value){
		baseMap.put(key, value);
	}
	
	public void setFloat(String key, float value){
		baseMap.put(key, value);
	}
	
	public void setSubMap(String key, YamlMap map){
		baseMap.put(key, map.baseMap);
	}
	
	public void setSubMap(String key, Map<Object, Object> map){
		baseMap.put(key, map);
		ThreadYaml.getYamlForThread();
	}
	
	public String dump(){
		return ThreadYaml.getYamlForThread().dump(baseMap);
	}
	
	public void dump(Writer writer){
		ThreadYaml.getYamlForThread().dump(baseMap, writer);
	}
	
	public void setList(String key, Object[] list){
		baseMap.put(key, Arrays.asList(list));
	}
	
	public void setList(String key, List<Object> list){
		baseMap.put(key, list);
	}
	
	public Map<Object, Object> getBaseMap() {
		return baseMap;
	}

	@Override
	public Iterator<Object> iterator() {
		return baseMap.values().iterator();
	}
	
}
