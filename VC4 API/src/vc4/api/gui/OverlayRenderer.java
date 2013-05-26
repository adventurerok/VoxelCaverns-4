package vc4.api.gui;

import java.util.ArrayList;

public class OverlayRenderer extends Component {

	private static ArrayList<OverlayRender> toRender = new ArrayList<OverlayRender>();

	public static void addRender(OverlayRender render) {
		toRender.add(render);
	}

	@Override
	public void draw() {
		while (!toRender.isEmpty()) {
			toRender.remove(0).draw();
		}
	}
	

	 /* (non-Javadoc)
	 * @see vc4.api.gui.Component#isClickable()
	 */
	@Override
	public boolean isClickable() {
		return false;
	}
	
	
}
