package vc4.api.container;

import vc4.api.item.ItemStack;

public class ContainerItems extends Container {

	/**
	 * 
	 */
	private static final long serialVersionUID = 36822208374927613L;

	
	public ContainerItems() {
		// TASK Auto-generated constructor stub
	}

	public ContainerItems(int slots) {
		super(slots);
	}

	public ContainerItems(ItemStack... items) {
		super(items);
	}

	@Override
	public String getName() {
		return "items";
	}

}
