/**
 * 
 */
package vc4.api.gui;

import java.util.LinkedHashMap;

/**
 * @author paul
 *
 */
public interface ScreenType {

	
	public String getTypeName();
	public void addComponentTo(Screen parent, LinkedHashMap<String, ?> yaml);
	public void setup(Screen c, LinkedHashMap<String, ?> yaml);
	public boolean isClickable(Screen c);
	public boolean isVisible(Screen c);
	
}
