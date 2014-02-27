package vc4.vanilla.block;

import java.awt.Color;

import vc4.api.block.Block;
import vc4.api.item.ItemStack;
import vc4.api.world.World;
import vc4.vanilla.BlockTexture;
import vc4.vanilla.Vanilla;

public class BlockWool extends Block {

	int wid;

	public BlockWool(int uid, int wid) {
		super(uid, BlockTexture.wool, "cloth");
		this.wid = wid * 32;
	}

	@Override
	public Color getColor(World world, long x, long y, long z, int side) {
		return Vanilla.woolColors[wid + world.getBlockData(x, y, z)];
	}

	@Override
	public Color getColor(ItemStack current, int side) {
		return Vanilla.woolColors[wid + current.getDamage()];
	}

	@Override
	protected String getModifiedName(ItemStack item) {
		if (item.getDamage() < 16) return modName0(item.getDamage());
		if (item.getDamage() < 32) return modName1(item.getDamage());
		if (item.getDamage() < 48) return modName2(item.getDamage());
		if (item.getDamage() < 64) return modName3(item.getDamage());
		return name;
	}

	/**
	 * @param damage
	 * @return
	 */
	private String modName3(int damage) {
		switch (damage) {
			case 0:
				return name + ".yellow";
			case 1:
				return name + ".springbud";
			case 2:
				return name + ".brightgreen";
			case 3:
				return name + ".lime";
			case 4:
				return name + ".orange";
			case 5:
				return name + ".citrus";
			case 6:
				return name + ".limade";
			case 7:
				return name + ".islamicgreen";
			case 8:
				return name + ".internationalorange";
			case 9:
				return name + ".tenne";
			case 10:
				return name + ".olive";
			case 11:
				return name + ".green";
			case 12:
				return name + ".red";
			case 13:
				return name + ".freespeechred";
			case 14:
				return name + ".maroon";
			case 15:
				return name + ".black";
		}
		return "wool";
	}

	/**
	 * @param damage
	 * @return
	 */
	private String modName2(int damage) {
		switch (damage) {
			case 0:
				return name + ".lazerlemon";
			case 1:
				return name + ".greenyellow";
			case 2:
				return name + ".screamingreen";
			case 3:
				return name + ".springgreen";
			case 4:
				return name + ".koromiko";
			case 5:
				return name + ".olivegreen";
			case 6:
				return name + ".fruitsalad";
			case 7:
				return name + ".pigmentgreen";
			case 8:
				return name + ".tomato";
			case 9:
				return name + ".appleblossom";
			case 10:
				return name + ".darkgray";
			case 11:
				return name + ".mosque";
			case 12:
				return name + ".torchred";
			case 13:
				return name + ".eggplant";
			case 14:
				return name + ".tyrianpurple";
			case 15:
				return name + ".navy";
		}
		return "wool";
	}

	/**
	 * @param damage
	 * @return
	 */
	private String modName1(int damage) {
		switch (damage) {
			case 0:
				return name + ".canary";
			case 1:
				return name + ".mintgreen";
			case 2:
				return name + ".aquamarine";
			case 3:
				return name + ".mediumspringgreen";
			case 4:
				return name + ".melon";
			case 5:
				return name + ".lightgray";
			case 6:
				return name + ".fountainblue";
			case 7:
				return name + ".persiangreen";
			case 8:
				return name + ".brilliantrose";
			case 9:
				return name + ".violetblue";
			case 10:
				return name + ".richblue";
			case 11:
				return name + ".cobalt";
			case 12:
				return name + ".hollywoodcerise";
			case 13:
				return name + ".darkmagenta";
			case 14:
				return name + ".indigo";
			case 15:
				return name + ".midnightblue";
		}
		return "wool";
	}

	/**
	 * @param damage
	 * @return
	 */
	private String modName0(int damage) {
		switch (damage) {
			case 0:
				return name + ".white";
			case 1:
				return name + ".anakwia";
			case 2:
				return name + ".babyblue";
			case 3:
				return name + ".cyan";
			case 4:
				return name + ".lavenderrose";
			case 5:
				return name + ".melrose";
			case 6:
				return name + ".malibu";
			case 7:
				return name + ".azureradiance";
			case 8:
				return name + ".pinkflamingo";
			case 9:
				return name + ".lightslateblue";
			case 10:
				return name + ".neonblue";
			case 11:
				return name + ".navyblue";
			case 12:
				return name + ".magenta";
			case 13:
				return name + ".electricpurple";
			case 14:
				return name + ".electricindigo";
			case 15:
				return name + ".blue";
		}
		return "wool";
	}

}
