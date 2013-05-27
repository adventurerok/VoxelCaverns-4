package vc4.vanilla.tileentity;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import vc4.api.container.Container;
import vc4.api.container.ContainerChest;
import vc4.api.entity.EntityItem;
import vc4.api.io.BitInputStream;
import vc4.api.io.BitOutputStream;
import vc4.api.math.MathUtils;
import vc4.api.tileentity.TileEntityContainer;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.vanilla.Vanilla;
import vc4.vanilla.block.BlockPlanks;

public class TileEntityChest extends TileEntityContainer{

	private static Color gold = new Color(255, 226, 102);
	private static Color adamantite = new Color(60, 93, 60);
	
	public byte type, subtype;
	
	public ContainerChest chest = null;

	public TileEntityChest(World world, Vector3l pos) {
		super(world, pos);
	}
	public int getCorrectSlots() {
		switch(type){
		case 0:
			return 44;
		case 1:
			return 88;
		case 2:
			return 154;
		}
		return 44;
	}
	public TileEntityChest(World world, Vector3l pos, byte type, byte subtype) {
		super(world, pos);
		this.type = type;
		this.subtype = subtype;
		chest = new ContainerChest(getCorrectSlots());
	}
	
	public Color getColor(){
		switch(type){
		case 0:
			return BlockPlanks.backColors[subtype];
		case 1:
			return gold;
		case 2:
			return adamantite;
		}
		
		return Color.white;
	}
	
	@Override
	public void updateTick() {
//		for(int d = 0; d < 6; ++d){
//			int id = world.getNearbyBlockId(position.x, position.y, position.z, d);
//			if(id != Block.lkradTransportPipe.uid) continue;
//			ItemStack toDrop = chest.removeItemsFromStack(1);
//			if(toDrop == null || !toDrop.checkIsNotEmpty()) break;
//			Vector3d pos = position.relative(d).toVector3d();
//			if(d == 0) pos.add(new Vector3d(0.05, 0.5, 0.5));
//			if(d == 1) pos.add(new Vector3d(0.5, 0.5, 0.05));
//			if(d == 2) pos.add(new Vector3d(0.95, 0.5, 0.5));
//			if(d == 3) pos.add(new Vector3d(0.5, 0.5, 0.95));
//			if(d == 4) pos.add(new Vector3d(0.5, 0.05, 0.5));
//			if(d == 5) pos.add(new Vector3d(0.5, 0.95, 0.5));
//			EntityItem item = new EntityItem(world, pos, toDrop);
//			item.isInPipe = true;
//			item.pipeDirection = d;
//			item.oldPos = pos.clone();
//		}
		if(world.getBlockId(position.x, position.y, position.z) != Vanilla.chest.uid){
			remove = true;
		}
	}

	public void dropContainer() {
		Random rand = new Random();
		for(int dofor = 0; dofor < chest.getSize(); ++dofor){
			if(chest.getItem(dofor) == null || !chest.getItem(dofor).checkIsNotEmpty()) continue;
			EntityItem e = new EntityItem(world);
			e.setPosition(position.x + 0.5F, position.y + 0.5F, position.z + 0.5F);
			e.setItem(chest.getItem(dofor));
			float motion = (float) (Math.PI * 2 * (rand.nextFloat() * 2 - 1F));
			float multi = rand.nextFloat();
			e.motionX = MathUtils.sin(motion) * multi;
			e.motionZ = MathUtils.cos(motion) * multi;
			e.addToWorld();
			chest.setItem(dofor, null);
		}
		setUnsavedChanges();
		
	}
	@Override
	public short getId() {
		return 1;
	}
	@Override
	public void writeAdditionalData(BitOutputStream out) throws IOException {
//		out.writeByte((byte) 0);
//		out.writeByte(type);
//		out.writeByte(subtype);
//		chest.writeTo(out);
	}
	@Override
	public void readAdditionalData(BitInputStream in) throws IOException {
//		in.readByte();
//		type = in.readByte();
//		subtype = in.readByte();
//		chest = (ContainerChest) Container.readFrom(in);
	}
	@Override
	public Container getContainer() {
		return chest;
	}


	
}
