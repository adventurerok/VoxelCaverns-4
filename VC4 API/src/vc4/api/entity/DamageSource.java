package vc4.api.entity;

import vc4.api.item.ItemStack;
import vc4.api.vector.Vector3l;

public class DamageSource {

	private final String name;
	private final int id;
	private boolean defendable = true;

	private Vector3l blockPos;

	private EntityLiving attacker;
	private ItemStack weapon;
	private Entity projectile;
	private String itemName;

	public static final DamageSource fallDamage = new DamageSource("falling", 0);
	public static final DamageSource fireDamage = new DamageSource("fire", 2);
	public static final DamageSource explosionDamage = new DamageSource("explosion", 7);

	public static final DamageSource melee(EntityLiving attacker, ItemStack weapon) {
		return new DamageSource("melee", 1).setAttacker(attacker).setWeapon(weapon);
	}

	public static final DamageSource liquid(String name) {
		return new DamageSource("liquid", 3).setItemName(name);
	}

	public DamageSource(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}

	public EntityLiving getAttacker() {
		return attacker;
	}

	public String getItemName() {
		return itemName;
	}

	public Vector3l getBlockPos() {
		return blockPos;
	}

	public int getId() {
		return id;
	}

	public ItemStack getWeapon() {
		return weapon;
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

	public DamageSource setWeapon(ItemStack weapon) {
		this.weapon = weapon;
		return this;
	}

	public DamageSource setProjectile(Entity projectile) {
		this.projectile = projectile;
		return this;
	}

	public DamageSource setItemName(String name) {
		this.itemName = name;
		return this;
	}

}
