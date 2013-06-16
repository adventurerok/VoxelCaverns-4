package vc4.api.entity.ai;

import vc4.api.entity.Entity;
import vc4.api.path.TargetType;
import vc4.api.vector.Vector3d;

public class Movement {

	boolean pathfind;
	TargetType targetType;
	Entity target;
	Vector3d position;
	double speed = 1;
	
	
	
	public boolean isPathfind() {
		return pathfind;
	}

	public TargetType getTargetType() {
		return targetType;
	}

	public Entity getTarget() {
		return target;
	}

	public Vector3d getPosition() {
		return position;
	}

	public double getSpeed() {
		return speed;
	}

	public Movement(Entity target, double speed, boolean pathfind) {
		super();
		this.target = target;
		this.speed = speed;
		this.pathfind = pathfind;
		this.targetType = TargetType.ENTITY;
		this.position = target.position.clone();
	}

	public Movement(Vector3d position, double speed, boolean pathfind) {
		super();
		this.position = position;
		this.speed = speed;
		this.pathfind = pathfind;
		this.targetType = TargetType.LOCATION;
	}
}
