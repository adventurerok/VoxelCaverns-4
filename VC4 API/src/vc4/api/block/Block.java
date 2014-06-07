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
	public static boolean canFallableFall = true;

	public final short uid;
	public final Material material;
	public final int textureIndex;

	protected float timeToDestroy;
	protected float blastResistance = 1;
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

	static {

		Arrays.fill(blockOpacity, (byte) 15);
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
	
	public Block setBlastResistance(float res){
		this.blastResistance = res;
		return this;
	}

	/**
	 * Sets the opacity of the block
	 * 
	 * @param opac
	 *            The new block opacity, from 1 to 15
	 * @return The modified block
	 */
	public Block setLightOpacity(int opac) {
		blockOpacity[uid] = (byte) opac;
		return this;
	}

	/**
	 * Sets the light level the block emits
	 * 
	 * @param lvl
	 *            The light level the block emits, from 0 to 15
	 * @return The modified block
	 */
	public Block setLightLevel(int lvl) {
		blockLight[uid] = (byte) lvl;
		return this;
	}

	public void onEntityTickInside(World world, long x, long y, long z, Entity entity) {

	}

	public void onEntityStandOn(World world, long x, long y, long z, Entity entity) {

	}

	public void onEntityCollideHorizontal(World world, long x, long y, long z, Entity entity) {

	}

	public Block(int uid, int texture, Material m) {
		this((short) uid, texture, m);
	}

	public Block(int uid, int texture, String material) {
		this((short) uid, texture, Material.getMaterial(material));
	}

	/**
	 * Sets the name of the block that is used in language files
	 * 
	 * @param name
	 *            The name of the block (e.g. "stone" for stone)
	 * @return The modified block
	 */
	public Block setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * If the block has a special left click function
	 * 
	 * @param world
	 *            The world the block is located in
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
	 * @return A boolean if the block has a special left click
	 */
	public boolean overrideLeftClick(World world, long x, long y, long z) {
		return false;
	}

	/**
	 * If the block has a special right click function
	 * 
	 * @param world
	 *            The world the block is located in
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
	 * @return A boolean if the block has a special right click
	 */
	public boolean overrideRightClick(World world, long x, long y, long z) {
		return false;
	}

	/**
	 * Gets the block object with the given id
	 * 
	 * @param id
	 *            The id of the block. From -1 to 2047
	 * @return The block object with the given id
	 */
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

	/**
	 * Gets the renderer array index to use 0: Solid render array (back face culling) 1: Transparent render array (no culling) 2: Fluid render array (renders after entities)
	 * 
	 * @param data
	 *            The metadata of the block
	 * @param side
	 *            The side of the block that is being rendered
	 * @return The renderer array index to use.
	 */
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
			if (mined != null) mined.damage();
		}
		dropItems(world, x, y, z, mined);
		world.setBlockId(x, y, z, 0);
	}

	public void dropItems(World world, long x, long y, long z, ItemStack mined) {
		if (!getMiningData(world, x, y, z).onMine(mined)) return;
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
	 *            The world the block is located in
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
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
	 *            The world the block is located in
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
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
	 * @param air
	 * @return The modified block
	 */
	public Block setAir(boolean air) {
		isAir = air;
		if (air) solid = false;
		return this;
	}

	/**
	 * @param solid
	 *            If the block is solid (not transparent)
	 * @return The modified block
	 */
	public Block setSolid(boolean solid) {
		this.solid = solid;
		return this;
	}

	public boolean replacableBy(World world, long x, long y, long z, int bid, byte data) {
		return isAir;
	}

	/**
	 * Checks if the block can have a plant grown on it
	 * 
	 * @param plant
	 *            The {@link Plant} to grow
	 * @return If the plant can grow on this block
	 */
	public boolean canGrowPlant(Plant plant) {
		return false;
	}

	/**
	 * Gets the {@link AABB} size to render the block as an item in the inventory
	 * 
	 * @param item
	 *            The {@link ItemStack} that is being rendered
	 * @return The aabb size to render the itemstack
	 */
	public AABB getRenderSize(ItemStack item) {
		return square;
	}

	/**
	 * Gets the {@link AABB} size to render the block as default aabb(0,0,0,1,1,1)
	 * 
	 * @param world
	 *            The world the block is located in
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
	 * @return The {@link AABB} size to render the block
	 */
	public AABB getRenderSize(World world, long x, long y, long z) {
		return square;
	}

	/**
	 * 
	 * @param world
	 *            The world the block is located in
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
	 * @param start
	 *            The start vector of the ray trace
	 * @param end
	 *            The end vector of the ray trace
	 * @return A {@link RayTraceResult} if successful, or null if unsuccessful
	 */
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

	/**
	 * Clears the block list. Called first when a world is loaded
	 */
	public static void clearBlocks() {
		blocksList = new Block[2048];
		blocksList[0] = air;
		blocksList[1] = stone;
	}

	/**
	 * Alerts the block that a nearby block has changed and allows it to update
	 * 
	 * @param world
	 *            The world the block is located in
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
	 * @param dir
	 *            The direction of the changed nearby block (0-5)
	 */
	public void nearbyBlockChanged(World world, long x, long y, long z, Direction dir) {

	}

	/**
	 * Gets the ray trace size for the given block
	 * 
	 * @param world
	 *            The world the block is located in
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
	 * @return The AABB that is to be used when doing ray trace detection
	 */
	public AABB getRayTraceSize(World world, long x, long y, long z) {
		return square;
	}

	/**
	 * Gets if the block is raytracable (if it can be included in the ray trace)
	 * 
	 * @param data
	 *            The metadata of the block
	 * @return If the block should be included in the ray trace
	 */
	public boolean includeInRayTrace(byte data) {
		return !isAir;
	}

	/**
	 * Gets the color to render the item of the block
	 * 
	 * @param current
	 *            The itemstack being rendered
	 * @param side
	 *            The side of the block being rendered
	 * @return The color to render the block
	 */
	public Color getColor(ItemStack current, int side) {
		return Color.white;
	}

	/**
	 * Gets if the block wants to look like a cube (true) or a square (false) in the inventory
	 * 
	 * @param data
	 * @return If the block should render it's item in 3d
	 */
	public boolean render3d(byte data) {
		return true;
	}

	/**
	 * Gets the collision sizes for the block at the location, stored in an array Collision sizes are the collision data before it has been transformed by the block position
	 * 
	 * @param world
	 *            The world the block is located in
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
	 * @return An array containing the collision sizes for the block
	 */
	public AABB[] getCollisionSizes(World world, long x, long y, long z) {
		if (!isAir || uid == -1) return new AABB[] { square };
		else return null;
	}
	
	public float getBlastResistance(World world, long x, long y, long z, Entity exploder){
		return blastResistance;
	}

	/**
	 * Gets the collision bounds for the block at the location, stored in an array Collision bounds are the collision data after it has been transformed by the block position
	 * 
	 * Calls the {@link #getCollisionSizes(World, long, long, long)} method, and transforms the results
	 * 
	 * @param world
	 *            The world the block is located in
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
	 * @return An array containing the collision bounds for the block
	 */
	public AABB[] getCollisionBounds(World world, long x, long y, long z) {
		AABB[] size = getCollisionSizes(world, x, y, z);
		if (size == null) return null;
		for (int d = 0; d < size.length; ++d) {
			size[d] = size[d].clone().add(x, y, z);
		}
		return size;
	}

	/**
	 * Updates the block (makes grass spread, crops grow, etc...)
	 * 
	 * @param world
	 *            The world the block is located in
	 * @param rand
	 *            The random number generator to use while updating
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
	 * @param data
	 *            The metadata of the block (included for performance reasons)
	 * @param buid
	 *            The block update type id (0 for random, 1 for scheduled, users can specify others)
	 * @return The number of ticks to wait before a scheduled block update, or 0 for no block update
	 */
	public int blockUpdate(World world, Random rand, long x, long y, long z, byte data, int buid) {
		return 0;
	}

	public int getProvidingSignal(World world, long x, long y, long z, int side) {
		return 0;
	}

	public int getDirectSignal(World world, long x, long y, long z) {
		return getDirectSignal(world, x, y, z, -1);
	}

	public int getDirectSignal(World world, long x, long y, long z, int nocheck) {
		int max = 0;
		for (int dofor = 0; dofor < 6; ++dofor) {
			if (dofor == nocheck) continue;
			Direction dir = Direction.getDirection(dofor);
			long nx = x + dir.getX();
			long ny = y + dir.getY();
			long nz = z + dir.getZ();
			int at = world.getBlockType(nx, ny, nz).getProvidingSignal(world, nx, ny, nz, dir.opposite().id());
			if (at > max) max = at;
			if (max == 15) return max;
		}
		return max;
	}

	public int getIndirectSignal(World world, long x, long y, long z) {
		int max = 0;
		for (int dofor = 0; dofor < 6; ++dofor) {
			Direction dir = Direction.getDirection(dofor);
			long nx = x + dir.getX();
			long ny = y + dir.getY();
			long nz = z + dir.getZ();
			dir = dir.opposite();
			if (!world.getBlockType(nx, ny, nz).isSolid(world, nx, ny, nz, dir.id())) {
				int at = world.getBlockType(nx, ny, nz).getProvidingSignal(world, nx, ny, nz, dir.id());
				if (at > max) max = at;
				if (max == 15) return max;
			}
			int at = world.getBlockType(nx, ny, nz).getDirectSignal(world, nx, ny, nz, dir.id());
			if (at > max) max = at;
			if (max == 15) return max;
		}
		return max;
	}

	/**
	 * Checks if the block takes signal (kradonium) input or, if kradonium cables should render as sticking into it
	 * 
	 * @param world
	 *            The world the block is located in
	 * @param x
	 *            The x coordinate of the block in the world
	 * @param y
	 *            The y coordinate of the block in the world
	 * @param z
	 *            The z coordinate of the block in the world
	 * @param side
	 *            The side of the block that is being tested
	 * @return If the side specified of the block takes signal input
	 */
	public boolean takesSignalInput(World world, long x, long y, long z, int side) {
		return false;
	}

	/**
	 * Gets if the block has random block updates
	 * 
	 * @return If the block is to recieve random block updates
	 */
	public boolean updatesRandomly() {
		return true;
	}

	public void onBlockExploded(World world, long x, long y, long z, Entity exploder) {
		
	}

}
