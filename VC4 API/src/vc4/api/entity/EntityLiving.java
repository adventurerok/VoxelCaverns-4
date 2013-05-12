package vc4.api.entity;

import vc4.api.vector.Vector3d;
import vc4.api.world.World;

public class EntityLiving extends Entity {

	public double yaw, pitch, forward, sideways;
	public double fallDistance;
	public MovementStyle movement = MovementStyle.WALK;
	
	public byte resistanceTime = 20;
	public int resistedDamage = 100000;
	
	
	public EntityLiving(World world) {
		super(world);
		// TASK Auto-generated constructor stub
	}
	
	public void jump(){
		if(onGround || movement == MovementStyle.FLY){
			motionY = 0.8;
		}
	}
	
	public void sneak(){
		if(movement == MovementStyle.FLY){
			motionY = -0.8;
		} else movement = MovementStyle.SNEAK;
	}
	
	public double getEyeHeight(){
		return position.y + getDefaultSize().y - 0.15;
	}
	
	
	public Vector3d getEyePos(){
		return new Vector3d(position.x, getEyeHeight(), position.z);
	}
	
	@Override
	public void damage(int amount, DamageSource source) {
		amount = reduceDamage(source, amount);
		if(resistanceTime > 0){
			if(amount > resistedDamage) amount -= resistedDamage;
			else return;
		}
		if(amount < 1) return;
		super.damage(amount, source);
		resistanceTime = 5;
	}
	
	public void heal(int amount){
		health += amount;
		if(health > getMaxHealth()) health = getMaxHealth();
	}
	
	//Useful for walking with delta
	public void walk(double forward, double sideways){
		double xmotion = -forward * movement.getSpeed() * Math.sin(Math.toRadians(yaw));
		double zmotion = forward * movement.getSpeed() * Math.cos(Math.toRadians(yaw));
		xmotion += sideways * movement.getSpeed() * Math.sin(Math.toRadians(yaw - 90));
		zmotion -= sideways * movement.getSpeed() * Math.cos(Math.toRadians(yaw - 90));
		motionX += xmotion;
		motionZ += zmotion;
	}
	
	
	@Override
	public void update() {
		getOlder();
		move();
	}
	
	@Override
	public void getOlder() {
		super.getOlder();
		if(resistanceTime > 0) --resistanceTime;
		else resistedDamage = 0;
	}
	
	public void move(){
		motionX *= 0.6;
		motionZ *= 0.6;
		if(movement != MovementStyle.FLY){
			motionY -= world.getFallAcceleration();
			if(motionY < -world.getFallMaxSpeed()) motionY = -world.getFallMaxSpeed();
		} else{
			motionY *= 0.6;
		}
		if(!onGround){
			fallDistance += Math.max(0, oldPos.y - position.y);
			if(fallDistance > 512 && motionY <= -world.getFallMaxSpeed()){
				double base = fallDistance - 512;
				damage((int) base, DamageSource.fallDamage, 5);
			}
		} else{
			if(fallDistance > 4){
				damage((int) (4.5 * fallDistance), DamageSource.fallDamage);
			}
			fallDistance = 0;
		}
		move(motionX, motionY, motionZ);
		if(movement == MovementStyle.FLY && onGround){
			movement = MovementStyle.WALK;
		}
		if(collisionHorizontal && movement == MovementStyle.SPRINT) movement = MovementStyle.WALK; 
	}

	boolean sneaking;
	
	public boolean isSneaking() {
		return movement == MovementStyle.SNEAK;
	}
	
	public void setMovement(MovementStyle movement) {
		this.movement = movement;
	}
	
	public MovementStyle getMovement() {
		return movement;
	}
	
	public int reduceDamage(DamageSource source, int damage){
		return damage;
	}
	
	@Override
	public void teleport(Vector3d pos) {
		super.teleport(pos);
		fallDistance = 0;
		resistanceTime = 20;
		resistedDamage = 100000;
	}
}
