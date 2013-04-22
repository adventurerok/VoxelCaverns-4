package vc4.launcher.gui.frame;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import javax.swing.JLabel;

public class LoaderGui extends JFrame{
	
	public LoaderGui() {
		setTitle("Updating Launcher");
		setResizable(false);
		
		JProgressBar progressBar = new JProgressBar();
		getContentPane().add(progressBar, BorderLayout.CENTER);
		
		JLabel lblThisMayTake = new JLabel("This may take a few minutes, depending on your internet speed");
		getContentPane().add(lblThisMayTake, BorderLayout.NORTH);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4128306563942263708L;

}
