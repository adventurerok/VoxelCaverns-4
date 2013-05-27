package vc4.vanilla.gui;

import vc4.api.block.OpenContainer;
import vc4.api.gui.GuiOpenContainer;
import vc4.api.gui.ItemPanel;

public class GuiChest extends GuiOpenContainer {

	public GuiChest(OpenContainer cont) {
		super(cont);
		innerGui = new ItemPanel(cont.entity.getContainer());
	}

}
