package vc4.launcher.task;

import java.util.ArrayList;

public class TaskSystem extends Thread {

	private ArrayList<ListedTask> tasks = new ArrayList<>();
	private ListedTask current;
	private boolean stop;
	private MainProgress overall;
	private boolean wasComplete = false;

	public TaskSystem() {
		overall = new MainProgress(this);
	}

	@Override
	public void run() {
		while (!stop) {
			update();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	public void update() {
		if (current == null && tasks.size() < 1) {
			if (!wasComplete) {
				overall.setText("Complete");
				overall.setPercent(100);
			}
			return;
		}
		if (current == null || !current.canRun() || current.delete()) {
			if (current != null) tasks.add(current);
			current = getNext();
			if (current == null) return;
		}
		current.getProgress().setFeeding(overall);
		current.run();
		current.getProgress().setFeeding(null);
		if (current.delete()) {
			current = null;
		}
		wasComplete = false;
	}

	public Progress getOverall() {
		return overall;
	}

	private ListedTask getNext() {
		ListedTask ta = null;
		for (ListedTask t : tasks) {
			if (t == null) continue;
			if (t.canRun()) {
				ta = t;
				break;
			}
		}
		if (ta != null) tasks.remove(ta);
		return ta;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public void addTask(Task t) {
		tasks.add(new ListedTask(t));
	}

	public double calcPercent() {
		int total = 0;
		double current = 0;
		for (ListedTask t : tasks) {
			if (t == null) continue;
			++total;
			current += t.getProgress().getPercent();
		}
		if(this.current != null){
			++total;
			current += this.current.getProgress().getPercent();
		}

		return current / (double) total;
	}

	public void setUpdated(boolean b) {
		wasComplete = true;
		
	}
}
