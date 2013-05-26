package vc4.api.gui;

public class BorderResizer implements Resizer {

	Border border;
	
	
	public BorderResizer(Border border) {
		super();
		this.border = border;
	}

	@Override
	public void resize(Component target) {
		Component.getUtils().attachBorder(target.getParent().getBounds(), target, border);
	}
	
	public void setBorder(Border border) {
		this.border = border;
	}

}
