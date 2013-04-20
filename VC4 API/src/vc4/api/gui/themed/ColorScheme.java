/**
 * 
 */
package vc4.api.gui.themed;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import vc4.api.vector.Vector4f;

/**
 * @author paul
 *
 */
public class ColorScheme {

	String name;
	
	public Vector4f backgroundNormal;
	public Vector4f backgroundSelected;
	public Vector4f outlineNormal;
	public Vector4f outlineSelected;
	
	@SuppressWarnings("unchecked")
	public void load(LinkedHashMap<String, ?> input){
		for(Entry<String, ?> e : input.entrySet()){
			if(e.getKey().equals("name")) name = e.getValue().toString();
			if(!(e.getValue() instanceof LinkedHashMap)) continue;
			LinkedHashMap<String, ?> colorText = (LinkedHashMap<String, ?>) e.getValue();
			Vector4f color = loadColor(colorText);
			if(e.getKey().equals("background.normal")) backgroundNormal = color;
			else if(e.getKey().equals("background.selected")) backgroundSelected = color;
			else if(e.getKey().equals("outline.normal")) outlineNormal = color;
			else if(e.getKey().equals("outline.selected")) outlineSelected = color;
			
		}
	}
	
	private Vector4f loadColor(LinkedHashMap<String, ?> input){
		Vector4f color = new Vector4f(0, 0, 0, 1);
		for(Entry<String, ?> e : input.entrySet()){
			float f = 0;
			try{
				f = Float.parseFloat(e.getValue().toString());
			} catch(Exception e1){}
			if(e.getKey().equals("red")) color.x = f;
			else if(e.getKey().equals("green")) color.y = f;
			else if(e.getKey().equals("blue")) color.z = f;
			else if(e.getKey().equals("alpha")) color.w = f;
			else if(e.getKey().equals("rgb")){
				int i = Integer.parseInt(e.getValue().toString(), 16);
				Color c = new Color(i);
				color.x = c.getRed() / 255f;
				color.y = c.getGreen() / 255f;
				color.z = c.getBlue() / 255f;
			}
		}
		return color;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	
}
