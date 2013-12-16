package vc4.launcher.gui.tab;

import java.awt.*;

import javax.swing.*;

import vc4.launcher.Launcher;

public class ConsoleTab extends JPanel {
	public ConsoleTab() {
		setLayout(new BorderLayout(0, 0));
		
		JTextArea cout = Launcher.getSingleton().getConsoleOut();
		cout.setBackground(Color.black);
		cout.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		cout.setForeground(Color.white);
		cout.setEditable(false);
		JScrollPane scroller = new JScrollPane(cout);
		add(scroller);
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 862924372163580459L;

}
