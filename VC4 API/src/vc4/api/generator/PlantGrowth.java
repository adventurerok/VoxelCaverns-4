package vc4.api.generator;

import vc4.api.block.Plant;

public class PlantGrowth {

	Plant plant;
	int amount;

	public Plant getPlant() {
		return plant;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((plant == null) ? 0 : plant.hashCode());
		return result;
	}

	@Override
	public PlantGrowth clone() {
		return new PlantGrowth(plant, amount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		PlantGrowth other = (PlantGrowth) obj;
		if (plant == null) {
			if (other.plant != null) return false;
		} else if (!plant.equals(other.plant)) return false;
		return true;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void addAmount(int amount) {
		this.amount += amount;
	}

	public PlantGrowth(Plant plant, int amount) {
		super();
		this.plant = plant;
		this.amount = amount;
	}
}
