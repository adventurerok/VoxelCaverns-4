/**
 * 
 */
package vc4.server;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.text.*;

import vc4.api.cmd.*;
import vc4.api.font.ChatColor;
import vc4.api.logging.ConsoleHandler;
import vc4.api.logging.Logger;
import vc4.api.permissions.DefaultPermissions;
import vc4.api.server.ServerConsole;
import vc4.api.text.Localization;
import vc4.api.util.StringSplitter;
import vc4.api.world.World;
import vc4.impl.GameLoader;
import vc4.impl.cmd.CommandExecutor;
import vc4.impl.plugin.PluginManager;
import vc4.impl.server.ConsoleUser;
import vc4.impl.world.ImplWorld;
import vc4.server.cmd.BasicCommand;
import vc4.server.cmd.PermissionCommand;
import vc4.server.gui.ConsoleListener;
import vc4.server.server.ServerHandler;
import vc4.server.server.ServerSettings;
import vc4.server.user.UserManager;

/**
 * @author paul
 * 
 */
public class Console extends ServerConsole implements Runnable {

	private static String colorChart = "0123456789abcdefgnt";

	public JFrame window;
	public JTextPane output;
	public JTextPane input;
	public ImplWorld world;

	ServerHandler serverHandler;

	public ServerHandler getServerHandler() {
		return serverHandler;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		Console console = new Console();
		new ServerResources();
		GameLoader.load(new ConsoleHandler());
		console.run();
	}

	/**
	 * 
	 */
	public Console() {
		setConsole(this);

		ConsoleListener listen = new ConsoleListener(this);

		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setSize(675, 340);
		window.setLayout(new BorderLayout());
		window.addWindowListener(listen);
		output = new JTextPane();
		output.setEditable(false);
		output.setBackground(Color.black);
		output.setForeground(Color.white);
		output.addMouseListener(listen);
		output.setFont(new Font("Monospaced", 0, 12));
		input = new JTextPane();
		((AbstractDocument) input.getDocument())
				.setDocumentFilter(new OneLineFilter());
		input.setBackground(Color.black);
		input.setForeground(Color.white);
		input.addKeyListener(listen);
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
		public void insertString(FilterBypass fb, int offset, String string,
				AttributeSet attr) throws BadLocationException {
			fb.insertString(offset, string.replaceAll("\\n", ""), attr);
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length,
				String string, AttributeSet attr) throws BadLocationException {
			fb.insertString(offset, string.replaceAll("\\n", ""), attr);
		}
	}

	public void handleInput(String input) {
		if (input == null || input.isEmpty())
			return;
		String[] parts = StringSplitter.splitString(input, false);
		String cmd = parts[0];
		String[] args = Arrays.copyOfRange(parts, 1, parts.length);
		Command command = new Command(cmd, args, ConsoleUser.CONSOLE);
		CommandExecutor.executeCommand(command);
	}

	@Override
	public void write(String line) {
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setForeground(attributes, Color.white);
		StyleConstants.setBackground(attributes, Color.black);
		write(line, attributes);
		output.setCaretPosition(output.getStyledDocument().getLength());
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
					if (s == '}')
						--indent;
					else if (s == '{')
						++indent;
					if (indent < 0)
						break;
					format.append(s);
				}
				try {
					d.insertString(d.getLength(), text.toString(), attributes);
				} catch (BadLocationException e) {
					Logger.getLogger(Console.class).warning(
							"Failed to append text", e);
				}
				text = new StringBuilder();
				attributes = handleFormat(format.toString(), attributes);
				continue;
			} else
				text.append(c);
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
	private SimpleAttributeSet subHandleFormat(String format,
			SimpleAttributeSet s) {
		int firstPos = format.indexOf(":");
		if (firstPos == -1)
			return s;
		String key = format.substring(0, firstPos);
		String value = format.substring(firstPos + 1, format.length());
		if (key.equals("c")) {
			Color color = Color.white;
			if (value.length() == 1) {
				int pos = colorChart.indexOf(value.charAt(0));
				color = ChatColor.getColor(pos);
				if (color == null)
					color = Color.white;
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
				StyleConstants.setStrikeThrough(s,
						!StyleConstants.isStrikeThrough(s));
			}
		} else if (key.equals("b")) {
			Color color = Color.white;
			if (value.length() == 1) {
				int pos = colorChart.indexOf(value.charAt(0));
				color = ChatColor.getColor(pos);
				if (color == null)
					color = Color.white;
				else if (pos == 18)
					color = Color.black;
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
		load();
		try {
			serverHandler = new ServerHandler();
		} catch (IOException e) {
			Logger.getLogger(Console.class)
					.warning("Failed to start server", e);
			return;
		}
		serverHandler.start();
	}
	
	public void load() {
		window.setVisible(true);
		new ServerVoxelCaverns();
		ServerSettings.load();
		UserManager.load();
		loadCommands();
		PluginManager.loadAndEnablePlugins();
		loadWorld("World");
		
	}
	
	public void loadWorld(String name) {
		Logger.getLogger("VC4").info("Loading world: " + name);
		world = new ImplWorld(name);
		world.loadWorld();
		try {
			world.saveInfo();
		} catch (IOException e) {
			Logger.getLogger(Console.class).warning("Failed to save world details", e);
		}
		Logger.getLogger("VC4").info("World loaded");
	}

	private void loadCommands() {
		CommandHandler perms = new PermissionCommand();
		CommandHandler basic = new BasicCommand();
		CommandExecutor.addCommand(new ExecutableCommand(new CommandInfo(
				"permissions").addAlias("perms")
				.setUsage("<task> [taskoptions]")
				.setDescription("{l:cmd.perms.desc}")
				.setCommandUsage(new CommandUsage().setMinimumArgs(1)), perms));
		CommandExecutor
				.addCommand(new ExecutableCommand(new CommandInfo("name")
						.addAlias("nick")
						.setUsage("<newname> [any]")
						.setDescription("{l:cmd.name.desc}")
						.setCommandUsage(
								new CommandUsage().setMinimumArgs(1)
										.setMaximumArgs(2)
										.setRequiredCapabilities(0b1)), basic));
		DefaultPermissions.setPermission("vc4.cmd.perms.list", 1);
		DefaultPermissions.setPermission("vc4.cmd.perms.get", 1);
	}

	public void stop() {
		Logger.getLogger("VC4").info("Server shutting down...");
		UserManager.saveUsers();
		ServerSettings.save();
		serverHandler.exit();
	}

	@Override
	public World getWorld() {
		return world;
	}

}
