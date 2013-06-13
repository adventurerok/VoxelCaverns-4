package vc4.api.entity.ai;

import vc4.api.entity.Entity;
import vc4.api.entity.EntityLiving;
import vc4.api.path.*;
import vc4.api.vector.Vector3d;

public class MovementHandler implements Navigator{

	AStarNavigationStrategy astar;
	TargetType type = TargetType.NONE;
	
	double x, y, z, speed;
	boolean update;
	
	EntityLiving entity;
	Entity target;
	
	Vector3d pathPos = null;
	int lastUpdate = 0;
	
	public void targetVector(double x, double y, double z, double speed){
		astar = null;
		type = TargetType.LOCATION;
		this.x = x;
		this.y = y;
		this.z = z;
		this.speed = speed;
		update = true;
	}
	
	public void targetAstarVector(double x, double y, double z, double speed){
		this.x = x;
		this.y = y;
		this.z = z;
		this.speed = speed;
		update = true;
	}
	
	public void pathfindVector(double x, double y, double z, double speed){
		type = TargetType.LOCATION;
		astar = new AStarNavigationStrategy(entity, new Vector3d(x, y, z));
		this.speed = speed;
	}
	
	public void pathfindEntity(Entity entity, double speed){
		type = TargetType.ENTITY;
		target = entity;
		astar = new AStarNavigationStrategy(this.entity, pathPos = target.position.clone());
		this.speed = speed;
		lastUpdate = 0;
	}
	
	
	public void targetEntity(Entity entity, double speed){
		astar = null;
		if(entity == null){
			clearTarget();
			return;
		}
		target = entity;
		type = TargetType.ENTITY;
		this.speed = speed;
	}
	
	public void clearTarget(){
		astar = null;
		type = TargetType.NONE;
		update = false;
		target = null;
	}
	
	public void update(){
		if(type == null || type == TargetType.NONE) return;
		if(type == TargetType.ENTITY){
			if(target == null ||target.isDead){
				clearTarget();
				return;
			}
			if(astar != null){
//				if(lastUpdate > 5 && pathPos.distanceSquared(target.position) > 2){
//					astar = new AStarNavigationStrategy(this.entity, pathPos = target.getEyePos());
//					lastUpdate = 0;
//				}
				++lastUpdate;
				updateVector();
			} else {
				entity.lookTargetEntity(target);
				entity.walk(speed, 0);
				
				if(target.position.y > entity.position.y + 0.1 && target.position.horizontalDistanceSquared(entity.position) < 3){
					entity.jump();
				}
			}
		} else {
			updateVector();
		}
	}
	
	private void updateVector(){
		if(astar != null){
			if(astar.update()){
				return;
			}
		}
		entity.lookTargetVector(new Vector3d(x, y, z));
		entity.walk(speed, 0);
		
		double dx = x - entity.position.x;
		double dz = z - entity.position.z;
		if(y > entity.position.y + 0.1 && dx * dx + dz * dz < 3){
			entity.jump();
		}
	}

	public MovementHandler(EntityLiving entity) {
		super();
		this.entity = entity;
	}
}
