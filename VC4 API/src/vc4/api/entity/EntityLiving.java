package vc4.api.entity;

import java.util.HashMap;

import vc4.api.Resources;
import vc4.api.VoxelCaverns;
import vc4.api.client.Client;
import vc4.api.entity.ai.AI;
import vc4.api.entity.ai.MovementHandler;
import vc4.api.font.FontRenderer;
import vc4.api.graphics.Graphics;
import vc4.api.graphics.OpenGL;
import vc4.api.item.ItemStack;
import vc4.api.math.MathUtils;
import vc4.api.model.Model;
import vc4.api.vbt.TagCompound;
import vc4.api.vector.*;
import vc4.api.world.World;

public abstract class EntityLiving extends Entity {

	static FontRenderer font;

	public double moveYaw, movePitch, forward, sideways, lookYaw, lookPitch;
	public double fallDistance;
	public MovementStyle movement = MovementStyle.WALK;
	public FlyingStyle flying = FlyingStyle.WALK;
	public boolean looking = false;

	public byte resistanceTime = 20;
	public int resistedDamage = 100000;
	public int healing = 25;

	public double walkSwing = 0;
	public double walkSwingSin = 0;

	MovementHandler moveHandler;

	protected HashMap<String, AI> ais = new HashMap<>();

	public EntityLiving(World world) {
		super(world);
		moveHandler = new MovementHandler(this);
		if (font == null && VoxelCaverns.hasGraphics()) font = FontRenderer.createFontRenderer("unispaced_24", 0.15f);
	}

	public void dropItem(ItemStack drop) {
		if (drop == null || !drop.exists()) return;
		Vector3d epos = new Vector3d(bounds.averageX(), getEyeHeight(), bounds.averageZ());
		EntityItem i = new EntityItem(world);
		i.setPosition(epos);
		i.setItem(drop);
		i.motionX = (rand.nextDouble() - 0.5) * 2;
		i.motionY = rand.nextDouble();
		i.motionZ = (rand.nextDouble() - 0.5) * 2;
		i.addToWorld();
	}

	public void lookAtEntity(Entity entity) {
		double xDif = entity.position.x - position.x;
		double zDif = entity.position.z - position.z;
		double yDif;

		if (entity instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving) entity;
			yDif = (getEyeHeight()) - entityliving.getEyeHeight();
		} else {
			yDif = entity.position.y - getEyeHeight();
		}

