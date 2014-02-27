package vc4.launcher.task;

public class ListedTask {

	private Task task;
	private Progress progress = new Progress();

	public ListedTask(Task task) {
		super();
		this.task = task;
	}

	public Task getTask() {
		return task;
	}

	public boolean delete() {
		return progress.isDelete();
	}

	public boolean canRun() {
		return task.canRun();
	}

	public void run() {
		task.run(progress);
	}

	public Progress getProgress() {
		return progress;
	}
}
