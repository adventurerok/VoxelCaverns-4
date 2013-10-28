package vc4.launcher.gui.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;

import vc4.launcher.Launcher;
import vc4.launcher.gui.tab.NewsTab;
import vc4.launcher.gui.tab.SettingsTab;

public class LauncherGui extends JFrame {
	
	public LauncherGui() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(640, 480);
		setTitle("VoxelCaverns Launcher v4");
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("News", new ImageIcon(getClass().getClassLoader().getResource("resources/icons/news.png")), new NewsTab());
		tabbedPane.addTab("Settings", new ImageIcon(getClass().getClassLoader().getResource("resources/icons/settings.png")), new SettingsTab());
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
		playButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.getSingleton().launch();
				
			}
		});
		panel_1.add(playButton);
		
		_progressBar = new JProgressBar();
		_progressBar.setStringPainted(true);
		panel.add(_progressBar, BorderLayout.CENTER);
	}
	
	public JProgressBar getProgressBar() {
		return _progressBar;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5628814843842908657L;
	private JProgressBar _progressBar;

}
