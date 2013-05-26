package vc4.api.entity;

import vc4.api.vector.Vector3d;
import vc4.api.world.World;

public class EntityLiving extends Entity {

	public double yaw, pitch, forward, sideways;
	public double fallDistance;
	public MovementStyle movement = MovementStyle.WALK;
	public FlyingStyle flying = FlyingStyle.WALK;
	
	public byte resistanceTime = 20;
	public int resistedDamage = 100000;
	public int healing = 25;
	
	
	public EntityLiving(World world) {
		super(world);
		// TASK Auto-generated constructor stub
	}
	
	public void jump(){
		if(flying != FlyingStyle.WALK){
			motionY += 0.45;
		} else if(onGround){
			motionY = 0.7;
		}
	}
	
	public void sneak(){
		if(flying != FlyingStyle.WALK){
			motionY -= 0.45;
		} else movement = MovementStyle.SNEAK;
	}
	
	public double getEyeHeight(){
		return position.y + getDefaultSize().y - 0.15;
	}
	
	public void setFlying(FlyingStyle flying) {
		this.flying = flying;
	}
	
	
	
	public FlyingStyle getFlying() {
		return flying;
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
		double fMod = flying != FlyingStyle.WALK ? 2.2 : 1;
		double xmotion = -forward * movement.getSpeed() * fMod * Math.sin(Math.toRadians(yaw));
		double zmotion = forward * movement.getSpeed() * fMod * Math.cos(Math.toRadians(yaw));
		xmotion += sideways * movement.getSpeed() * fMod * Math.sin(Math.toRadians(yaw - 90));
		zmotion -= sideways * movement.getSpeed() * fMod * Math.cos(Math.toRadians(yaw - 90));
		motionX += xmotion;
		motionZ += zmotion;
	}
	
	public int getHealing() {
		return healing;
	}
	
	public int getMaxHealing(){
		return 25;
	}
	
	
	@Override
	public void update() {
		updateAge();
		move();
	}
	
	@Override
	public void updateAge() {
		super.updateAge();
		if(resistanceTime > 0) --resistanceTime;
		else resistedDamage = 0;
	}
	
	public void move(){
		motionX *= 0.6;
		motionZ *= 0.6;
		if(flying == FlyingStyle.WALK){
			motionY -= world.getFallAcceleration();
			if(motionY < -world.getFallMaxSpeed()) motionY = -world.getFallMaxSpeed();
		} else{
			motionY *= 0.6;
		}
		if(!onGround){
			if(flying == FlyingStyle.WALK){
				fallDistance += Math.max(0, oldPos.y - position.y);
				if(fallDistance > 512 && motionY <= -world.getFallMaxSpeed()){
					double base = fallDistance - 512;
					damage((int) base, DamageSource.fallDamage, 5);
				}
			}
		} else{
			if(fallDistance > 4){
				damage((int) (3.75 * fallDistance), DamageSource.fallDamage);
			}
			fallDistance = 0;
		}
		move(motionX, motionY, motionZ);
		if(flying == FlyingStyle.FLY && onGround){
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
	
	@Override
	public boolean noClip() {
		return flying == FlyingStyle.NOCLIP;
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
