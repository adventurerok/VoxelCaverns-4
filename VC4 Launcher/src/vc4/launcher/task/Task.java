package vc4.launcher.task;

public interface Task {

	
	public void run(Progress progress);
	public boolean canRun();
}
