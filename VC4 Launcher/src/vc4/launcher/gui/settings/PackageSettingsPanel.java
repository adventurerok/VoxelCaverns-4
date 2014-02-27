package vc4.launcher.gui.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import vc4.launcher.Launcher;
import vc4.launcher.gui.popup.RunnablePopupMenu;
import vc4.launcher.repo.Package;
import vc4.launcher.repo.Version;

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
	private JComboBox<String> _updateStream;
	private JCheckBox _manualVersion;
	private JCheckBox _autoUpdate;
	private Version _chosen;

	private JTextArea _packageInfo;

	public PackageSettingsPanel(final Package pack) {
		this.pack = pack;
		pack.setPanel(this);
		setLayout(new BorderLayout(0, 0));

		_packageInfo = new JTextArea("Package Info");
		_packageInfo.setPreferredSize(new Dimension(63, 80));
		add(_packageInfo, BorderLayout.NORTH);
		_packageInfo.setText(pack.getInfo());
		_packageInfo.setEditable(false);
		_packageInfo.setFont(new Font("default", 0, 10));
		_packageInfo.setBackground(getBackground());

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
				if (!pack.isManual()) _versionComboBox.setSelectedItem(pack.getLatest());
			}
		});
		panel.add(_manualVersion);

		JLabel lblNewLabel_2 = new JLabel("Update Stream");
		panel.add(lblNewLabel_2);

		_updateStream = new JComboBox<>();
		_updateStream.setModel(new DefaultComboBoxModel<String>(pack.getUpdateStreams().keySet().toArray(new String[pack.getUpdateStreams().keySet().size()])));
		_updateStream.setSelectedItem(pack.getUpdateStreamName(pack.getStream()));
		_updateStream.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				pack.setStream(pack.getUpdateStreamId(_updateStream.getSelectedItem().toString()));
				Version sel = (Version) _versionComboBox.getSelectedItem();
				_versionComboBox.setModel(new DefaultComboBoxModel<Version>(pack.getVisibleVersions()));
				if (pack.isManual()) _versionComboBox.setSelectedItem(sel);
				else _versionComboBox.setSelectedItem(pack.getLatest());
				_versionComboBox.repaint();
			}
		});
		panel.add(_updateStream);

		JLabel lblNewLabel_3 = new JLabel("Version");
		panel.add(lblNewLabel_3);

		_updateButton = new JButton("Update");
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
				if (current == null) _updateButton.setText("Install");
				else if (current.getIntVersion() < _chosen.getIntVersion()) _updateButton.setText("Update");
				else if (current.getIntVersion() > _chosen.getIntVersion()) _updateButton.setText("Downgrade");
				else {
					_updateButton.setEnabled(false);
				}
				_updateButton.repaint();

			}
		});
		if (pack.getVersion() != null) {
			_versionComboBox.setSelectedItem(pack.getVersion());
			_chosen = pack.getVersion();
		} else _chosen = (Version) _versionComboBox.getSelectedItem();
		panel.add(_versionComboBox);

		_updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Version v = (Version) _versionComboBox.getSelectedItem();
					if (v == null) v = pack.getLatest();
					pack.install((Version) v);
				} catch (IOException e1) {
					System.out.println("Failed to install");
				}

			}
		});
		Version current = pack.getVersion();
		_updateButton.setEnabled(true);
		if (current == null) _updateButton.setText("Install");
		else if (current.getIntVersion() < _chosen.getIntVersion()) _updateButton.setText("Update");
		else if (current.getIntVersion() > _chosen.getIntVersion()) _updateButton.setText("Downgrade");
		else {
			_updateButton.setEnabled(false);
		}
		panel.add(_updateButton);

		_launchButton = new JButton("Launch");
		_launchButton.setEnabled(pack.isDownloaded() && !pack.isDisabled() && pack.getRuns().length > 0);
		_launchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.getSingleton().launchRunnable(pack.getRuns()[0]);

			}
		});
		_launchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				doPop(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				doPop(e);
			}

			public void doPop(MouseEvent e) {
				if (!e.isPopupTrigger()) return;
				if (!_launchButton.isEnabled()) return;
				JPopupMenu p = new RunnablePopupMenu(pack);
				p.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		panel.add(_launchButton);

		_disableButton = new JButton(pack.isDisabled() ? "Enable" : "Disable");
		_disableButton.setEnabled(pack.isDownloaded());
		_disableButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (pack.isDisabled()) pack.enable();
				else pack.disable();
			}
		});
		panel.add(_disableButton);

		_removeButton = new JButton("Remove");
		_removeButton.setEnabled(pack.isDownloaded());
		_removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pack.remove();
			}
		});
		panel.add(_removeButton);
	}

	public void packageUpdate() {
		_packageInfo.setText(pack.getInfo());
		_autoUpdate.setSelected(pack.isAuto());
		_removeButton.setEnabled(pack.isDownloaded());
		_disableButton.setEnabled(pack.isDownloaded());
		_disableButton.setText(pack.isDisabled() ? "Enable" : "Disable");
		_launchButton.setEnabled(pack.isDownloaded() && !pack.isDisabled() && pack.getRuns().length > 0);
		_updateButton.setEnabled(true);
		_chosen = (Version) _versionComboBox.getSelectedItem();
		if (pack.getVersion() == null) _updateButton.setText("Install");
		else if (pack.getVersion().getIntVersion() < _chosen.getIntVersion()) _updateButton.setText("Update");
		else if (pack.getVersion().getIntVersion() > _chosen.getIntVersion()) _updateButton.setText("Downgrade");
		else {
			_updateButton.setEnabled(false);
		}
	}

}
