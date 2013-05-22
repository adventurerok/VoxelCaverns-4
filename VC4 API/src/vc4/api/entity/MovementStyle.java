package vc4.api.entity;

public enum MovementStyle {

	WALK(0.22),
	SNEAK(0.065),
	SPRINT(0.3);
	
	private double speed;
	
	private MovementStyle(double tickSpeed) {
		speed = tickSpeed;
	}
	
	public double getSpeed() {
		return speed;
	}
}
