package vc4.api.entity.trait;

import vc4.api.block.CraftingTable;
import vc4.api.entity.Entity;

public class TraitCrafting extends Trait {

	CraftingTable craftingTable = new CraftingTable();

	public TraitCrafting(Entity entity) {
		super(entity);
	}

	@Override
	public void update() {
		craftingTable.update(entity);
	}

	@Override
	public String name() {
		return "crafting";
	}

	public CraftingTable getCraftingTable() {
		return craftingTable;
	}

	@Override
	public boolean persistent() {
		return false;
	}

}
