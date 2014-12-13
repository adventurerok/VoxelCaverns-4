/**
 * 
 */
package vc4.api.entity;

import java.util.Random;

import vc4.api.block.Block;
import vc4.api.block.CraftingTable;
import vc4.api.block.render.BlockRendererDefault;
import vc4.api.client.Client;
import vc4.api.container.Container;
import vc4.api.container.ContainerInventory;
import vc4.api.entity.trait.TraitCrafting;
import vc4.api.entity.trait.TraitOpenContainers;
import vc4.api.graphics.*;
import vc4.api.input.*;
import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.api.math.MathUtils;
import vc4.api.render.CracksRenderer;
import vc4.api.tool.*;
import vc4.api.util.AABB;
import vc4.api.util.RayTraceResult;
import vc4.api.vbt.TagCompound;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;
import vc4.api.world.*;

/**
 * @author paul
 * 
 */
public class EntityPlayer extends EntityLiving implements IEntityPickUpItems {

	RayTraceResult rays;
	double coolDown;
	ContainerInventory inventory = new ContainerInventory();
	Random rand = new Random();
	double minedAmount = 0;

	Vector3d spawn;
	Vector3l oRP;

	int maxHealing = 100;

	double nowHealing = 0;
	double healMinus = 0;
	private boolean paused;
	private int maxHealth = 100;
	private String name = "player";
	private String area = "{l:area.wilderness}";
	private int ticksSinceUpdate;

	private long entityAttackTick = 0;

	public Chunk playerChunk;

	public EntityPlayer(World world) {
		super(world);
		healing = 100;
		int pick = 0;
		double pickPower = 0;
		int spade = 0;
		double spadePower = 0;
		int axe = 0;
		double axePower = 0;
		for (int d = 2048; d < 4096; ++d) {
			if (Item.byId(d) == null) continue;
			Item itm = Item.byId(d);
			Tool tool = itm.getTool(new ItemStack(d));
			if (tool == null) continue;
			if (tool.getType() == null || tool.getPower() < 1) continue;
			if (tool.getType().equals(ToolType.pickaxe)) {
				if (tool.getPower() > pickPower) {
					pick = d;
					pickPower = tool.getPower();
				}
			} else if (tool.getType().equals(ToolType.axe)) {
				if (tool.getPower() > axePower) {
					axe = d;
					axePower = tool.getPower();
				}
			} else if (tool.getType().equals(ToolType.spade)) {
				if (tool.getPower() > spadePower) {
					spade = d;
					spadePower = tool.getPower();
				}
			}

		}
		inventory.setItem(0, new ItemStack(pick, 0, 1));
		// inventory.setItem(1, new ItemStack(world.getRegisteredBlock("vanilla.vine"), 0, 99));
		inventory.setItem(2, new ItemStack(spade, 0, 1));
		inventory.setItem(3, new ItemStack(axe, 0, 1));

		addTrait(new TraitOpenContainers(this));
		addTrait(new TraitCrafting(this));
	}

	public CraftingTable getCraftingTable() {
		return ((TraitCrafting) getTrait("crafting")).getCraftingTable();
	}

	public String getPlayerName() {
		return name;
	}

