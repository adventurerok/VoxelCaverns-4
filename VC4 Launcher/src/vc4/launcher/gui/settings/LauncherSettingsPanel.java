package vc4.launcher.gui.settings;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import vc4.launcher.enumeration.UpdateStream;

public class LauncherSettingsPanel extends JPanel {
	public LauncherSettingsPanel() {
		setLayout(new GridLayout(0, 2, 0, 0));
		setPreferredSize(new Dimension(300, 100));
		
		JLabel lblAutoUpdate = new JLabel("Auto Update");
		add(lblAutoUpdate);
		
		JCheckBox checkBox = new JCheckBox("");
		checkBox.setSelected(true);
		add(checkBox);
		
		JLabel lblNewLabel = new JLabel("Default Update Stream");
		add(lblNewLabel);
		
		JComboBox<UpdateStream> comboBox = new JComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<UpdateStream>(UpdateStream.values()));
		add(comboBox);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4839629446085628340L;

}
