package vc4.launcher.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JProgressBar;

public class LauncherGui extends JFrame {
	
	public LauncherGui() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(640, 480);
		setTitle("VoxelCaverns Launcher v4");
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("News", new NewsTab());
		tabbedPane.addTab("Settings", new SettingsTab());
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 50));
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(100, 10));
		panel.add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton playButton = new JButton("Play");
		panel_1.add(playButton);
		
		JProgressBar progressBar = new JProgressBar();
		panel.add(progressBar, BorderLayout.CENTER);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5628814843842908657L;

}