	public void decreaseCooldown(double delta) {
		coolDown -= delta;
		if (coolDown < 0) coolDown = 0;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	@Override
	public int getId() {
		return 5;
	}

	public boolean isPaused() {
		return paused;
	}

	public String getArea() {
		return area;
	}

	// NESW
	public int getSimpleFacing() {
		return (int) ((MathUtils.floor(((moveYaw * 4F) / 360F) + 0.5D) + 1) & 3);
	}

	@Override
	public void draw() {
		if (Client.getPlayer() == this) return;
		renderHumanModel("man", name);
	}

	public void setTicksSinceUpdate(int ticksSinceUpdate) {
		this.ticksSinceUpdate = ticksSinceUpdate;
	}

	@Override
	public int getMaxHealth() {
		return maxHealth;
	}

	// NESW NE, SE, SW, NW
	public int getAdvancedFacing() {
		int eight = (int) ((MathUtils.floor(((moveYaw * 8F) / 360F) + 0.5D) + 1) & 7);
		if (eight % 2 == 0) return eight / 2;
		else return ((eight - 1) / 2) + 6;

	}

	public void message(String text) {
		Client.getGame().printChatLine(text);
	}

	public void message(String sender, String text) {
		message("<" + sender + "{c:f}>: " + text);
	}

	@Override
	public TagCompound getSaveCompound() {
		TagCompound tag = super.getSaveCompound();
		tag.setInt("heMax", maxHealing);
		tag.setInt("hpMax", maxHealth);
		tag.setDouble("heNow", nowHealing);
		tag.setDouble("heMinus", healMinus);
		TagCompound inv = new TagCompound("inv");
		inventory.writeContainer(world, inv);
		tag.addTag(inv);
		tag.addTag(TagCompound.createVector3dTag("spawn", this.spawn));
		if (this.oRP != null) {
			tag.addTag(TagCompound.createVector3lTag("minePos", this.oRP));
			tag.setDouble("mineAmt", minedAmount);
		}
		return tag;
	}

	@Override
	public void loadSaveCompound(TagCompound tag) {
		super.loadSaveCompound(tag);
		maxHealing = tag.getInt("heMax");
		maxHealth = tag.getInt("hpMax");
		nowHealing = tag.getDouble("heNow");
		healMinus = tag.getDouble("heMinus");
		TagCompound inv = tag.getCompoundTag("inv");
		inventory = (ContainerInventory) Container.readContainer(world, inv);
		spawn = tag.getCompoundTag("spawn").readVector3d();
		if (tag.hasKey("minePos")) {
			oRP = tag.getCompoundTag("minePos").readVector3l();
			minedAmount = tag.getDouble("mineAmt");
		}
	}

	public void setMaxHealing(int maxHealing) {
		this.maxHealing = maxHealing;
	}

	public void drawCube() {
		if (rays == null || rays.isEntity) return;
		if (Client.getGame().guiVisible()) {
			OpenGL gl = Graphics.getOpenGL();
			AABB bounds = world.getBlockType(rays.x, rays.y, rays.z).getRayTraceSize(world, rays.x, rays.y, rays.z);
			if (bounds == null) return;
			gl.unbindShader();
			gl.disable(GLFlag.CULL_FACE);
			float s = 0.003f;
			float minX = (float) bounds.minX;
			float minY = (float) bounds.minY;
			float minZ = (float) bounds.minZ;
			float maxX = (float) bounds.maxX;
			float maxY = (float) bounds.maxY;
			float maxZ = (float) bounds.maxZ;
			gl.begin(GLPrimitive.LINE_LOOP);
			gl.color(0, 0, 0);
			gl.vertex(maxX + rays.x + s, minY + rays.y - s, minZ + rays.z - s);
			gl.vertex(maxX + rays.x + s, maxY + rays.y + s, minZ + rays.z - s);
			gl.vertex(maxX + rays.x + s, maxY + rays.y + s, maxZ + rays.z + s);
			gl.vertex(maxX + rays.x + s, minY + rays.y - s, maxZ + rays.z + s);
			gl.end();
			gl.begin(GLPrimitive.LINE_LOOP);
			gl.vertex(minX + rays.x - s, minY + rays.y - s, minZ + rays.z - s);
			gl.vertex(minX + rays.x - s, maxY + rays.y + s, minZ + rays.z - s);
			gl.vertex(minX + rays.x - s, maxY + rays.y + s, maxZ + rays.z + s);
			gl.vertex(minX + rays.x - s, minY + rays.y - s, maxZ + rays.z + s);
			gl.end();
			gl.begin(GLPrimitive.LINE_LOOP);
			gl.vertex(minX + rays.x - s, minY + rays.y - s, minZ + rays.z - s);
			gl.vertex(maxX + rays.x + s, minY + rays.y - s, minZ + rays.z - s);
			gl.vertex(maxX + rays.x + s, minY + rays.y - s, maxZ + rays.z + s);
			gl.vertex(minX + rays.x - s, minY + rays.y - s, maxZ + rays.z + s);
			gl.end();
			gl.begin(GLPrimitive.LINE_LOOP);
			gl.vertex(minX + rays.x - s, maxY + rays.y + s, minZ + rays.z - s);
			gl.vertex(maxX + rays.x + s, maxY + rays.y + s, minZ + rays.z - s);
			gl.vertex(maxX + rays.x + s, maxY + rays.y + s, maxZ + rays.z + s);
			gl.vertex(minX + rays.x - s, maxY + rays.y + s, maxZ + rays.z + s);
			gl.end();
			gl.begin(GLPrimitive.LINE_LOOP);
			gl.vertex(minX + rays.x - s, minY + rays.y - s, minZ + rays.z - s);
			gl.vertex(maxX + rays.x + s, minY + rays.y - s, minZ + rays.z - s);
			gl.vertex(maxX + rays.x + s, maxY + rays.y + s, minZ + rays.z - s);
			gl.vertex(minX + rays.x - s, maxY + rays.y + s, minZ + rays.z - s);
			gl.end();
			gl.begin(GLPrimitive.LINE_LOOP);
			gl.vertex(minX + rays.x - s, minY + rays.y - s, maxZ + rays.z + s);
			gl.vertex(maxX + rays.x + s, minY + rays.y - s, maxZ + rays.z + s);
			gl.vertex(maxX + rays.x + s, maxY + rays.y + s, maxZ + rays.z + s);
			gl.vertex(minX + rays.x - s, maxY + rays.y + s, maxZ + rays.z + s);
			gl.end();
		}
		if (minedAmount > 0 && oRP != null && world.getBlockType(oRP.x, oRP.y, oRP.z).getRenderer() instanceof BlockRendererDefault) {
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
		return new Vector3d(0.25, 0.83, 0.25);
	}

	/**
	 * @return the inventory
	 */
	public ContainerInventory getInventory() {
		return inventory;
	}

	@Override
	public int getMaxHealing() {
		return maxHealing;
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

	@Override
	public void kill() {
		health = (int) Math.max(100, getMaxHealth() * 0.65);
		dropItems();
		respawn();
	}

	public void dropItems() {
		for (ItemStack i : inventory) {
			dropItem(i);
		}
		inventory.clear();
	}

	public void dropHeldItem() {
		dropItem(inventory.getHeldItemStack());
		inventory.setHeldItemStack(null);
		;
	}

	public double getMinedAmount() {
		return minedAmount;
	}

	public void setArea(String area) {
		this.area = area;
	}

	// delta in ms
	public void leftMouseDown(double delta) {
		if (rays == null) { return; }
		if (rays.isEntity) leftMouseDownEntity(delta);
		else leftMouseDownBlock(delta);

	}

	private void leftMouseDownEntity(double delta) {
		if (ticksAlive - entityAttackTick > 3) {
			int damage = 0;
			ItemStack held = inventory.getSelectedStack();
			if (held == null) damage = 3;
			else damage = held.getAttackDamage();
			rays.entity.damage(damage, DamageSource.melee(this, held));
		}
		entityAttackTick = ticksAlive;
	}

	private void leftMouseDownBlock(double delta) {
		if (oRP != null && (oRP.x != rays.x || oRP.y != rays.y || oRP.z != rays.z)) {
			minedAmount = 0;
			oRP = new Vector3l(rays.x, rays.y, rays.z);
		} else if (oRP == null) {
			oRP = new Vector3l(rays.x, rays.y, rays.z);
		}

		ItemStack held = inventory.getSelectedStack();
		if (held != null && held.overrideLeftClick()) {
			held.onLeftClick(this);
			return;
		}
		if (world.getBlockType(rays.x, rays.y, rays.z).overrideLeftClick(world, rays.x, rays.y, rays.z)) {
			world.getBlockType(rays.x, rays.y, rays.z).onLeftClick(world, rays.x, rays.y, rays.z, rays.side, this, held);
			return;
		}
		if (coolDown < 0.1d) {
			if (minedAmount >= 1) {
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
		RayTraceResult blocks = world.rayTraceBlocks(getEyePos(), end, 200);
		double bDist = blocks != null ? blocks.vector.distanceSquared(position) : 1000000;
		RayTraceResult entitys = world.rayTraceEntitys(this, end, 200);
		double eDist = entitys != null ? entitys.vector.distanceSquared(position) : 1000000;
		rays = bDist < eDist ? blocks : entitys;
	}

	public void respawn() {
		teleport(spawn.clone());
		loadNearbyChunks();
	}

	public void loadNearbyChunks() {
		ChunkPos pos = ChunkPos.createFromWorldVector(position);
		if (!world.chunkExists(pos)) {
			world.loadChunk(pos);
		}
		ChunkPos lower = pos.add(0, -1, 0);
		if (!world.chunkExists(lower)) {
			world.loadChunk(lower);
		}
	}

	public void rightMouseDown(double delta) {
		if (rays == null) return;
		if (rays.isEntity) {
			rays.entity.onRightClick(this);
		} else {
			ItemStack held = inventory.getSelectedStack();
			if (world.getBlockType(rays.x, rays.y, rays.z).overrideRightClick(world, rays.x, rays.y, rays.z)) {
				world.getBlockType(rays.x, rays.y, rays.z).onRightClick(world, rays.x, rays.y, rays.z, rays.side, this, held);
			} else if (held != null) {
				held.onRightClick(this);
				if (!held.exists()) inventory.setSelectedStack(null);
			}
		}
	}

	/**
	 * @param coolDown
	 *            the coolDown to set
	 */
	public void setCoolDown(double coolDown) {
		this.coolDown = coolDown;
	}

	/**
	 * @param inventory
	 *            the inventory to set
	 */
	public void setInventory(ContainerInventory inventory) {
		this.inventory = inventory;
	}

	/**
	 * @param rays
	 *            the rays to set
	 */
	public void setRays(RayTraceResult rays) {
		this.rays = rays;
	}

	public void setSpawn(Vector3d spawn) {
		this.spawn = spawn;
	}

	public void addTickSinceUpdate() {
		ticksSinceUpdate++;
	}

	public int getTicksSinceUpdate() {
		return ticksSinceUpdate;
	}

	@Override
	public void update() {
		this.playerChunk = chunk;
		ticksSinceUpdate = 0;
		super.update();
		if (healing == 0) return;
		double heal = healing / 5500d;
		if (heal > 0 && health >= getMaxHealth()) heal = 0;
		if (heal > 0) healMinus += 1;
		else if (heal < 0) healMinus -= 1;
		nowHealing += heal;
		if (nowHealing > 0) {
			while (nowHealing >= 1) {
				health += 1;
				nowHealing -= 1;
			}
			while (healMinus >= 375) {
				healing -= 1;
				healMinus -= 370;
			}
		} else if (nowHealing < 0) {
			while (nowHealing <= -1) {
				health -= 1;
				nowHealing += 1;
			}
			while (healMinus <= -375) {
				healing += 1;
				healMinus += 370;
			}
		}
	}

	public void updateInput() {
		Keyboard keys = Input.getClientKeyboard();
		if (keys.keyPressed(Key.E)) inventory.toggleOpen();
		for (int d = 1; d < 10; ++d) {
			Key k = Key.valueOf("NUM_" + d);
			if (keys.keyPressed(k)) {
				inventory.setSelectedIndex(d - 1);
			}
		}
		if (keys.keyPressed(Key.Q)) {
			ItemStack itm = inventory.getSelectedStack();
			if (itm != null && itm.exists()) {
				if (keys.keyPressed(Key.CONTROL)) {
					throwItem(itm);
					inventory.setSelectedStack(null);
				} else {
					itm.decrementAmount();
					throwItem(itm.clone().setAmount(1));
					if (!itm.exists()) inventory.setSelectedStack(null);
				}
			}
		}
		if (keys.keyPressed(Key.NUM_0)) inventory.setSelectedIndex(9);
		if (!keys.isKeyDown(Key.M)) {
			if (keys.keyPressed(Key.MINUS)) inventory.setSelectedIndex(10);
			if (keys.keyPressed(Key.EQUALS)) inventory.sort();
		}
		if (keys.keyPressed(Key.LEFT)) {
			int i = inventory.getSelectedIndex() - 1;
			if (i < 0) i = 10;
			inventory.setSelectedIndex(i);
		} else if (keys.keyPressed(Key.RIGHT)) {
			int i = inventory.getSelectedIndex() + 1;
			if (i > 10) i = 0;
			inventory.setSelectedIndex(i);
		}
		if (keys.keyPressed(Key.UP)) {
			if (keys.isKeyDown(Key.CONTROL)) inventory.shiftItemsUp();
			else inventory.shiftSelectedUp();
		} else if (keys.keyPressed(Key.DOWN)) {
			if (keys.isKeyDown(Key.CONTROL)) inventory.shiftItemsDown();
			else inventory.shiftSelectedDown();
		}
	}

	@Override
	public int reduceDamage(DamageSource source, int damage) {
		// if(source == DamageSource.fallDamage) return 0;
		return super.reduceDamage(source, damage);
	}

	@Override
	public String getName() {
		return "player";
	}

	@Override
	public boolean persistent() {
		return false; // Why would you "freeze" players in save files?
	}
}
