package vc4.api.entity;

import java.util.ArrayList;

import org.jnbt.CompoundTag;

import vc4.api.Resources;
import vc4.api.client.Client;
import vc4.api.entity.ai.AI;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.Graphics;
import vc4.api.graphics.OpenGL;
import vc4.api.math.MathUtils;
import vc4.api.model.Model;
import vc4.api.vector.*;
import vc4.api.world.World;

public abstract class EntityLiving extends Entity {

	static FontRenderer font;

	public double yaw, pitch, forward, sideways;
	public double fallDistance;
	public MovementStyle movement = MovementStyle.WALK;
	public FlyingStyle flying = FlyingStyle.WALK;

	public byte resistanceTime = 20;
	public int resistedDamage = 100000;
	public int healing = 25;
	
	public double walkSwing = 0;
	public double walkSwingSin = 0;
	
	public double headYaw = Double.NaN;
	
	protected ArrayList<AI> ais = new ArrayList<>();

	public EntityLiving(World world) {
		super(world);
		if (font == null) font = FontRenderer.createFontRenderer("unispaced_24", 0.15f);
	}

	@Override
	public CompoundTag getSaveCompound() {
		CompoundTag tag = super.getSaveCompound();
		CompoundTag rot = new CompoundTag("angle");
		rot.setDouble("yaw", yaw);
		rot.setDouble("pitch", pitch);
		tag.addTag(rot);
		tag.setInt("he", healing);
		tag.setByte("movement", (byte) movement.ordinal());
		tag.setByte("flying", (byte) flying.ordinal());
		tag.setDouble("falling", fallDistance);
		return tag;
	}

	@Override
	public void loadSaveCompound(CompoundTag tag) {
		super.loadSaveCompound(tag);
		CompoundTag rot = tag.getCompoundTag("angle");
		yaw = rot.getDouble("yaw");
		pitch = rot.getDouble("pitch");
		healing = tag.getInt("he");
		movement = MovementStyle.values()[tag.getByte("movement")];
		flying = FlyingStyle.values()[tag.getByte("flying")];
		fallDistance = tag.getDouble("falling");
	}

	public void jump() {
		if (flying != FlyingStyle.WALK) {
			motionY += 0.45;
		} else if (onGround) {
			motionY = 0.7;
		}
	}
	
	@Override
	public boolean includeInRayTrace() {
		return true;
	}

	public void sneak() {
		if (flying != FlyingStyle.WALK) {
			motionY -= 0.45;
		} else movement = MovementStyle.SNEAK;
	}

	public double getEyeHeight() {
		return position.y + getDefaultSize().y - 0.15;
	}

	public void setFlying(FlyingStyle flying) {
		this.flying = flying;
	}

	public FlyingStyle getFlying() {
		return flying;
	}

	public void renderHumanModel(String skin) {
		renderHumanModel(skin, null);
	}

	public void setModelRotations(Model model){
		double headY = headYaw == Double.NaN ? 0 : yaw + headYaw;
		model.setRotation("head", new Vector3f(MathUtils.clamp((float) pitch, -30, 30), (float)headY, 0));
		model.setRotation("leftarm", new Vector3f((float)walkSwing, 0, 0));
		model.setRotation("rightarm", new Vector3f((float)-walkSwing, 0, 0));
		model.setRotation("leftleg", new Vector3f((float)-walkSwing, 0, 0));
		model.setRotation("rightleg", new Vector3f((float)walkSwing, 0, 0));
		//System.out.println(walkSwing);
	}
	
