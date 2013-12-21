package vc4.launcher.gui;

import javax.swing.JTextArea;

public class AutoTextArea extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6956598734412974461L;
	
	private int length = 0;

	public AutoTextArea() {
		super();
	}

	public AutoTextArea(int rows, int columns) {
		super(rows, columns);
	}

	public AutoTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		this.length += text.length();
	}

	public AutoTextArea(String text) {
		super(text);
		this.length += text.length();
	}
	
	@Override
	public void setText(String t) {
		super.setText(t);
		this.length = t.length();
	}
	
	@Override
	public void append(String str) {
		super.append(str);
		this.setCaretPosition(length += str.length());
	}

}
