/**
 * 
 */
package vc4.api.entity;

import java.util.*;

import vc4.api.block.Block;
import vc4.api.block.render.BlockRendererDefault;
import vc4.api.container.ContainerInventory;
import vc4.api.graphics.*;
import vc4.api.input.*;
import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.api.math.MathUtils;
import vc4.api.render.CracksRenderer;
import vc4.api.tool.MiningData;
import vc4.api.util.AABB;
import vc4.api.util.RayTraceResult;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;
import vc4.api.world.ChunkPos;
import vc4.api.world.World;

/**
 * @author paul
 *
 */
public class EntityPlayer extends EntityLiving implements IEntityPickUpItems{

	
	RayTraceResult rays;
	double coolDown;
	ContainerInventory inventory = new ContainerInventory();
	Random rand = new Random();
	double minedAmount = 0;
	
	Vector3d spawn;
	Vector3l oRP;
	
	public EntityPlayer(World world) {
		super(world);
		Random rand = new Random();
		ArrayList<ItemStack> creativeItems = new ArrayList<>();
		for(int d = 0; d < 2048; ++d){
			if(Item.byId(d) == null) continue;
			ItemStack[] b = Item.byId(d).getCreativeItems();
			if(b == null) continue;
			creativeItems.addAll(Arrays.asList(b));
		}
		for(int d = 0; d < 40; ++d){
			inventory.setItem(d, creativeItems.get(rand.nextInt(creativeItems.size())).clone().setAmount(50 + rand.nextInt(40)));
		}
	}
	
	public void decreaseCooldown(double delta){
		coolDown -= delta;
		if(coolDown < 0) coolDown = 0;
	}
	
	//NESW
	public int getSimpleFacing(){
		return (int) ((MathUtils.floor(((yaw * 4F) / 360F) + 0.5D) + 1) & 3);
	}
	
	//NESW NE, SE, SW, NW
	public int getAdvancedFacing(){
		int eight = (int) ((MathUtils.floor(((yaw * 8F) / 360F) + 0.5D) + 1) & 7);
		if(eight % 2 == 0) return eight / 2;
		else return ((eight - 1) / 2) + 6;
				
	}
	
	
	public void drawCube(){
		if(rays == null || rays.isEntity) return;
		OpenGL gl = Graphics.getClientOpenGL();
		AABB bounds = world.getBlockType(rays.x, rays.y, rays.z).getRayTraceSize(world, rays.x, rays.y, rays.z);
		Graphics.getClientShaderManager().unbindShader();
		gl.disable(GLFlag.CULL_FACE);
		float s = 0.003f;
		float minX = (float) bounds.minX;
		float minY = (float) bounds.minY;
		float minZ = (float) bounds.minZ;
		float maxX = (float) bounds.maxX;
		float maxY = (float) bounds.maxY;
		float maxZ = (float) bounds.maxZ;
		gl.begin(GLPrimative.LINE_LOOP);
		gl.color(0, 0, 0);
		gl.vertex(maxX + rays.x + s, minY + rays.y - s, minZ + rays.z - s);
		gl.vertex(maxX + rays.x + s, maxY + rays.y + s, minZ + rays.z - s);
		gl.vertex(maxX + rays.x + s, maxY + rays.y + s, maxZ + rays.z + s);
		gl.vertex(maxX + rays.x + s, minY + rays.y - s, maxZ + rays.z + s);
		gl.end();
		gl.begin(GLPrimative.LINE_LOOP);
		gl.vertex(minX + rays.x - s, minY + rays.y - s, minZ + rays.z - s);
		gl.vertex(minX + rays.x - s, maxY + rays.y + s, minZ + rays.z - s);
		gl.vertex(minX + rays.x - s, maxY + rays.y + s, maxZ + rays.z + s);
		gl.vertex(minX + rays.x - s, minY + rays.y - s, maxZ + rays.z + s);
		gl.end();
		gl.begin(GLPrimative.LINE_LOOP);
		gl.vertex(minX + rays.x - s, minY + rays.y - s, minZ + rays.z - s);
		gl.vertex(maxX + rays.x + s, minY + rays.y - s, minZ + rays.z - s);
		gl.vertex(maxX + rays.x + s, minY + rays.y - s, maxZ + rays.z + s);
		gl.vertex(minX + rays.x - s, minY + rays.y - s, maxZ + rays.z + s);
		gl.end();
		gl.begin(GLPrimative.LINE_LOOP);
		gl.vertex(minX + rays.x - s, maxY + rays.y + s, minZ + rays.z - s);
		gl.vertex(maxX + rays.x + s, maxY + rays.y + s, minZ + rays.z - s);
		gl.vertex(maxX + rays.x + s, maxY + rays.y + s, maxZ + rays.z + s);
		gl.vertex(minX + rays.x - s, maxY + rays.y + s, maxZ + rays.z + s);
		gl.end();
		gl.begin(GLPrimative.LINE_LOOP);
		gl.vertex(minX + rays.x - s, minY + rays.y - s, minZ + rays.z - s);
		gl.vertex(maxX + rays.x + s, minY + rays.y - s, minZ + rays.z - s);
		gl.vertex(maxX + rays.x + s, maxY + rays.y + s, minZ + rays.z - s);
		gl.vertex(minX + rays.x - s, maxY + rays.y + s, minZ + rays.z - s);
		gl.end();
		gl.begin(GLPrimative.LINE_LOOP);
		gl.vertex(minX + rays.x - s, minY + rays.y - s, maxZ + rays.z + s);
		gl.vertex(maxX + rays.x + s, minY + rays.y - s, maxZ + rays.z + s);
		gl.vertex(maxX + rays.x + s, maxY + rays.y + s, maxZ + rays.z + s);
		gl.vertex(minX + rays.x - s, maxY + rays.y + s, maxZ + rays.z + s);
		gl.end();
		if(minedAmount > 0 && oRP != null && world.getBlockType(oRP.x, oRP.y, oRP.z).getRenderer() instanceof BlockRendererDefault){
			AABB bnds = world.getBlockType(oRP.x, oRP.y, oRP.z).getRenderSize(world, oRP.x, oRP.y, oRP.z);
			CracksRenderer.renderCracks(world, oRP.x, oRP.y, oRP.z, bnds, minedAmount);
		}
	}

