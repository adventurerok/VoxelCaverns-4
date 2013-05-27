package vc4.api.container;

import vc4.api.item.ItemStack;
import vc4.api.math.MathUtils;

public class ContainerChest extends Container{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3820081214632348196L;
	
	public int scroll = 0;

	public ContainerChest() {
		super();
	}
	
	public int getRows(int columns){
		return MathUtils.ceil(getSize() / (float)columns);
	}
	
	@Override
	public String getGuiName() {
		return "chest";
	}


	public ContainerChest(int slots) {
		super(slots);
		
	}
	
	public int getScroll() {
		return scroll;
	}

	public void setScroll(int scroll) {
		this.scroll = scroll;
	}

	@Override
	public short getId() {
		return 4;
	}
	
	public ItemStack removeItemsFromStack(int count){
		for(int i = 0; i < getSize(); ++i){
			ItemStack s = getItem(i);
			if(s != null){
				if(!s.checkIsNotEmpty()){
					setItem(i, null);
					continue;
				}
				ItemStack small = s.clone().setAmount(count);
				s.setAmount(Math.max(0, s.getAmount() - count));
				if(!s.checkIsNotEmpty()){
					setItem(i, null);
				}
				return small;
			}
		}
		return null;
	}

}
