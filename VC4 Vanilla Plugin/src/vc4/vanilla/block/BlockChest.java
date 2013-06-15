package vc4.vanilla.block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import vc4.api.block.BlockMultitexture;
import vc4.api.entity.EntityPlayer;
import vc4.api.entity.trait.TraitOpenContainers;
import vc4.api.item.ItemStack;
import vc4.api.tileentity.TileEntity;
import vc4.api.util.Direction;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.container.ContainerChest;
import vc4.vanilla.itementity.ItemEntityChest;
import vc4.vanilla.tileentity.TileEntityChest;

public class BlockChest extends BlockMultitexture{

	public BlockChest(int id) {
		super(id, 1, "chest");
		//hasGoodRightClick = true;
	}
	
	@Override
	public int getTextureIndexMultitexture(World world, long x, long y, long z, int side) {
		if(side > 3) return BlockTexture.chestTop;
		else if(world.getBlockData(x, y, z) == side) return BlockTexture.chestFront;
		else return BlockTexture.chestSide;
	}
	
	@Override
	public int getTextureIndexMultitexture(ItemStack item, int side) {
		if(side > 3) return BlockTexture.chestTop;
		else if(item.getDamage() == side) return BlockTexture.chestFront;
		else return BlockTexture.chestSide;
	}
	
//	@Override
//	public void onBlockPlaced(World world, long x, long y, long z, int side,
//			EntityPlayer player) {
//		world.setBlockData(x, y, z, (byte) BlockFace.getOpposite(player.getFacingDirection()));
//	}
	
	@Override
	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		ItemStack drop = new ItemStack(uid, 0, 1);
		TileEntity t = world.getTileEntity(x, y, z);
		if(t == null || !(t instanceof TileEntityChest)) return new ItemStack[]{drop};
		ItemEntityChest c = new ItemEntityChest();
		c.type = ((TileEntityChest)t).type;
		c.subtype = ((TileEntityChest)t).subtype;
		drop.entities.add(c);
		return new ItemStack[]{drop};
	}
	

	
	@Override
	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> is = new ArrayList<ItemStack>();
		for(int dofor = 0; dofor < 7; ++dofor){
			is.add(generateChestItem(0, dofor, 1));
		}
		is.add(generateChestItem(1, 0, 1));
		is.add(generateChestItem(2, 0, 1));
		return is;
	}

	
	public ItemStack generateChestItem(int type, int subtype, int amount){
		ItemStack s = new ItemStack(uid, 0, amount);
		s.entities.add(new ItemEntityChest((byte)type, (byte)subtype));
		return s;
	}
	
	@Override
	public Color getColor(ItemStack item, int side) {
		ItemEntityChest chest = null;
		for(int dofor = 0; dofor < item.entities.size(); ++dofor){
			if(item.entities.get(dofor) instanceof ItemEntityChest){
				chest = (ItemEntityChest) item.entities.get(dofor);
				break;
			}
		}
		if(chest == null) return Color.white;
		return chest.getColor();
	}
	
	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		TileEntity t = world.getTileEntity(x, y, z);
		if(t == null || !(t instanceof TileEntityChest)) return Color.white;
		return ((TileEntityChest)t).getColor();
	}
	
	@Override
	public String getModifiedName(ItemStack item) {
		ItemEntityChest chest = null;
		for(int dofor = 0; dofor < item.entities.size(); ++dofor){
			if(item.entities.get(dofor) instanceof ItemEntityChest){
				chest = (ItemEntityChest) item.entities.get(dofor);
				break;
			}
		}
		if(chest == null) return name;
		return name + "." + chest.getTypeName();
	}
	

	

	
	@Override
	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		world.setBlockIdData(x, y, z, uid, Direction.getOpposite(player.getSimpleFacing()).id());
		ItemEntityChest chest = null;
		for(int dofor = 0; dofor < item.entities.size(); ++dofor){
			if(item.entities.get(dofor) instanceof ItemEntityChest){
				chest = (ItemEntityChest) item.entities.get(dofor);
				break;
			}
		}
		TileEntityChest c = new TileEntityChest(world, new Vector3l(x, y, z), (byte)0, (byte)0);
		c.addToWorld();
		if(chest != null){
			c.type = chest.type;
			c.subtype = chest.subtype;
			c.chest = new ContainerChest(c.getCorrectSlots());
			c.setChunkRedraw();
		}
		item.decrementAmount();
	}
	
	@Override
	public boolean overrideRightClick(World world, long x, long y, long z) {
		return true;
	}
	
	@Override
	public void onRightClick(World world, long x, long y, long z, int side,
			EntityPlayer player, ItemStack item) {
		if(player.getCoolDown() > 0.1) return;
		player.setPaused(true);
		player.getInventory().openContainer();
		TileEntity c = world.getTileEntity(x, y, z);
		TileEntityChest chest;
		if(c instanceof TileEntityChest){
			chest = (TileEntityChest) c;
		} else {
			chest = new TileEntityChest(world, new Vector3l(x, y, z), (byte)0, (byte)0);
			chest.addToWorld();
		}
		((TraitOpenContainers)player.getTrait("opencontainers")).open(chest);
		player.setCoolDown(200);
	}


	

}
