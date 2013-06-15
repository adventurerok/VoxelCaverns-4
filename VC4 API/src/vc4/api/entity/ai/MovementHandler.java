package vc4.api.entity.ai;

import vc4.api.entity.EntityLiving;
import vc4.api.path.*;
import vc4.api.vector.Vector3d;

public class MovementHandler implements Navigator{

	AStarNavigationStrategy astar;
	TargetType type = TargetType.NONE;
	
	
	EntityLiving entity;
	
	private Vector3d pathPos = null;
	private int lastUpdate = 0;
	private int tickDown = 0;
	
	Movement executing;
	
	public void setExecuting(Movement movement){
		executing = movement;
		if(movement == null || !movement.isPathfind()) astar = null;
		else if(movement != null && movement.isPathfind()){
			if(movement.getTargetType() == TargetType.LOCATION) astar = new AStarNavigationStrategy(entity, movement.position.clone());
			else astar = new AStarNavigationStrategy(entity, movement.getTarget().position.clone());
		}
	}
	
	public void targetAstarVector(double x, double y, double z, double speed){
		executing.position = new Vector3d(x, y, z);
		executing.speed = speed;
	}
	
	
	public void clearExecuting(){
		astar = null;
		executing = null;
	}
	
	public Movement getExecuting() {
		return executing;
	}
	
	public boolean isExecuting(){
		return executing != null;
	}
	
	public boolean isExecuting(Movement movement){
		return executing == movement;
	}
	
	public void update(){
		if(executing == null) return;
		if(executing.getTargetType() == TargetType.ENTITY){
			if(executing.getTarget() == null || executing.getTarget().isDead){
				clearExecuting();
				return;
			}
			if(astar != null){
				if(lastUpdate > 5 && pathPos.distanceSquared(executing.getTarget().position) > 2){
					astar = new AStarNavigationStrategy(this.entity, pathPos = executing.getTarget().position.clone());
					lastUpdate = 0;
				}
				++lastUpdate;
				updateVector();
			} else {
				entity.lookTargetEntity(executing.getTarget());
				entity.walk(executing.getSpeed(), 0);
				
				if(executing.getTarget().position.y > entity.position.y + 0.1 && executing.getTarget().position.horizontalDistanceSquared(entity.position) < 3){
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
				if(tickDown == -1){
					tickDown = 4;
				} else if(tickDown == 0){
					astar = null;
				}
				--tickDown;
			} else tickDown = -1;
		} else tickDown = -1;
		entity.lookTargetVector(executing.getPosition());
		entity.walk(executing.getSpeed(), 0);
		
		double dx = executing.getPosition().x - entity.position.x;
		double dz = executing.getPosition().z - entity.position.z;
		if(executing.getPosition().y > entity.position.y + 0.25 && dx * dx + dz * dz < 3){
			entity.jump();
		}
	}

	public MovementHandler(EntityLiving entity) {
		super();
		this.entity = entity;
	}
}