	/**
	 * @return the coolDown
	 */
	public double getCoolDown() {
		return coolDown;
	}

	@Override
	public Vector3d getDefaultSize() {
		return new Vector3d(0.25, 0.9, 0.25);
	}
	
	/**
	 * @return the inventory
	 */
	public ContainerInventory getInventory() {
		return inventory;
	}

	/**
	 * @return the rays
	 */
	public RayTraceResult getRays() {
		return rays;
	}

	public Vector3d getSpawn() {
		return spawn;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	@Override
	public void kill() {
		health = (int) Math.max(100, getMaxHealth() * 0.65);
		respawn();
	}
	
	public double getMinedAmount() {
		return minedAmount;
	}
	
	//delta in ms
	public void leftMouseDown(double delta){
		if(rays == null || rays.isEntity){
			return;
		}
		if(oRP != null && (oRP.x != rays.x || oRP.y != rays.y || oRP.z != rays.z)){
			minedAmount = 0;
			oRP = new Vector3l(rays.x, rays.y, rays.z);
		} else if(oRP == null){
			oRP = new Vector3l(rays.x, rays.y, rays.z);
		}
		
		ItemStack held = inventory.getSelectedStack();
		if(held != null && held.hasSpecialLeftClickEvent()){
			held.onLeftClick(this);
			return;
		}
		if(coolDown < 0.1d){
			if(minedAmount >= 1){
				world.getBlockType(rays.x, rays.y, rays.z).onBlockMined(world, rays.x, rays.y, rays.z, held);
				setCoolDown(200);
				minedAmount = 0;
			} else {
				Block b = world.getBlockType(rays.x, rays.y, rays.z);
				MiningData d = b.getMiningData(world, rays.x, rays.y, rays.z);
				minedAmount += d.getMiningDone(held != null ? held.getTool() : null, delta);
			}
		}
	}

	@Override
	public ItemStack pickUpItem(ItemStack in) {
		return inventory.addItemStack(in);
	}
	
	/**
	 * @param position
	 * @param look
	 * @param i
	 */
	public void rayTrace(Vector3d look, double dist) {
		Vector3d end = getEyePos().add(look.x * dist, look.y * dist, look.z * dist);
		rays = world.rayTraceBlocks(getEyePos(), end, 200);
	}
	
	public void respawn() {
		teleport(spawn.clone());
		ChunkPos pos = ChunkPos.createFromWorldVector(position);
		if(!world.chunkExists(pos)){
			world.generateChunk(pos);
		}
		ChunkPos lower = pos.add(0, -1, 0);
		if(!world.chunkExists(lower)){
			world.generateChunk(lower);
		}
		
	}

	public void rightMouseDown(double delta){
		ItemStack held = inventory.getSelectedStack();
		if(held != null) held.onRightClick(this);
	}
	
	/**
	 * @param coolDown the coolDown to set
	 */
	public void setCoolDown(double coolDown) {
		this.coolDown = coolDown;
	}
	
	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(ContainerInventory inventory) {
		this.inventory = inventory;
	}
	
	/**
	 * @param rays the rays to set
	 */
	public void setRays(RayTraceResult rays) {
		this.rays = rays;
	}
	
	public void setSpawn(Vector3d spawn) {
		this.spawn = spawn;
	}

	public void updateInput(){
		Keyboard keys = Input.getClientKeyboard();
		if(keys.keyPressed(Key.E)) inventory.toggleOpen();
		for(int d = 1; d < 10; ++d){
			Key k = Key.valueOf("NUM_" + d);
			if(keys.keyPressed(k)){
				inventory.setSelectedIndex(d - 1);
			}
		}
		if(keys.keyPressed(Key.NUM_0)) inventory.setSelectedIndex(9);
		if(keys.keyPressed(Key.MINUS)) inventory.setSelectedIndex(10);
		if(keys.keyPressed(Key.EQUALS)) inventory.sort();
		if(keys.keyPressed(Key.LEFT)){
			int i = inventory.getSelectedIndex() - 1;
			if (i < 0) i = 10;
			inventory.setSelectedIndex(i);
		} else if(keys.keyPressed(Key.RIGHT)){
			int i = inventory.getSelectedIndex() + 1;
			if (i > 10) i = 0;
			inventory.setSelectedIndex(i);
		}
		if(keys.keyPressed(Key.UP)){
			if(keys.isKeyDown(Key.CONTROL)) inventory.shiftItemsUp();
			else inventory.shiftSelectedUp();
		} else if(keys.keyPressed(Key.DOWN)){
			if(keys.isKeyDown(Key.CONTROL)) inventory.shiftItemsDown();
			else inventory.shiftSelectedDown();
		}
	}
	
	@Override
	public int reduceDamage(DamageSource source, int damage) {
		if(source == DamageSource.fallDamage) return 0;
		else return super.reduceDamage(source, damage);
	}
}
