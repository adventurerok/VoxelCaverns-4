/**
 * 
 */
package vc4.api.gui;

import java.util.LinkedHashMap;

/**
 * @author paul
 *
 */
public interface GuiType {

	
	public String getTypeName();
	public void addComponentTo(Gui parent, LinkedHashMap<String, ?> yaml);
	public void setup(Gui c, LinkedHashMap<String, ?> yaml);
	public boolean isClickable(Gui c);
	public boolean isVisible(Gui c);
	
}
