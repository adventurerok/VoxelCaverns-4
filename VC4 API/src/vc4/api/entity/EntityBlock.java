package vc4.api.entity;

import java.util.HashMap;

import vc4.api.Resources;
import vc4.api.block.Block;
import vc4.api.block.IBlockFallable;
import vc4.api.graphics.Graphics;
import vc4.api.graphics.OpenGL;
import vc4.api.item.ItemStack;
import vc4.api.render.DataRenderer;
import vc4.api.util.Direction;
import vc4.api.vbt.TagCompound;
import vc4.api.vector.*;
import vc4.api.world.World;

public class EntityBlock extends Entity{
	
	private static class IdData {
		public int id;
		public int data;
		
		public IdData(int id, int data) {
			super();
			this.id = id;
			this.data = data;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + data;
			result = prime * result + id;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			IdData other = (IdData) obj;
			if (data != other.data) return false;
			if (id != other.id) return false;
			return true;
		}
		
		
	}
	
	private static HashMap<IdData, DataRenderer> prerendered = new HashMap<>();
	
	public static void clearPrerendered(){
		for(DataRenderer d : prerendered.values()){
			d.destroy();
		}
		prerendered.clear();
	}
	
	private static OpenGL gl = Graphics.getOpenGL();
	public ItemStack item;
	private DataRenderer render;
	
	private static Vector3d BLOCK_SIZE = new Vector3d(0.5, 0.5, 0.5);

	
	
	public EntityBlock(World world, int uid, int data) {
		super(world);
		this.item = new ItemStack(uid, data);
		
	}
	
	@Override
	public void damage(int amount, DamageSource source) {
		
	}

	@Override
	public Vector3d getDefaultSize() {
		return BLOCK_SIZE;
	}
	
	@Override
	public TagCompound getSaveCompound() {
		TagCompound root = super.getSaveCompound();
		root.setShort("bid", (short) item.getId());
		root.setByte("data", item.getDamage());
		return root;
	}
	
	@Override
	public void loadSaveCompound(TagCompound tag) {
		super.loadSaveCompound(tag);
		item = new ItemStack();
		item.setId(tag.getShort("bid"));
		item.setDamage(tag.getByte("data"));
	}
	
	@Override
	public void draw() {
		if(render == null){
			IdData me = new IdData(item.getId(), item.getDamage());
			render = prerendered.get(me);
			if(render == null){
				render = new DataRenderer();
				Block.byId(item.getId()).getRenderer().renderBlock(item, -0.5f, -0.5f, -0.5f, render);
				render.compile();
				
				prerendered.put(me, render);
			}
		}
		
		byte ll = world.getBlockLight(position.toVector3l());
		byte la;
		Vector3l pos = position.toVector3l();
		
		for(int d = 0; d < 6; ++d){
			la = world.getBlockLight(pos.move(1, Direction.getDirection(d)));
			if(la > ll) ll = la;
		}
		Vector3f light = world.getGenerator().getLightColor(world, world.getMapData(pos.x >> 5, pos.z >> 5), pos.x, pos.y, pos.z, (int)(pos.x & 31), (int)(pos.z & 31), ll);
		if(world.hasSkyLight(pos.x, pos.y + 1, pos.z)){
			light = light.clone();
			float sky = world.getSkyLight();
			if(light.x < sky) light.x = sky;
			if(light.y < sky) light.y = sky;
			if(light.z < sky) light.z = sky;
		}
		
		gl.bindShader("texturelight");
		gl.shaderUniform3f("light", light);
		Resources.getAnimatedTexture("blocks").bind();
		gl.pushMatrix();
		gl.translate(position.x, position.y, position.z);
		render.render();
		gl.popMatrix();
		gl.unbindShader();
	}
	
	public boolean sets(){
		return true;
	}
	
	
	@Override
	public void update() {
		motionX *= 0.6;
		motionZ *= 0.6;
		motionY -= world.getFallAcceleration();
		if (motionY < -world.getFallMaxSpeed()) motionY = -world.getFallMaxSpeed();
		
		if(onGround && sets()){
			Vector3l pos = position.toVector3l();
			if(world.getBlockId(pos) == 0){
				world.setBlockIdData(pos, item.getId(), item.getDamage());
			} else {
				EntityItem drop = new EntityItem(getWorld());
				drop.setPosition(position);
				drop.item = item;
				drop.addToWorld();
			}
			kill();
			return;
		}
		
		if(ticksAlive == 1){
			Vector3l check = position.toVector3l().add(0, 1, 0);
			if(world.getBlockType(check) instanceof IBlockFallable){
				world.getBlockType(check).nearbyBlockChanged(world, check.x, check.y, check.z, Direction.DOWN);
			}
		}
		
		super.update();
	}
	
	@Override
	public String getName() {
		return "block";
	}

	public EntityBlock(World world) {
		super(world);
		this.item = new ItemStack(1, 0);
	}

}
