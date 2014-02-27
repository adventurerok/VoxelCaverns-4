/**
 * 
 */
package vc4.impl.version;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.*;

import vc4.api.Version;
import vc4.api.logging.Logger;

/**
 * @author paul
 * 
 */
public class ChangelogUpdater extends JFrame implements ActionListener {

	JTextArea text = new JTextArea();

	/**
	 * 
	 */
	private static final long serialVersionUID = 7702337211342240768L;

	/**
	 * 
	 */
	public ChangelogUpdater() {
		setTitle("Changelog Updater");
		JButton ok = new JButton("Ok");
		JButton skip = new JButton("Skip");
		ok.setActionCommand("ok");
		skip.setActionCommand("skip");
		ok.addActionListener(this);
		skip.addActionListener(this);
		text.setBorder(BorderFactory.createTitledBorder("Changes since last build"));
		add(text);
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 0));
		p.add(ok);
		p.add(skip);
		add(p, BorderLayout.SOUTH);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 300);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			URL url = Version.class.getClassLoader().getResource("vc4/api/changelog.txt");
			try {
				String loc = URLDecoder.decode(url.getFile(), "UTF-8");
				FileWriter r = new FileWriter(loc, true);
				try (BufferedWriter writer = new BufferedWriter(r)) {
					writer.write(Version.VERSION + "\n");
					String text = this.text.getText().replace("\n", "\n\t");
					writer.write(text);
				}
			} catch (Exception e1) {
				Logger.getLogger(ChangelogUpdater.class).warning("Failed to update changelog", e1);
			}
			try {
				url = new URL(url.toString().replace("bin", "src"));
				String loc = URLDecoder.decode(url.getFile(), "UTF-8");
				FileWriter r = new FileWriter(loc, true);
				try (BufferedWriter writer = new BufferedWriter(r)) {
					writer.write("\n" + Version.VERSION + "\n\t");
					String text = this.text.getText().replace("\n", "\n\t");
					writer.write(text);
				}
			} catch (Exception e1) {
				Logger.getLogger(ChangelogUpdater.class).warning("Failed to update changelog", e1);
			}
		}
		setVisible(false);
		dispose();
	}

}
