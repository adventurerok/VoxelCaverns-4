package vc4.launcher.task;

import java.util.ArrayList;

public class TaskSystem extends Thread{

	
	private ArrayList<ListedTask> tasks = new ArrayList<>();
	private ListedTask current;
	private boolean stop;
	private MainProgress overall = new MainProgress();
	
	@Override
	public void run() {
		while(!stop){
			update();
		}
	}
	
	public void update(){
		if(current == null && tasks.size() < 1){
			overall.setText("Complete");
			overall.setPercent(100);
			return;
		}
		if(current == null || !current.canRun() || current.delete()){
			tasks.add(current);
			current = getNext();
			if(current == null) return;
		}
		current.getProgress().setFeeding(overall);
		current.run();
		current.getProgress().setFeeding(null);
		if(current.delete()){
			current = null;
		}
	}
	
	public Progress getOverall() {
		return overall;
	}

	private ListedTask getNext() {
		ListedTask ta = null;
		for(ListedTask t : tasks){
			if(t.canRun()){
				ta = t;
				break;
			}
		}
		if(ta != null) tasks.remove(ta);
		return ta;
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public double calcPercent() {
		int total = 0;
		double current = 0;
		for(ListedTask t : tasks){
			++total;
			current += t.getProgress().getPercent();
		}
		
		return current / total;
	}
}
