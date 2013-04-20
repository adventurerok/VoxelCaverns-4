/**
 * 
 */
package vc4.api.gui;

import java.awt.Rectangle;

/**
 * @author paul
 *
 */
public class SettingScroller extends TextComponent {

	public SettingScroller(Object text, String cmd, String alt){
		setText(text);
		setCommand(cmd);
		setAltCommand(alt);
		final SettingScroller me = this;
		Button left = new Button("<", alt){
			
			@Override
			public void resized() {
				setBounds(new Rectangle(me.getX(), me.getY(), me.getHeight(), me.getHeight()));
			}
		};
		
		Button right = new Button(">", cmd){
			
			@Override
			public void resized() {
				setBounds(new Rectangle(me.getX() + me.getWidth() - me.getHeight(), me.getY(), me.getHeight(), me.getHeight()));
			}
		};
		add(left);
		add(right);
		Button center = new Button(text, cmd, alt){
			@Override
			public void resized() {
				setBounds(new Rectangle(me.getX() + me.getHeight() + 3, me.getY(), me.getWidth() - me.getHeight() * 2 - 6, me.getHeight()));
			}
		};
		add(center);
	}
	
	/* (non-Javadoc)
	 * @see vc4.api.gui.TextComponent#setFontSize(float)
	 */
	@Override
	public void setFontSize(float size) {
		super.setFontSize(size);
		for(Component c: getSubComponents()){
			if(c instanceof TextComponent){
				((TextComponent)c).setFontSize(size);
			}
		}
	}

}
