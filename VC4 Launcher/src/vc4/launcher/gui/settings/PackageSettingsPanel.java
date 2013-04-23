package vc4.launcher.gui.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import vc4.launcher.Package;
import vc4.launcher.Version;
import vc4.launcher.enumeration.UpdateStreamType;

public class PackageSettingsPanel extends JPanel {
	
	Package pack;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -530629436030272049L;

	private JButton _updateButton;
	private JButton _launchButton;
	private JButton _disableButton;
	private JButton _removeButton;
	private JComboBox<Version> _versionComboBox;
	private JComboBox<UpdateStreamType> _updateStream;
	private JCheckBox _manualVersion;
	private JCheckBox _autoUpdate;
	private JCheckBox _previousVersions;
	private Version _chosen;
	
	
	public PackageSettingsPanel(final Package pack) {
		this.pack = pack;
		setLayout(new BorderLayout(0, 0));
		
		final JTextArea packageInfo = new JTextArea("Package Info");
		packageInfo.setPreferredSize(new Dimension(63, 80));
		add(packageInfo, BorderLayout.NORTH);
		packageInfo.setText(pack.getInfo());
		packageInfo.setEditable(false);
		packageInfo.setFont(new Font("default", 0, 10));
		packageInfo.setBackground(getBackground());
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Auto Update");
		panel.add(lblNewLabel_1);
		
		_autoUpdate = new JCheckBox("Check to auto update package");
		_autoUpdate.setSelected(pack.isAuto());
		_autoUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pack.setAuto(_autoUpdate.isSelected());
				
			}
		});
		panel.add(_autoUpdate);
		
		JLabel lblManualVersionSelect = new JLabel("Manual Version Select");
		panel.add(lblManualVersionSelect);
		
		_manualVersion = new JCheckBox("Check to choose which version to use");
		_manualVersion.setSelected(pack.isManual());
		_manualVersion.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				pack.setManual(_manualVersion.isSelected());
				_versionComboBox.setEnabled(pack.isManual());
				if(!pack.isManual()) _versionComboBox.setSelectedItem(pack.getLatest());
			}
		});
		panel.add(_manualVersion);
		
		JLabel lb2 = new JLabel("Keep previous versions");
		panel.add(lb2);
		
		_previousVersions = new JCheckBox("Check if you want to backup old versions");
		_previousVersions.setSelected(pack.isBackup());
		_previousVersions.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				pack.setBackup(_previousVersions.isSelected());
				
			}
		});
		panel.add(_previousVersions);
		
		JLabel lblNewLabel_2 = new JLabel("Update Stream");
		panel.add(lblNewLabel_2);
		
		_updateStream = new JComboBox<>();
		_updateStream.setModel(new DefaultComboBoxModel<UpdateStreamType>(UpdateStreamType.values()));
		_updateStream.setSelectedItem(pack.getType());
		_updateStream.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				pack.setType((UpdateStreamType) _updateStream.getSelectedItem());
				Version sel = (Version) _versionComboBox.getSelectedItem();
				_versionComboBox.setModel(new DefaultComboBoxModel<Version>(pack.getVisibleVersions()));
				if(pack.isManual()) _versionComboBox.setSelectedItem(sel);
				else _versionComboBox.setSelectedItem(pack.getLatest());
				_versionComboBox.repaint();
			}
		});
		panel.add(_updateStream);
		
		JLabel lblNewLabel_3 = new JLabel("Version");
		panel.add(lblNewLabel_3);
		
		_versionComboBox = new JComboBox<Version>();
		_versionComboBox.setEnabled(pack.isManual());
		_versionComboBox.setModel(new DefaultComboBoxModel<Version>(pack.getVisibleVersions()));
		_versionComboBox.setToolTipText("Choose the version to use");
		_versionComboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				_chosen = (Version) _versionComboBox.getSelectedItem();
				Version current = pack.getVersion();
				_updateButton.setEnabled(true);
				if(current == null) _updateButton.setText("Install");
				else if(current.getIntVersion() < _chosen.getIntVersion()) _updateButton.setText("Update");
				else if(current.getIntVersion() > _chosen.getIntVersion()) _updateButton.setText("Downgrade");
				else  {
					_updateButton.setEnabled(false);
				}
				_updateButton.repaint();
				
			}
		});
		panel.add(_versionComboBox);
		
		_updateButton = new JButton("Update");
		_updateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Version v = (Version) _versionComboBox.getSelectedItem();
					if(v == null) v = pack.getLatest();
					pack.install((Version) v);
					packageInfo.setText(pack.getInfo());
					packageInfo.repaint();
				} catch (IOException e1) {
					System.out.println("Failed to install");
				}
				
			}
		});
		panel.add(_updateButton);
		
		_launchButton = new JButton("Launch");
		_launchButton.setEnabled(pack.isDownloaded());
		panel.add(_launchButton);
		
		_disableButton = new JButton("Disable");
		_disableButton.setEnabled(pack.isDownloaded());
		panel.add(_disableButton);
		
		_removeButton = new JButton("Remove");
		_removeButton.setEnabled(pack.isDownloaded());
		panel.add(_removeButton);
	}

}
