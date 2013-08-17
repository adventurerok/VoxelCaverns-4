package vc4.vanilla.entity;

import vc4.api.entity.EntityLiving;
import vc4.api.math.MathUtils;
import vc4.api.model.Model;
import vc4.api.vector.Vector3f;
import vc4.api.world.World;
import vc4.vanilla.entity.ai.AIAttackPlayer;
import vc4.vanilla.entity.ai.AIWander;

public class EntityZombie extends EntityLiving{

	public EntityZombie(World world) {
		super(world);
		ais.put("attack", new AIAttackPlayer(this, 15, 0.5, 10));
		ais.put("wander", new AIWander(this, 0.5, 15));
	}
	
	
	@Override
	public int getMaxHealth() {
		return 50;
	}
	
	@Override
	public void draw() {
		renderHumanModel("zombie");
	}
	
	@Override
	public void setModelRotations(Model model) {
		double headY = lookYaw() == Double.NaN ? 0 : moveYaw - lookYaw();
		model.setRotation("head", new Vector3f(MathUtils.clamp((float) lookPitch, -30, 30), (float) headY, 0));
		model.setRotation("leftarm", new Vector3f(-90, 0, 0));
		model.setRotation("rightarm", new Vector3f(-90, 0, 0));
		model.setRotation("leftleg", new Vector3f((float) -walkSwing, 0, 0));
		model.setRotation("rightleg", new Vector3f((float) walkSwing, 0, 0));
	}

	@Override
	public String getName() {
		return "vanilla.zombie";
	}

}
