package vc4.launcher;

import javax.swing.UIManager;

/**
 * Loads the launcher, and updates it
 * @author paul
 *
 */
public class Loader {

	
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
		new Launcher();
	}
}
