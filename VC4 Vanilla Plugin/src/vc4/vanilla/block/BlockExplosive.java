package vc4.vanilla.block;

import java.util.ArrayList;
import java.util.Collection;

import vc4.api.block.Block;
import vc4.api.entity.Entity;
import vc4.api.entity.EntityPlayer;
import vc4.api.item.ItemStack;
import vc4.api.util.Direction;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.entity.EntityBlockExplosive;

public class BlockExplosive extends Block {

	float power[] = new float[32];
	float chance[] = new float[32];
	
	int[] texture = new int[32];
	String[] names = new String[32];
	
	public BlockExplosive(int id){
		super(id, 0, "explosive");
		defineExplosive(0, BlockTexture.tnt, 5, false, true, 0.3f, "tnt");
		defineExplosive(1, BlockTexture.rdx, 10, false, true, 0.2f, "rdx");
		
	}
	
	
	@Override
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		return texture[world.getBlockData(x, y, z)];
	}
	
	@Override
	public int getTextureIndex(ItemStack item, int side) {
		return texture[item.getDamage()];
	}
	
	@Override
	protected String getModifiedName(ItemStack item) {
		return names[item.getDamage()];
	}
	
	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> res = new ArrayList<>();
		res.add(new ItemStack(uid, 0, 1));
		res.add(new ItemStack(uid, 1, 1));
		return res;
	}
	
	@Override
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {
		if(getDirectSignal(world, x, y, z) > 0) explode(world, x, y, z);
	}
	
	@Override
	public boolean takesSignalInput(World world, long x, long y, long z, int side) {
		return true;
	}
	
	
	@Override
	public void onRightClick(World world, long x, long y, long z, int side, EntityPlayer player, ItemStack item) {
		if(player.getCoolDown() > 0.01) return;
		explode(world, x, y, z);
		player.setCoolDown(150);
	}
	
	@Override
	public boolean overrideRightClick(World world, long x, long y, long z) {
		return true;
	}
	
	public void explode(World world, long x, long y, long z){
		byte data = world.getBlockData(x, y, z);
		EntityBlockExplosive bang = new EntityBlockExplosive(world, uid, data);
		bang.dropChance = chance[data];
		bang.power = power[data];
		bang.setPosition(x + 0.5, y + 0.5, z + 0.5);
		bang.motionX = bang.rand.nextFloat() - 0.5;
		bang.motionZ = bang.rand.nextFloat() - 0.5;
		bang.motionY = 0.25;
		bang.addToWorld();
		world.setBlockId(x, y, z, 0);
	}
	
	@Override
	public void onBlockExploded(World world, long x, long y, long z, Entity exploder) {
		explode(world, x, y, z);
	}
	
	private void defineExplosive(int data, int tex, float power, boolean fire, boolean gravity, float chance, String name){
		this.power[data] = power;
		//this.fire[data] = fire;
		//this.gravity[data] = gravity;
		this.texture[data] = tex;
		this.names[data] = name;
		this.chance[data] = chance;
	}
	
}
