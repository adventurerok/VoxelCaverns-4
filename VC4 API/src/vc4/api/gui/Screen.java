/**
 * 
 */
package vc4.api.gui;

import java.util.*;
import java.util.Map.Entry;

import vc4.api.logging.Logger;

/**
 * @author paul
 *
 */
public class Screen extends Component{

	private static HashMap<String, Class<? extends ScreenType>> types = new HashMap<String, Class<? extends ScreenType>>();
	
	private ScreenType type;
	
	
	public static void registerGuiType(String key, Class<? extends ScreenType> clz){
		types.put(key, clz);
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Screen(LinkedHashMap<String, ?> yaml) {
		List<LinkedHashMap<String, ?>> subs = new ArrayList<LinkedHashMap<String, ?>>();
		for(Entry<String, ?> e : yaml.entrySet()){
			if(!(e.getValue() instanceof LinkedHashMap)) continue;
			LinkedHashMap<String, ?> l = (LinkedHashMap<String, ?>) e.getValue();
			if(e.getKey().equals("gui")){
				for(Entry<String, ?> e1: l.entrySet()){
					if(e1.getKey().equals("type")){
						Class<? extends ScreenType> g = types.get(e1.getValue().toString());
						try {
							type = g.newInstance();
						} catch (InstantiationException | IllegalAccessException e2) {
							Logger.getLogger(Screen.class).warning("Failed to load Gui Type for GUI", e2);
							return;
						}
					}
				}
				type.setup(this, l);
			} else {
				subs.add(l);
			}
		}
		for(LinkedHashMap<String, ?> e : subs){
			type.addComponentTo(this, e);
		}
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#isClickable()
	 */
	@Override
	public boolean isClickable() {
		return type.isClickable(this);
	}
	
	/**
	 * @return the type
	 */
	public ScreenType getType() {
		return type;
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#draw()
	 */
	@Override
	public void draw() {
		if(!type.isVisible(this)) return;
		super.draw();
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.Component#update()
	 */
	@Override
	public void update() {
		if(!type.isVisible(this)) return;
		super.update();
	}
	
}
