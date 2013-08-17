package vc4.api.gui;

import java.awt.Rectangle;

public class ResizerFill implements Resizer {

	@Override
	public void resize(Component target) {
		target.setBounds((Rectangle) target.getParent().getBounds().clone());
	}

}
