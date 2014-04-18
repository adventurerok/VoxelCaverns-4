package vc4.api.entity.trait;

import vc4.api.container.Container;
import vc4.api.container.ContainerItems;
import vc4.api.entity.Entity;
import vc4.api.vbt.TagCompound;

public class TraitInventory extends Trait {

	ContainerItems inventory;

	public TraitInventory(Entity entity) {
		super(entity);
		inventory = new ContainerItems(44);
	}

	@Override
	public void update() {
		// TASK Auto-generated method stub

	}

	@Override
	public String name() {
		return "inventory";
	}

	@Override
	public boolean persistent() {
		return true;
	}

	@Override
	public void loadSaveCompound(TagCompound tag) {
		super.loadSaveCompound(tag);
		TagCompound inv = tag.getCompoundTag("inv");
		inventory = (ContainerItems) Container.readContainer(entity.world, inv);
	}

	@Override
	public TagCompound getSaveCompound() {
		TagCompound tag = super.getSaveCompound();
		TagCompound inv = new TagCompound("inv");
		inventory.writeContainer(entity.world, inv);
		tag.addTag(inv);
		return tag;
	}

}