	public void renderHumanModel(String skin, String name) {
		OpenGL gl = Graphics.getOpenGL();
		gl.bindShader("model");
		gl.shaderUniform1f("modelIndex", Resources.getAnimatedTexture("human").getArrayIndex(skin));
		Vector4f colorMod = new Vector4f(1, 1, 1, 1);
		if(resistanceTime > 0) colorMod.y = colorMod.z = 0;
		gl.shaderUniform4f("colorMod", colorMod.x, colorMod.y, colorMod.z, colorMod.w);
		Resources.getAnimatedTexture("human").bind();
		Model model = Resources.getModel("human");
		model.reset();
		setModelRotations(model);
		gl.pushMatrix();
		gl.translate(position.x, position.y, position.z);
		gl.pushMatrix();
		gl.rotate((float) -yaw, 0, 1, 0);
		model.draw();
		gl.popMatrix();
		if (name != null && !name.isEmpty()) {
			gl.translate(0, size.y + 0.25, 0);
			EntityPlayer plr = Client.getPlayer();
			Vector2d myGrid = new Vector2d(position.x, position.z);
			Vector2d plrGrid = new Vector2d(plr.position.x, plr.position.z);
			double textYaw = myGrid.angle(plrGrid);
			textYaw = Math.toDegrees(textYaw);
			gl.rotate((float) -textYaw + 90, 0, 1, 0);
			double textPitch = new Vector2d(0, position.y).angle(new Vector2d(plrGrid.distance(myGrid), plr.position.y));
			textPitch = Math.toDegrees(textPitch);
			gl.rotate((float) (180 - textPitch), 1, 0, 0);
			name = "{f:b}" + name;
			Vector2f textSize = font.measureString(name, 0.15f);
			font.resetStyles();
			font.renderString(-(textSize.x / 2), 0, name);
		}
		gl.popMatrix();
		gl.unbindShader();
	}

	public Vector3d getEyePos() {
		return new Vector3d(position.x, getEyeHeight(), position.z);
	}

	@Override
	public void damage(int amount, DamageSource source) {
		amount = reduceDamage(source, amount);
		if (resistanceTime > 0) {
			if (amount > resistedDamage) amount -= resistedDamage;
			else return;
		}
		if (amount < 1) return;
		super.damage(amount, source);
		resistanceTime = 5;
	}

	public void heal(int amount) {
		health += amount;
		if (health > getMaxHealth()) health = getMaxHealth();
	}

	// Useful for walking with delta
	public void walk(double forward, double sideways) {
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

	public int getMaxHealing() {
		return 25;
	}

	@Override
	public void update() {
		updateAge();
		updateTraits();
		updateAIs();
		move();
	}

	@Override
	public void updateAge() {
		super.updateAge();
		if (resistanceTime > 0) --resistanceTime;
		else resistedDamage = 0;
	}
	
	public void updateAIs(){
		for(AI i : ais){
			if(!i.isRunning()){
				if(!i.shouldStart()) continue;
				i.setRunning(true);
				i.start();
			} else {
				i.setRunning(i.update());
			}
		}
	}

	public void move() {
		motionX *= 0.6;
		motionZ *= 0.6;
		if (flying == FlyingStyle.WALK) {
			motionY -= world.getFallAcceleration();
			if (motionY < -world.getFallMaxSpeed()) motionY = -world.getFallMaxSpeed();
		} else {
			motionY *= 0.6;
		}
		if (!onGround) {
			if (flying == FlyingStyle.WALK) {
				fallDistance += Math.max(0, oldPos.y - position.y);
				if (fallDistance > 512 && motionY <= -world.getFallMaxSpeed()) {
					double base = fallDistance - 512;
					damage((int) base, DamageSource.fallDamage, 5);
				}
			} else fallDistance = -256;
		} else {
			if (fallDistance > 4) {
				damage((int) (3.75 * fallDistance), DamageSource.fallDamage);
			}
			fallDistance = 0;
		}
		move(motionX, motionY, motionZ);
		double moved = position.horizontalDistance(oldPos);
		if(moved < 0.005){
			walkSwing *= 0.5;
			//walkSwingSin = 0;
		}
		else {
			moved *= 100;
			walkSwingSin += moved;
			walkSwing = MathUtils.sin((float) moved) * 85;
		}
		if (collisionHorizontal && movement == MovementStyle.SPRINT) movement = MovementStyle.WALK;
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

	public int reduceDamage(DamageSource source, int damage) {
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
