package vc4.launcher.gui.settings;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import vc4.launcher.Launcher;
import vc4.launcher.task.Progress;
import vc4.launcher.task.Task;

public class TextSettingsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7522149954435101073L;

	private JTextArea text;

	public TextSettingsPanel(final URL url) {
		text = new JTextArea();
		text.setEditable(false);
		text.setLineWrap(true);
		text.setFont(new Font("default", 0, 10));
		text.setWrapStyleWord(true);
		setLayout(new BorderLayout());
		add(new JScrollPane(text));
		Task task = new Task() {

			@Override
			public void run(Progress progress) {
				progress.setText("Opening text file");
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
					final StringBuilder builder = new StringBuilder();
					String s = null;
					while ((s = reader.readLine()) != null) {
						builder.append(s).append("\n");
					}
					progress.setPercent(50);
					progress.setText("Updating Gui");
					Runnable run = new Runnable() {

						@Override
						public void run() {
							text.setText(builder.toString());

						}
					};
					progress.setPercent(100);
					progress.setDelete(true);
					SwingUtilities.invokeLater(run);
				} catch (IOException e) {
				}
			}

			@Override
			public boolean canRun() {
				return true;
			}
		};
		Launcher.getSingleton().getTasks().addTask(task);
	}

}
