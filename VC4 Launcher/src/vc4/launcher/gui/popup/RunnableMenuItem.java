package vc4.launcher.gui.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import vc4.launcher.Launcher;
import vc4.launcher.repo.Runnable;

public class RunnableMenuItem extends JMenuItem implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2757561468492361096L;

	Runnable runner;

	public RunnableMenuItem(Runnable runner) {
		super(runner.getName());
		this.runner = runner;
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Launcher.getSingleton().launchRunnable(runner);
	}

}
