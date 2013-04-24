package vc4.launcher.task;

public class MainProgress extends Progress{

	private TaskSystem tasks;
	
	@Override
	public void calcPercent() {
		setPercent(tasks.calcPercent());
	}
	
}
