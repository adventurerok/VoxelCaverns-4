/**
 * 
 */
package vc4.api.block;

import java.awt.Color;
import java.util.*;

import vc4.api.block.render.*;
import vc4.api.entity.*;
import vc4.api.graphics.TextureCoords;
import vc4.api.item.Item;
import vc4.api.item.ItemStack;
import vc4.api.text.Localization;
import vc4.api.tool.MiningData;
import vc4.api.util.*;
import vc4.api.vector.Vector3d;
import vc4.api.world.World;

/**
 * @author paul
 * 
 */
public class Block {

	public static final AABB square = AABB.getBoundingBox(0, 1, 0, 1, 0, 1);
	public static final BlockRenderer main = new BlockRendererDefault();

	public final short uid;
	public final Material material;
	public final int textureIndex;

	protected float timeToDestroy;
	protected float blastResistance;
	protected int renderType;
	protected String name;
	protected BlockRenderer renderer = main;
	protected boolean isCube = true;
	protected boolean isAir = false;

	protected MiningData mineData = new MiningData();

	private boolean solid = true;
	private boolean standable = true;
	private boolean standinable = false;

	protected static Random rand = new Random();

	protected static Block[] blocksList = new Block[2048];
	public static byte[] blockOpacity = new byte[2048];
	public static byte[] blockLight = new byte[2048];
	
	static{
		
		Arrays.fill(blockOpacity, (byte)15);
		blockOpacity[0] = 1;
	}

	public Block(short uid, int texture, Material m) {
		super();
		this.uid = uid;
		material = m;
		textureIndex = texture;
		timeToDestroy = (Float) m.getAttribute("destroyTime");
		blastResistance = (Float) m.getAttribute("blastResist");
		if (uid < 0) return;
		if (blocksList[uid] != null) throw new DuplicateBlockException("Block already exists at id: " + uid);
		blocksList[uid] = this;
		Item.addItemBlock(uid);
	}

	public Block setMineData(MiningData mineData) {
		this.mineData = mineData;
		return this;
	}
	
	public Block setLightOpacity(int opac){
		blockOpacity[uid] = (byte) opac;
		return this;
	}
	
	public Block setLightLevel(int lvl){
		blockLight[uid] = (byte) lvl;
		return this;
	}

	public void onEntityTickInside(World world, long x, long y, long z, Entity entity) {

	}
	
	public void onEntityStandOn(World world, long x, long y, long z, Entity entity){
		
	}

	public void onEntityCollideHorizontal(World world, long x, long y, long z, Entity entity) {

	}

	public Block(int uid, int texture, Material m) {
		this((short) uid, texture, m);
	}

	public Block(int uid, int texture, String material) {
		this((short) uid, texture, Material.getMaterial(material));
	}

	public Block setName(String name) {
		this.name = name;
		return this;
	}

	public boolean overrideLeftClick(World world, long x, long y, long z) {
		return false;
	}

	public boolean overrideRightClick(World world, long x, long y, long z) {
		return false;
	}

	public static Block byId(int id) {
		if (id == -1) return minus;
		return blocksList[id];
	}

	public boolean canBeReplaced(int id, byte data) {
		return isAir || renderer instanceof BlockRendererFluid;
	}

	/**
	 * @return the renderer
	 */
	public BlockRenderer getRenderer() {
		return renderer;
	}

	public boolean isSolid(World world, long x, long y, long z, int side) {
		return solid;
	}

	public boolean renderSide(World world, long x, long y, long z, int side) {
		Direction d = Direction.getDirection(side);
		long ox = x + d.getX();
		long oy = y + d.getY();
		long oz = z + d.getZ();
		if (world.getBlockType(ox, oy, oz).isSolid(world, ox, oy, oz, d.opposite().id())) return false;
		return true;
	}

	public boolean canStandOn() {
		return standable && !isAir;
	}

	public boolean canStandIn() {
		return standinable || isAir;
	}

	public int getRendererToUse(byte data, int side) {
		return 0;
	}

	protected String getModifiedName(ItemStack item) {
		return name;
	}

	public String getName(ItemStack item) {
		return "block." + getModifiedName(item) + ".name";
	}

	public String getDescription(ItemStack item) {
		return "block." + getModifiedName(item) + ".desc";
	}

	public String getLocalizedName(ItemStack item) {
		return Localization.getLocalization(getName(item));
	}

	public String getLocalizedDescription(ItemStack item) {
		return Localization.getLocalization(getDescription(item));
	}

	public Collection<ItemStack> getCreativeItems() {
		ArrayList<ItemStack> res = new ArrayList<>();
		res.add(new ItemStack(uid, 0, 1));
		return res;
	}

	private static class DuplicateBlockException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5673051503358295281L;

