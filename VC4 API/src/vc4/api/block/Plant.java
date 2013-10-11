/**
 * 
 */
package vc4.api.block;

import java.util.HashMap;

/**
 * @author paul
 *
 */
public class Plant {
	
	public static class PlantData{
		
		
		
		int id;
		HashMap<String, Integer> variants = new HashMap<>();
		HashMap<String, Integer> species = new HashMap<>();
		int nextVariant = 0, nextSpecies = 0;
		public PlantData(int id) {
			super();
			this.id = id;
		}
		
		public int getVariantId(String variant){
			Integer res = variants.get(variant);
			if(res == null){
				res = nextVariant++;
				variants.put(variant, res);
			}
			return res;
		}
		
		public int getSpeciesId(String species){
			Integer res = this.species.get(species);
			if(res == null){
				res = nextSpecies++;
				this.species.put(species, res);
			}
			return res;
		}
	}
	
	private static HashMap<String, PlantData> types = new HashMap<>();
	private static int nextId = 0;

	private int typeId;
	private byte variantId, speciesId;
	private boolean checkSubtype = true;
	
	private byte data;
	
	public Plant setData(byte data) {
		this.data = data;
		return this;
	}
	
	public byte getData() {
		return data;
	}
	
	public static PlantData getPlantData(String type){
		PlantData p = types.get(type);
		if(p == null){
			p = new PlantData(nextId++);
			types.put(type, p);
		}
		return p;
	}
	
	public static int getTypeId(String type){
		return getPlantData(type).id;
	}
	
	public void setCheckSpecies(boolean checkSpecies) {
		this.checkSubtype = checkSpecies;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + typeId;
		result = prime * result + variantId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Plant other = (Plant) obj;
		if (checkSubtype && other.checkSubtype && speciesId != other.speciesId) return false;
		if (typeId != other.typeId) return false;
		if(variantId != other.variantId) return false;
		return true;
	}

	public int getTypeId() {
		return typeId;
	}
	
	public byte getSpeciesId() {
		return speciesId;
	}
	
	public byte getVariantId() {
		return variantId;
	}
	
	public Plant(String type, String species, String variant){
		PlantData p = getPlantData(type);
		typeId = p.id;
		speciesId = (byte) p.getSpeciesId(species);
		variantId = (byte) p.getVariantId(variant);
	}
	
}