		double dist = Math.sqrt(xDif * xDif + zDif * zDif);
		float newYaw = (float) ((Math.atan2(zDif, xDif) * 180D) / Math.PI) - 90F;
		float newPitch = (float) (-((Math.atan2(yDif, dist) * 180D) / Math.PI));
		lookPitch = -newPitch;
		lookYaw = newYaw;
	}

	public void throwItem(ItemStack item) {
		if (item == null || !item.exists()) return;
		double pitch = lookPitch();
		double yaw = lookYaw();

		double power = 2;

		EntityItem drop = new EntityItem(world);
		drop.setItem(item);
		drop.setPosition(getEyePos().clone());

		drop.motionY += 1.4 * Math.tan(Math.toRadians(pitch));
		drop.motionX -= power * Math.sin(Math.toRadians(yaw));
		drop.motionZ += power * Math.cos(Math.toRadians(yaw));
		drop.addToWorld();
	}

	public void targetEntity(Entity entity) {
		double xDif = entity.position.x - position.x;
		double zDif = entity.position.z - position.z;
		double yDif;

		if (entity instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving) entity;
			yDif = (getEyeHeight()) - entityliving.getEyeHeight();
		} else {
			yDif = entity.position.y - getEyeHeight();
		}

		double dist = Math.sqrt(xDif * xDif + zDif * zDif);
		float newYaw = (float) ((Math.atan2(zDif, xDif) * 180D) / Math.PI) - 90F;
		float newPitch = (float) (-((Math.atan2(yDif, dist) * 180D) / Math.PI));
		movePitch = -newPitch;
		moveYaw = newYaw;
	}

	public void lookTargetEntity(Entity entity) {
		double xDif = entity.position.x - position.x;
		double zDif = entity.position.z - position.z;
		double yDif;

		if (entity instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving) entity;
			yDif = (getEyeHeight()) - entityliving.getEyeHeight();
		} else {
			yDif = entity.position.y - getEyeHeight();
		}

		double dist = Math.sqrt(xDif * xDif + zDif * zDif);
		float newYaw = (float) ((Math.atan2(zDif, xDif) * 180D) / Math.PI) - 90F;
		float newPitch = (float) (-((Math.atan2(yDif, dist) * 180D) / Math.PI));
		movePitch = lookPitch = -newPitch;
		moveYaw = lookYaw = newYaw;
	}

	public void lookAtVector(Vector3d vector) {
		double xDif = vector.x - position.x;
		double zDif = vector.z - position.z;
		double yDif = vector.y - getEyeHeight();
		double dist = Math.sqrt(xDif * xDif + zDif * zDif);
		float newYaw = (float) ((Math.atan2(zDif, xDif) * 180D) / Math.PI) - 90F;
		float newPitch = (float) (-((Math.atan2(yDif, dist) * 180D) / Math.PI));
		lookPitch = -newPitch;
		lookYaw = newYaw;
	}

	public double lookPitch() {
		return looking ? lookPitch : movePitch;
	}

	public double lookYaw() {
		return looking ? lookYaw : moveYaw;
	}

	public void lookTargetVector(Vector3d vector) {
		double xDif = vector.x - position.x;
		double zDif = vector.z - position.z;
		double yDif = vector.y - getEyeHeight();
		double dist = Math.sqrt(xDif * xDif + zDif * zDif);
		float newYaw = (float) ((Math.atan2(zDif, xDif) * 180D) / Math.PI) - 90F;
		float newPitch = (float) (-((Math.atan2(yDif, dist) * 180D) / Math.PI));
		lookPitch = movePitch = -newPitch;
		lookYaw = moveYaw = newYaw;
	}

	public void targetVector(Vector3d vector) {
		double xDif = vector.x - position.x;
		double zDif = vector.z - position.z;
		double yDif = vector.y - getEyeHeight();
		double dist = Math.sqrt(xDif * xDif + zDif * zDif);
		float newYaw = (float) ((Math.atan2(zDif, xDif) * 180D) / Math.PI) - 90F;
		float newPitch = (float) (-((Math.atan2(yDif, dist) * 180D) / Math.PI));
		movePitch = -newPitch;
		moveYaw = newYaw;
	}

	@Override
	public TagCompound getSaveCompound() {
		TagCompound tag = super.getSaveCompound();
		TagCompound rot = new TagCompound("angle");
		rot.setDouble("yaw", moveYaw);
		rot.setDouble("pitch", movePitch);
		tag.addTag(rot);
		tag.setInt("he", healing);
		tag.setByte("movement", (byte) movement.ordinal());
		tag.setByte("flying", (byte) flying.ordinal());
		tag.setDouble("falling", fallDistance);
		return tag;
	}

	@Override
	public void loadSaveCompound(TagCompound tag) {
		super.loadSaveCompound(tag);
		TagCompound rot = tag.getCompoundTag("angle");
		moveYaw = rot.getDouble("yaw");
		movePitch = rot.getDouble("pitch");
		healing = tag.getInt("he");
		movement = MovementStyle.values()[tag.getByte("movement")];
		flying = FlyingStyle.values()[tag.getByte("flying")];
		fallDistance = tag.getDouble("falling");
	}

	public MovementHandler getMoveHandler() {
		return moveHandler;
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

	@Override
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

	public void setModelRotations(Model model) {
		double headY = lookYaw() == Double.NaN ? 0 : moveYaw - lookYaw();
		model.setRotation("head", new Vector3f(MathUtils.clamp((float) lookPitch, -30, 30), (float) headY, 0));
		model.setRotation("leftarm", new Vector3f((float) walkSwing, 0, 0));
		model.setRotation("rightarm", new Vector3f((float) -walkSwing, 0, 0));
		model.setRotation("leftleg", new Vector3f((float) -walkSwing, 0, 0));
		model.setRotation("rightleg", new Vector3f((float) walkSwing, 0, 0));
		// System.out.println(walkSwing);
	}

	public void renderHumanModel(String skin, String name) {
		OpenGL gl = Graphics.getOpenGL();
		gl.bindShader("model");
		gl.shaderUniform1f("modelIndex", Resources.getAnimatedTexture("human").getArrayIndex(skin));
		Vector4f colorMod = new Vector4f(1, 1, 1, 1);
		if (resistanceTime > 0) colorMod.y = colorMod.z = 0;
		gl.shaderUniform4f("colorMod", colorMod.x, colorMod.y, colorMod.z, colorMod.w);
		Resources.getAnimatedTexture("human").bind();
		Model model = Resources.getModel("human");
		model.reset();
		setModelRotations(model);
		gl.pushMatrix();
		gl.translate(position.x, position.y, position.z);
		gl.pushMatrix();
		gl.rotate((float) -moveYaw, 0, 1, 0);
		model.draw();
		gl.popMatrix();
		if (name != null && !name.isEmpty() && Client.getGame().guiVisible()) {
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

	public void renderHumanModel(String skin, String name, String mt) {
		OpenGL gl = Graphics.getOpenGL();
		gl.bindShader("model_mt");
		gl.shaderUniform1f("modelIndex", Resources.getAnimatedTexture("human").getArrayIndex(skin));
		gl.shaderUniform1f("mtIndex", Resources.getAnimatedTexture("human").getArrayIndex(mt));
		Vector4f colorMod = new Vector4f(1, 1, 1, 1);
		if (resistanceTime > 0) colorMod.y = colorMod.z = 0;
		gl.shaderUniform4f("colorMod", colorMod.x, colorMod.y, colorMod.z, colorMod.w);
		Resources.getAnimatedTexture("human").bind();
		Model model = Resources.getModel("human");
		model.reset();
		setModelRotations(model);
		gl.pushMatrix();
		gl.translate(position.x, position.y, position.z);
		gl.pushMatrix();
		gl.rotate((float) -moveYaw, 0, 1, 0);
		model.draw();
		gl.popMatrix();
		if (name != null && !name.isEmpty() && Client.getGame().guiVisible()) {
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
		double xmotion = -forward * movement.getSpeed() * fMod * Math.sin(Math.toRadians(moveYaw));
		double zmotion = forward * movement.getSpeed() * fMod * Math.cos(Math.toRadians(moveYaw));
		xmotion += sideways * movement.getSpeed() * fMod * Math.sin(Math.toRadians(moveYaw - 90));
		zmotion -= sideways * movement.getSpeed() * fMod * Math.cos(Math.toRadians(moveYaw - 90));
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
		preMove();
		updateSurroundings();
		updateEffects();
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

	public void updateAIs() {
		outside: for (AI i : ais.values()) {
			if (i.isDisabled()) continue;
			if (!i.isRunning()) {
				for (AI j : ais.values()) {
					if (i == j) continue;
					if (!j.isRunning() || j.isDisabled()) continue;
					int cf = i.conflictId() & j.conflictId();
					if (cf == 0) continue;
					if (j.priority() >= i.priority()) continue outside;

				}
				if (!i.shouldStart()) continue;
				for (AI j : ais.values()) {
					if (i == j) continue;
					if (!j.isRunning() || j.isDisabled()) continue;
					int cf = i.conflictId() & j.conflictId();
					if (cf == 0) continue;
					if (i.priority() > j.priority()) {
						j.stop();
						j.setRunning(false);
					} else continue outside;

				}
				i.setRunning(true);
				i.start();
			} else {
				if (!i.update()) {
					i.stop();
					i.setRunning(false);
				}
			}
		}
	}

	public void preMove() {
		moveHandler.update();
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
	}

	public void move() {
		move(motionX, motionY, motionZ);
		double moved = position.horizontalDistance(oldPos);
		if (moved < 0.005) {
			walkSwing *= 0.5;
			// walkSwingSin = 0;
		} else {
			moved *= 5;
			walkSwingSin += moved;
			walkSwing = MathUtils.sin((float) walkSwingSin) * 85;
		}
		if (collisionHorizontal && movement == MovementStyle.SPRINT) movement = MovementStyle.WALK;
	}

	boolean sneaking;

	protected int knockback;

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

	public void setHorizontalVelocity(double x, double z) {
		if (knockback > 0) return;
		motionX = x;
		motionZ = z;
	}
}