		public DuplicateBlockException(String message) {
			super(message);

		}

	}

	public ItemStack[] getItemDrops(World world, long x, long y, long z, ItemStack mined) {
		return new ItemStack[] { new ItemStack(uid, world.getBlockData(x, y, z), 1) };
	}

	// Block not yet changed to 0
	public void onBlockMined(World world, long x, long y, long z, ItemStack mined) {
		MiningData data = getMiningData(world, x, y, z);
		if (data == null) {
			if(mined != null) mined.damage();
		}
		dropItems(world, x, y, z, mined);
		world.setBlockId(x, y, z, 0);
	}
	
	public void dropItems(World world, long x, long y, long z, ItemStack mined){
		if(!getMiningData(world, x, y, z).onMine(mined)) return;
		ItemStack[] drops = getItemDrops(world, x, y, z, mined);
		for (ItemStack d : drops) {
			new EntityItem(world).setItem(d.clone()).setPosition(x + 0.5, y + 0.5, z + 0.5).setVelocity((rand.nextDouble() - 0.5) / 2d, 0, (rand.nextDouble() - 0.5) / 2d).addToWorld();
		}
		
	}

	public void onLeftClick(World world, long x, long y, long z, int side, EntityPlayer player, ItemStack item) {

	}

	public void onRightClick(World world, long x, long y, long z, int side, EntityPlayer player, ItemStack item) {

	}

	public MiningData getMiningData(World world, long x, long y, long z) {
		return mineData;
	}

	/**
	 * @return isAir
	 */
	public boolean isAir() {
		return isAir;
	}

	/**
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param i
	 * @return the texture index
	 */
	public int getTextureIndex(World world, long x, long y, long z, int side) {
		return textureIndex;
	}

	public void setOrientation(World world, long x, long y, long z, int side, TextureCoords coords) {

	}

	public void setOrientation(ItemStack item, int side, TextureCoords coords) {

	}

	public int getTextureIndex(ItemStack item, int side) {
		return textureIndex;
	}

	/**
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param i
	 * @return the color of the side of the block
	 */
	public Color getColor(World world, long x, long y, long z, int side) {
		return Color.white;
	}

	public void place(World world, long x, long y, long z, EntityPlayer player, ItemStack item) {
		world.setBlockIdData(x, y, z, uid, item.getData());
		item.decrementAmount();
	}

	public static Block minus = new Block((short) -1, 0, Material.getMaterial("air")).setAir(true);
	public static Block stone = new BlockStone((short) 1, Material.getMaterial("stone")).setSolid(true).setName("stone");
	public static Block air = new Block((short) 0, 0, Material.getMaterial("air")).setAir(true);

	/**
	 * @param b
	 */
	public Block setAir(boolean air) {
		isAir = air;
		if (air) solid = false;
		return this;
	}

	/**
	 * @param solid
	 *            the solid to set
	 */
	public Block setSolid(boolean solid) {
		this.solid = solid;
		return this;
	}

	public boolean replacableBy(World world, long x, long y, long z, int bid, byte data) {
		return isAir;
	}

	public boolean canGrowPlant(Plant plant) {
		return false;
	}

	public AABB getRenderSize(ItemStack item) {
		return square;
	}

	public AABB getRenderSize(World world, long x, long y, long z) {
		return square;
	}

	public RayTraceResult getRayTrace(World world, long x, long y, long z, Vector3d start, Vector3d end) {
		start = start.add(-x, -y, -z);
		end = end.add(-x, -y, -z);
		AABB bounds = world.getBlockType(x, y, z).getRayTraceSize(world, x, y, z);
		Vector3d minX = start.getXIntermediate(end, bounds.minX);
		Vector3d maxX = start.getXIntermediate(end, bounds.maxX);
		Vector3d minY = start.getYIntermediate(end, bounds.minY);
		Vector3d maxY = start.getYIntermediate(end, bounds.maxY);
		Vector3d minZ = start.getZIntermediate(end, bounds.minZ);
		Vector3d maxZ = start.getZIntermediate(end, bounds.maxZ);
		if (!bounds.isVecInYZ(minX)) minX = null;
		if (!bounds.isVecInYZ(maxX)) maxX = null;
		if (!bounds.isVecInXZ(minY)) minY = null;
		if (!bounds.isVecInXZ(maxY)) maxY = null;
		if (!bounds.isVecInXY(minZ)) minZ = null;
		if (!bounds.isVecInXY(maxZ)) maxZ = null;
		Vector3d using = null;
		if (minX != null && (using == null || start.floatDistanceTo(minX) < start.floatDistanceTo(using))) using = minX;
		if (maxX != null && (using == null || start.floatDistanceTo(maxX) < start.floatDistanceTo(using))) using = maxX;
		if (minY != null && (using == null || start.floatDistanceTo(minY) < start.floatDistanceTo(using))) using = minY;
		if (maxY != null && (using == null || start.floatDistanceTo(maxY) < start.floatDistanceTo(using))) using = maxY;
		if (minZ != null && (using == null || start.floatDistanceTo(minZ) < start.floatDistanceTo(using))) using = minZ;
		if (maxZ != null && (using == null || start.floatDistanceTo(maxZ) < start.floatDistanceTo(using))) using = maxZ;
		if (using == null) return null;
		byte b = -1;
		if (using == minX) b = 2;
		if (using == maxX) b = 0;
		if (using == minY) b = 5;
		if (using == maxY) b = 4;
		if (using == minZ) b = 3;
		if (using == maxZ) b = 1;
		minX = null;
		maxX = null;
		minY = null;
		maxY = null;
		minZ = null;
		maxZ = null;
		bounds = null;
		return new RayTraceResult(x, y, z, b, using.add(x, y, z));
	}

	public static void clearBlocks() {
		blocksList = new Block[2048];
		blocksList[0] = air;
		blocksList[1] = stone;
	}

	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {

	}

	/**
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		return square;
	}

	public boolean includeInRayTrace(byte data) {
		return !isAir;
	}

	/**
	 * @param current
	 * @param i
	 * @return The blocks color
	 */
	public Color getColor(ItemStack current, int side) {
		return Color.white;
	}

	/**
	 * @param data
	 * @return If the block should render it's item in 3d
	 */
	public boolean render3d(byte data) {
		return true;
	}

	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		if (!isAir || uid == -1) return new AABB[] { square };
		else return null;
	}

	public AABB[] getCollisionBounds(World world, long x, long y, long z) {
		AABB[] size = getCollisionSizes(world, x, y, z);
		if (size == null) return null;
		for (int d = 0; d < size.length; ++d) {
			size[d] = size[d].clone().add(x, y, z);
		}
		return size;
	}

	public int blockUpdate(World world, Random rand, long x, long y, long z, byte data) {
		return 0;
	}

}
