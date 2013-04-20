package vc4.launcher.gui;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

public class NewsTab extends JPanel {
	public NewsTab() {
		setLayout(new BorderLayout(0, 0));
		
		JEditorPane editorPane = new JEditorPane();
		add(editorPane);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8629244080721280459L;

}
