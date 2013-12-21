package vc4.launcher.task;

import javax.swing.SwingUtilities;

public class UpdateGuiTask implements Task {

	Runnable updateGui;
	
	
	
	public UpdateGuiTask(Runnable updateGui) {
		super();
		this.updateGui = updateGui;
	}

	@Override
	public void run(Progress progress) {
		System.out.println("Updating GUI");
		SwingUtilities.invokeLater(updateGui);
		progress.setPercent(100);
		progress.setDelete(true);

	}

	@Override
	public boolean canRun() {
		return true;
	}

}
