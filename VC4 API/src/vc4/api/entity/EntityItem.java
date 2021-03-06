package vc4.api.entity;

import java.util.List;

import vc4.api.Resources;
import vc4.api.block.Block;
import vc4.api.graphics.*;
import vc4.api.item.ItemStack;
import vc4.api.math.MathUtils;
import vc4.api.render.DataRenderer;
import vc4.api.render.ItemRenderer;
import vc4.api.vbt.TagCompound;
import vc4.api.vector.Vector3d;
import vc4.api.world.World;

public class EntityItem extends Entity {

	private static OpenGL gl = Graphics.getOpenGL();
	public ItemStack item;
	private Renderer render;
	private boolean compiled;
	private int waitTime = 10;

	public EntityItem(World world) {
		super(world);

	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public Vector3d getDefaultSize() {
		return new Vector3d(0.2f, 0.2f, 0.2f);
	}

	public EntityItem setItem(ItemStack item) {
		this.item = item;
		return this;
	}

	@Override
	public void update() {
		if (item == null || !item.exists()) {
			isDead = true;
			return;
		}
		if (waitTime > 0) --waitTime;
		else {
			List<Entity> entities = world.getEntitiesInBoundsExcluding(this.chunk, bounds.expand(0.6, 0.75, 0.6), this);
			for (int d = 0; d < entities.size(); ++d) {
				Entity e = entities.get(d);
				if (e == null) continue;
				if (e instanceof IEntityPickUpItems) {
					item = ((IEntityPickUpItems) e).pickUpItem(item);
				} else if (e instanceof EntityItem) {
					EntityItem itm = (EntityItem) e;
					if (itm.item != null && itm.item.equals(item)) {
						item = itm.item.combineItemStack(item);
					}
				}
				if (item == null || !item.exists()) {
					isDead = true;
					return;
				}
			}
			entities = null;
		}
		motionX *= 0.6;
		motionZ *= 0.6;
		motionY -= world.getFallAcceleration();
		if (motionY < -world.getFallMaxSpeed()) motionY = -world.getFallMaxSpeed();
		super.update();
	}

	@Override
	public void draw() {
		if (item == null || !item.exists()) return;
		if (item.isBlock() && Block.byId(item.getId()).render3d(item.getData())) {
			if (!compiled) {
				render = new DataRenderer();
				Block.byId(item.getId()).getRenderer().renderBlock(item, -0.5f, -0.5f, -0.5f, render);
				render.compile();
				compiled = true;
			}
			gl.pushMatrix();
			gl.translate(position.x, position.y + 0.1 + (MathUtils.sin(world.getTime() / 5f) * 0.1), position.z);
			gl.scale(0.4f, 0.4f, 0.4f);
			int amount = 1;
			if (item.getAmount() > 5) amount = 2;
			if (item.getAmount() > 15) amount = 3;
			if (item.getAmount() > 60) amount = 4;
			if (item.getAmount() > 98) amount = 5;
			Graphics.getClientShaderManager().bindShader("texture");
			Resources.getAnimatedTexture("blocks").bind();
			for (int d = 0; d < amount; ++d) {
				gl.rotate((world.getTime() * 4) % 360, 0, 1, 0);
				render.render();
			}
			gl.popMatrix();

		} else {
			gl.bindShader("texture");
			gl.pushMatrix();
			gl.translate(position.x, position.y + 0.1 + (MathUtils.sin(world.getTime() / 5f) * 0.1), position.z);
			gl.scale(0.4f, 0.4f, 0.4f);
			int amount = 1;
			if (item.getAmount() > 5) amount = 2;
			if (item.getAmount() > 15) amount = 3;
			if (item.getAmount() > 60) amount = 4;
			if (item.getAmount() > 98) amount = 5;
			if(!compiled){
				render = new DataRenderer();
				ItemRenderer.renderItem3D(item, 0, 0, 0, render);
				render.compile();
				compiled = true;
			}
			if (item.isBlock()) Resources.getAnimatedTexture("blocks").bind();
			else Resources.getAnimatedTexture("items").bind();
			for (int d = 0; d < amount; ++d) {
				gl.rotate((world.getTime() * 4) % 360, 0, 1, 0);
				render.render();
			}
			gl.popMatrix();
		}
	}

	@Override
	public boolean canCollide(Entity test) {
		return false;
	}

	@Override
	public String getName() {
		return "item";
	}

	@Override
	public TagCompound getSaveCompound() {
		TagCompound tag = super.getSaveCompound();
		TagCompound itm = new TagCompound("item");
		ItemStack.write(world, item, itm);
		tag.addTag(itm);
		return tag;
	}

	@Override
	public void loadSaveCompound(TagCompound tag) {
		super.loadSaveCompound(tag);
		item = ItemStack.read(world, tag.getCompoundTag("item"));
	}

}
