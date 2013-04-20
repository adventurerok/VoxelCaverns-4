/**
 * 
 */
package vc4.api;

import java.util.*;

import vc4.api.block.Block;
import vc4.api.container.ContainerInventory;
import vc4.api.entity.EntityItem;
import vc4.api.graphics.*;
import vc4.api.input.*;
import vc4.api.item.ItemStack;
import vc4.api.util.*;
import vc4.api.vector.Vector3d;
import vc4.api.world.World;

/**
 * @author paul
 *
 */
public class BlockInteractor {

	
	World world;
	RayTraceResult rays;
	double coolDown;
	ContainerInventory inventory = new ContainerInventory();
	
	
	/**
	 * @return the coolDown
	 */
	public double getCoolDown() {
		return coolDown;
	}

	/**
	 * @param coolDown the coolDown to set
	 */
	public void setCoolDown(double coolDown) {
		this.coolDown = coolDown;
	}

	/**
	 * @return the rays
	 */
	public RayTraceResult getRays() {
		return rays;
	}

	/**
	 * @param rays the rays to set
	 */
	public void setRays(RayTraceResult rays) {
		this.rays = rays;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	public BlockInteractor(World world) {
		super();
		this.world = world;
		Random rand = new Random();
		ArrayList<ItemStack> creativeItems = new ArrayList<>();
		for(int d = 1; d < 2048; ++d){
			if(Block.byId(d) == null) continue;
			ItemStack[] b = Block.byId(d).getCreativeItems();
			if(b == null) continue;
			creativeItems.addAll(Arrays.asList(b));
		}
		for(int d = 0; d < 44; ++d){
			inventory.setItem(d, creativeItems.get(rand.nextInt(creativeItems.size())).clone().setAmount(1 + rand.nextInt(20)));
		}
	}

	//delta in ms
	public void leftMouseDown(double delta){
		if(rays == null || rays.isEntity) return;
		if(coolDown < 0.1d){
			int id = world.getBlockId(rays.x, rays.y, rays.z);
			world.setBlockId(rays.x, rays.y, rays.z, 0);
			setCoolDown(200);
			new EntityItem(getWorld()).setItem(new ItemStack(id)).setPosition(rays.x + 0.5, rays.y + 0.5, rays.z + 0.5).addToWorld();
		}
	}
	
	public void rightMouseDown(double delta){
		if(rays == null || rays.isEntity) return;
		if(coolDown < 0.1d){
			ItemStack held = inventory.getSelectedStack();
			if(held == null || !held.checkIsNotEmpty()) return;
			world.setNearbyBlockIdData(rays.x, rays.y, rays.z, held.getId(), held.getData(), Direction.getDirection(rays.side));
			setCoolDown(200);
		}
	}
	
	public void decreaseCooldown(double delta){
		coolDown -= delta;
		if(coolDown < 0) coolDown = 0;
	}

	/**
	 * @param position
	 * @param look
	 * @param i
	 */
	public void rayTrace(Vector3d position, Vector3d look, double dist) {
		Vector3d end = position.add(look.x * dist, look.y * dist, look.z * dist);
		rays = world.rayTraceBlocks(position.clone(), end, 200);
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
		
	}
	
	/**
	 * @return the inventory
	 */
	public ContainerInventory getInventory() {
		return inventory;
	}
	
	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(ContainerInventory inventory) {
		this.inventory = inventory;
	}
	
	public void update(){
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
}
