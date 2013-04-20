/**
 * 
 */
package vc4.api.block;

import java.util.HashMap;
import java.util.Random;

/**
 * @author paul
 *
 */
public class Material {

	private String name;
	
	private long uid;
	
	private static Random rand = new Random(2973293712387L);
	
	private static HashMap<String, Material> materials = new HashMap<String, Material>();
	
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the uid created at runtime for the Material
	 */
	public long getUid() {
		return uid;
	}
	
	public Object getAttribute(String name){
		return attributes.get(name);
	}
	
	public void setAttribute(String name, Object value){
		attributes.put(name, value);
	}
	
	public static Material getMaterial(String name){
		if(materials.containsKey(name)) return materials.get(name);
		
		Material m = new Material(name);
		materials.put(name, m);
		return m;
	}

	public Material(String name) {
		super();
		this.name = name;
		uid = rand.nextLong();
		initDefaultAttributes();
	}
	
	private void initDefaultAttributes(){
		attributes.put("destroyTime", 1.0F);
		attributes.put("blastResist", 1.0F);
	}
	
	/**
	 * Sets the attributes for the time to destroy a block and the blast resistance of the block.
	 * Attributes are options that define the default settings of a block of the material.
	 * 
	 * @param destroyTime The time it takes for a player to destroy this block, in seconds
	 * @param blastResistance The resistance of the blast. 1 would be typical
	 * @return The Material, to help in constructors
	 */
	public Material setDestroyAttributes(float destroyTime, float blastResistance){
		attributes.put("destroyTime", destroyTime);
		attributes.put("blastResist", blastResistance);
		return this;
	}
	
	

}
