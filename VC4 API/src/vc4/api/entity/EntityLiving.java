package vc4.api.entity;

import vc4.api.vector.Vector3d;
import vc4.api.world.World;

public class EntityLiving extends Entity {

	public double yaw, pitch;
	
	public EntityLiving(World world) {
		super(world);
		// TASK Auto-generated constructor stub
	}
	
	public void jump(){
		if(onGround){
			motionY = 0.8;
		}
	}
	
	public double getEyeHeight(){
		return position.y + getDefaultSize().y - 0.15;
	}
	
	public Vector3d getEyePos(){
		return new Vector3d(position.x, getEyeHeight(), position.z);
	}
	
	@Override
	public void update() {
		motionX *= 0.6;
		motionZ *= 0.6;
		motionY -= world.getFallAcceleration();
		if(motionY < -world.getFallMaxSpeed()) motionY = -world.getFallMaxSpeed();
		super.update();
	}

	boolean sneaking;
	
	public boolean isSneaking() {
		return sneaking;
	}
	
	public void setSneaking(boolean sneaking) {
		this.sneaking = sneaking;
	}
}
