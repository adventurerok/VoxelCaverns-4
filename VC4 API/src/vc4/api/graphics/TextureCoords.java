package vc4.api.graphics;

import vc4.api.util.AABB;
import vc4.api.vector.Vector3f;

public class TextureCoords {

	Vector3f t[] = new Vector3f[4];
	int number;
	int flips;

	public TextureCoords(AABB bounds, int dir, double tex) {
		if (dir < 3) {
			if (dir == 0) setupNorth(bounds, tex);
			else if (dir == 1) setupEast(bounds, tex);
			else if (dir == 2) setupSouth(bounds, tex);
		} else {
			if (dir == 3) setupWest(bounds, tex);
			else if (dir == 4) setupTop(bounds, tex);
			else if (dir == 5) setupBottom(bounds, tex);
		}
	}

	public void setOrientation(int or) {
		number = or;
	}

	public Vector3f next() {
		Vector3f res = t[number ^ flips];
		number = (number + 1) & 3;
		return res;
	}

	public void flipHorizontal() {
		flips ^= 1;
	}

	public void flipVertical() {
		flips ^= 2;
	}

	public void flipHorizontal(boolean flip) {
		if (flip) flips |= 1;
		else flips &= ~1;
	}

	public void flipVertical(boolean flip) {
		if (flip) flips |= 2;
		else flips &= ~2;
	}

	private void setupEast(AABB bounds, double tex) {
		t[0] = new Vector3f(bounds.minX, 1 - bounds.maxY, tex);
		t[1] = new Vector3f(bounds.maxX, 1 - bounds.maxY, tex);
		t[2] = new Vector3f(bounds.maxX, 1 - bounds.minY, tex);
		t[3] = new Vector3f(bounds.minX, 1 - bounds.minY, tex);
	}

	private void setupNorth(AABB bounds, double tex) {
		t[0] = new Vector3f(bounds.minZ, 1 - bounds.maxY, tex);
		t[1] = new Vector3f(bounds.maxZ, 1 - bounds.maxY, tex);
		t[2] = new Vector3f(bounds.maxZ, 1 - bounds.minY, tex);
		t[3] = new Vector3f(bounds.minZ, 1 - bounds.minY, tex);
	}

	private void setupWest(AABB bounds, double tex) {
		t[0] = new Vector3f(bounds.minX, 1 - bounds.maxY, tex);
		t[1] = new Vector3f(bounds.maxX, 1 - bounds.maxY, tex);
		t[2] = new Vector3f(bounds.maxX, 1 - bounds.minY, tex);
		t[3] = new Vector3f(bounds.minX, 1 - bounds.minY, tex);
	}

	private void setupSouth(AABB bounds, double tex) {
		t[0] = new Vector3f(bounds.minZ, 1 - bounds.maxY, tex);
		t[1] = new Vector3f(bounds.maxZ, 1 - bounds.maxY, tex);
		t[2] = new Vector3f(bounds.maxZ, 1 - bounds.minY, tex);
		t[3] = new Vector3f(bounds.minZ, 1 - bounds.minY, tex);
	}

	private void setupTop(AABB bounds, double tex) {
		t[0] = new Vector3f(bounds.minX, bounds.minZ, tex);
		t[1] = new Vector3f(bounds.maxX, bounds.minZ, tex);
		t[2] = new Vector3f(bounds.maxX, bounds.maxZ, tex);
		t[3] = new Vector3f(bounds.minX, bounds.maxZ, tex);
	}

	private void setupBottom(AABB bounds, double tex) {
		t[0] = new Vector3f(bounds.minX, bounds.minZ, tex);
		t[1] = new Vector3f(bounds.maxX, bounds.minZ, tex);
		t[2] = new Vector3f(bounds.maxX, bounds.maxZ, tex);
		t[3] = new Vector3f(bounds.minX, bounds.maxZ, tex);
	}

	public TextureCoords(Vector3f... t) {
		super();
		this.t = t;
	}

	/**
	 * @param tex
	 * @param max
	 * @param mix
	 * @param miz
	 * @param maz
	 * @return A pipe tex 0 type tex coord
	 */
	public static TextureCoords pipeTex_0(final int tex, final float max, final float mix, final float miz, final float maz) {
		return new TextureCoords(new Vector3f(max, miz, tex), new Vector3f(max, maz, tex), new Vector3f(mix, maz, tex), new Vector3f(mix, miz, tex));
	}

	public static TextureCoords pipeTex_1(final int tex, final float max, final float mix, final float miz, final float maz) {
		return new TextureCoords(new Vector3f(max, miz, tex), new Vector3f(mix, miz, tex), new Vector3f(mix, maz, tex), new Vector3f(max, maz, tex));
	}
}
