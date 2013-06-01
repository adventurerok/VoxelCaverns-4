package vc4.api.map;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BidiMap<K, V> implements Map<K, V>{

	//Not immutable, but almost. Used for thread safe
	final ConcurrentHashMap<K, V> forwardMap = new ConcurrentHashMap<>();
	final ConcurrentHashMap<V, K> inverseMap = new ConcurrentHashMap<>();
	
	@Override
	public int size() {
		return forwardMap.size();
	}

	@Override
	public boolean isEmpty() {
		return forwardMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return forwardMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return inverseMap.containsKey(value);
	}

	@Override
	public V get(Object key) {
		return forwardMap.get(key);
	}
	
	public V getValue(K key){
		return get(key);
	}
	
	public K getKey(V value){
		return inverseMap.get(value);
	}

	@Override
	public V put(K key, V value) {
		if (forwardMap.containsKey(key)) {
            inverseMap.remove(forwardMap.get(key));
        }
        if (inverseMap.containsKey(value)) {
            forwardMap.remove(inverseMap.get(value));
        }
        final V obj = forwardMap.put(key, value);
        inverseMap.put(value, key);
        return obj;
	}

	@Override
	public V remove(Object key) {
		V value = null;
        if (forwardMap.containsKey(key)) {
            value = forwardMap.remove(key);
            inverseMap.remove(value);
        }
        return value;
	}
	
	public V removeKey(K key){
		return remove(key);
	}
	
	public K removeValue(V value){
		K key = null;
        if (inverseMap.containsKey(value)) {
        	key = inverseMap.remove(value);
            forwardMap.remove(key);
        }
        return key;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(Entry<? extends K, ? extends V> e : m.entrySet()){
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public void clear() {
		forwardMap.clear();
		inverseMap.clear();
		
	}

	@Override
	public Set<K> keySet() {
		return forwardMap.keySet();
	}

	@Override
	public Collection<V> values() {
		return forwardMap.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return forwardMap.entrySet();
	}

}
