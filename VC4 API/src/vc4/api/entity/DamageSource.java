package vc4.api.entity;

import vc4.api.vector.Vector3l;

public class DamageSource {

	private final String name;
	private final int id;
	private boolean defendable = true;

	private Vector3l blockPos;

	private EntityLiving attacker;

	private Entity projectile;
	
	public static final DamageSource fallDamage = new DamageSource("falling", 0);

	public DamageSource(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}

	public EntityLiving getAttacker() {
		return attacker;
	}

	public Vector3l getBlockPos() {
		return blockPos;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Entity getProjectile() {
		return projectile;
	}
	
	public boolean isDefendable() {
		return defendable;
	}

	public DamageSource setDefendable(boolean defendable) {
		this.defendable = defendable;
		return this;
	}

	public DamageSource setBlockPos(Vector3l blockPos) {
		this.blockPos = blockPos;
		return this;
	}

	public DamageSource setAttacker(EntityLiving attacker) {
		this.attacker = attacker;
		return this;
	}

	public DamageSource setProjectile(Entity projectile) {
		this.projectile = projectile;
		return this;
	}
	
	
	
	
}
