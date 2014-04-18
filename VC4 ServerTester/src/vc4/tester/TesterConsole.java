package vc4.tester;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

import vc4.api.Version;
import vc4.api.cmd.*;
import vc4.api.font.ChatColor;
import vc4.api.logging.ConsoleHandler;
import vc4.api.logging.Logger;
import vc4.api.server.ServerConsole;
import vc4.api.server.User;
import vc4.api.text.Localization;
import vc4.api.util.OS;
import vc4.api.util.StringSplitter;
import vc4.api.vbt.Tag;
import vc4.api.vbt.TagCompound;
import vc4.api.world.World;
import vc4.impl.GameLoader;
import vc4.tester.cmd.CommandListener;

/**
 * @author paul
 * 
 */
public class TesterConsole extends ServerConsole implements MouseListener, WindowListener, KeyListener, Runnable {

	private static String colorChart = "0123456789abcdefgnt";

	public HashMap<String, ExecutableCommand> commands = new HashMap<>();
	public CommandHandler commandListener = new CommandListener();
	public User user = new TestUser();
	public ClientHandler client;

	JFrame window;
	JTextPane output;
	JTextPane input;

	// ServerHandler serverHandler;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TesterConsole console = new TesterConsole();
		new ServerResources();
		GameLoader.load(new ConsoleHandler());
		console.run();
	}

	public ClientHandler getClient() {
		return client;
	}

	/**
	 * 
	 */
	public TesterConsole() {
		setConsole(this);
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setSize(675, 340);
		window.setLayout(new BorderLayout());
		window.addWindowListener(this);
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
		window.setTitle("VC4 Server Testing Utility");
		ConsoleHandler.setConsole(this);

		commands.put("msg", new ExecutableCommand(new CommandInfo("msg").setUsage("<msg>").setDescription("Sends the server a message").setCommandUsage(new CommandUsage().setMinimumArgs(1)), commandListener));
	}

	public Tag getClientDetails() {
		TagCompound tag = new TagCompound("client");
		tag.setString("version", Version.VERSION);
		tag.setString("name", "VC4 Server Debug/Testing Client");
		tag.setByte("graphics", 0);
		tag.setString("java", System.getProperty("java.version"));
		tag.setString("zone", Calendar.getInstance().getTimeZone().getID());
		tag.setInt("dst", Calendar.getInstance().getTimeZone().getDSTSavings());
		// tag.setString("region", System.getProperty("user.region"));
		// tag.setString("lang", System.getProperty("user.language"));
		tag.setString("os", OS.getOs().name());
		return tag;
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
			processInput(input.getText());
			input.setText("");
		}
	}

	public void processInput(String input) {
		String[] parts = StringSplitter.splitString(input, false);
		String cmd = parts[0];
		String[] args = Arrays.copyOfRange(parts, 1, parts.length);
		Command command = new Command(cmd, args, user);
		ExecutableCommand e = commands.get(command.getCommand());
		if (e == null) {
			user.message("{l:cmd.nocommand," + "NOT_IMPLEMENTED" + "}");
			return;
		}
		if (!e.getInfo().getCommandUsage().check(command)) return;
		e.getHandler().handleCommand(command);
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
					Logger.getLogger(TesterConsole.class).warning("Failed to append text", e);
				}
				text = new StringBuilder();
				attributes = handleFormat(format.toString(), attributes);
				continue;
			} else text.append(c);
		}
		try {
			d.insertString(d.getLength(), text.toString(), attributes);
		} catch (BadLocationException e) {
			Logger.getLogger(TesterConsole.class).warning("Failed to append text", e);
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
		ServerMap.loadSuids();
		window.setVisible(true);
		try {
			client = new ClientHandler("localhost");
		} catch (IOException e) {
			Logger.getLogger(TesterConsole.class).warning("Failed to start server", e);
			return;
		}
		client.start();
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		ServerMap.saveSuids();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public World getWorld() {
		return null;
	}

}
