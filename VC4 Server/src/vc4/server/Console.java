/**
 * 
 */
package vc4.server;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.text.*;

import vc4.api.font.ChatColor;
import vc4.api.logging.ConsoleHandler;
import vc4.api.logging.Logger;
import vc4.api.server.ServerConsole;
import vc4.api.text.Localization;
import vc4.impl.GameLoader;

/**
 * @author paul
 * 
 */
public class Console extends ServerConsole implements MouseListener, KeyListener, Runnable {

	private static String colorChart = "0123456789abcdefgnt";

	JFrame window;
	JTextPane output;
	JTextPane input;
	
	ServerHandler serverHandler;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Console console = new Console();
		GameLoader.load(new ConsoleHandler());
		console.run();
	}
	
	

	/**
	 * 
	 */
	public Console() {
		setConsole(this);
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setSize(675, 340);
		window.setLayout(new BorderLayout());
		output = new JTextPane();
		output.setEditable(false);
		output.setBackground(Color.black);
		output.setForeground(Color.white);
		output.addMouseListener(this);
		output.setFont(new Font("Monospaced", 0, 12));
		input = new JTextPane();
		((AbstractDocument) input.getDocument()).setDocumentFilter(new OneLineFilter());
		input.setBackground(Color.black);
		input.setForeground(Color.white);
		input.addKeyListener(this);
		input.setFont(new Font("Monospaced", 0, 12));
		JScrollPane scroll = new JScrollPane(output);
		window.add(scroll, BorderLayout.CENTER);
		window.add(input, BorderLayout.SOUTH);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setTitle("VC4 Console");
		ConsoleHandler.setConsole(this);
	}

	private static class OneLineFilter extends DocumentFilter {
		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			fb.insertString(offset, string.replaceAll("\\n", ""), attr);
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
			fb.insertString(offset, string.replaceAll("\\n", ""), attr);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		input.requestFocusInWindow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			writeLine(input.getText());
			input.setText("");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void write(String line) {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.white);
		StyleConstants.setBackground(attributes, Color.black);
		write(line, attributes);
	}

	public SimpleAttributeSet write(String line, SimpleAttributeSet attributes) {
		StyledDocument d = output.getStyledDocument();
		StringBuilder text = new StringBuilder();
		for (int q = 0; q < line.length(); ++q) {
			char c = line.charAt(q);
			if (c == '{') {
				int indent = 0;
				StringBuilder format = new StringBuilder();
				for (q += 1; q < line.length(); ++q) {
					char s = line.charAt(q);
					if (s == '}') --indent;
					else if (s == '{') ++indent;
					if (indent < 0) break;
					format.append(s);
				}
				try {
					d.insertString(d.getLength(), text.toString(), attributes);
				} catch (BadLocationException e) {
					Logger.getLogger(Console.class).warning("Failed to append text", e);
				}
				text = new StringBuilder();
				attributes = handleFormat(format.toString(), attributes);
				continue;
			} else text.append(c);
		}
		try {
			d.insertString(d.getLength(), text.toString(), attributes);
		} catch (BadLocationException e) {
			Logger.getLogger(Console.class).warning("Failed to append text", e);
		}
		return attributes;
	}

	@Override
	public void writeLine(String line) {
		write(line + "\n");
	}

	private SimpleAttributeSet handleFormat(String format, SimpleAttributeSet s) {
		String[] parts = format.split(";");
		for (String l : parts) {
			s = subHandleFormat(l, s);
		}
		return s;
	}

	/**
	 * @param l
	 * @param s
	 * @return
	 */
	private SimpleAttributeSet subHandleFormat(String format, SimpleAttributeSet s) {
		int firstPos = format.indexOf(":");
		if (firstPos == -1) return s;
		String key = format.substring(0, firstPos);
		String value = format.substring(firstPos + 1, format.length());
		if (key.equals("c")) {
			Color color = Color.white;
			if (value.length() == 1) {
				int pos = colorChart.indexOf(value.charAt(0));
				color = ChatColor.getColor(pos);
				if (color == null) color = Color.white;
			} else if (value.length() == 6) {
				try {
					color = new Color(Integer.parseInt(value, 16));
				} catch (NumberFormatException e) {
				}
			}
			StyleConstants.setForeground(s, color);
		} else if (key.equals("f")) {
			if (value.equals("b")) {
				StyleConstants.setBold(s, !StyleConstants.isBold(s));
			} else if (value.equals("i")) {
				StyleConstants.setItalic(s, !StyleConstants.isItalic(s));
			} else if (value.equals("u")) {
				StyleConstants.setUnderline(s, !StyleConstants.isUnderline(s));
			} else if (value.equals("s")) {
				StyleConstants.setStrikeThrough(s, !StyleConstants.isStrikeThrough(s));
			}
		} else if (key.equals("b")) {
			Color color = Color.white;
			if (value.length() == 1) {
				int pos = colorChart.indexOf(value.charAt(0));
				color = ChatColor.getColor(pos);
				if (color == null) color = Color.white;
				else if (pos == 18) color = Color.black;
			} else if (value.length() == 6) {
				try {
					color = new Color(Integer.parseInt(value, 16));
				} catch (NumberFormatException e) {
				}
			}
			StyleConstants.setBackground(s, color);
		} else if (key.equals("l")) {
			String oa[] = value.split(",");
			String args[] = Arrays.copyOfRange(oa, 1, oa.length);
			String loc = Localization.getLocalization(oa[0], args);
			s = write(loc, s);
		}
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		window.setVisible(true);
		try {
			serverHandler = new ServerHandler();
		} catch (IOException e) {
			Logger.getLogger(Console.class).warning("Failed to start server", e);
			return;
		}
		serverHandler.start();
	}

}