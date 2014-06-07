package vc4.vanilla.entity;

import vc4.api.entity.Entity;
import vc4.api.entity.EntityBlock;
import vc4.api.sound.Audio;
import vc4.api.vbt.TagCompound;
import vc4.api.world.Explosion;
import vc4.api.world.World;

public class EntityBlockExplosive extends EntityBlock {

	public int fuse = 100;
	public float power = 5;
	public float dropChance = 0.3f;
	
	
	public EntityBlockExplosive(World world, int uid, int data) {
		super(world, uid, data);
		
	}

	public EntityBlockExplosive(World world) {
		super(world);
		
	}
	
	@Override
	public Entity addToWorld() {
		Audio.playSound("explosion/fuse", this, 1.0f, 0.9f);
		return super.addToWorld();
	}
	
	@Override
	public boolean sets() {
		return false;
	}
	
	@Override
	public TagCompound getSaveCompound() {
		TagCompound root = super.getSaveCompound();
		root.setInt("fuse", fuse);
		root.setFloat("power", power);
		root.setFloat("drop", dropChance);
		return root;
	}
	
	@Override
	public void loadSaveCompound(TagCompound tag) {
		super.loadSaveCompound(tag);
		fuse = tag.getInt("fuse");
		power = tag.getFloat("power");
		dropChance = tag.getFloat("drop");
	}
	
	
	@Override
	public void update() {
		--fuse;
		if(fuse <= 0){
			Explosion bang = new Explosion(getWorld(), this, position.x, position.y, position.z, power);
			bang.dropChance = dropChance;
			bang.calculateExplosionDamage();
			bang.explode();
			kill();
			return;
		}
		super.update();
	}
	
	

}
