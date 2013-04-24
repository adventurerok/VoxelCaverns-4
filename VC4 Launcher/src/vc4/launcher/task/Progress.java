package vc4.launcher.task;

public class Progress {

	
	private volatile String text;
	private volatile double percent;
	private boolean delete;
	private Progress feeding;
	
	public void setFeeding(Progress feeding) {
		this.feeding = feeding;
	}
	
	public String getText() {
		return text;
	}
	
	public double getPercent() {
		return percent;
	}
	
	public void setText(String text) {
		this.text = text;
		if(feeding != null) feeding.setText(text);
	}
	
	public void setPercent(double percent) {
		this.percent = percent;
		if(feeding != null) feeding.calcPercent();
	}
	
	public void calcPercent(){
		
	}
	
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	
	public boolean isDelete() {
		return delete;
	}
}
