package vc4.launcher.task;

import javax.swing.SwingUtilities;

import vc4.launcher.Launcher;

public class MainProgress extends Progress {

	private TaskSystem tasks;

	public MainProgress(TaskSystem tasks) {
		super();
		this.tasks = tasks;
	}

	@Override
	public void setText(String text) {
		if (text.equals(this.getText())) return;
		super.setText(text);
		final String s = text;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					Launcher.getSingleton().getGui().getProgressBar().setString(s);
				} catch (Exception e) {
				}

			}
		});
	}

	@Override
	public void setPercent(double percent) {
		if (this.getPercent() == percent) return;
		super.setPercent(percent);
		final int per = (int) percent;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					Launcher.getSingleton().getGui().getProgressBar().setValue(per);
					Launcher.getSingleton().getTasks().setUpdated(true);
				} catch (Exception e) {
				}

			}
		});
	}

	@Override
	public void calcPercent() {
		setPercent(tasks.calcPercent());
	}

}
