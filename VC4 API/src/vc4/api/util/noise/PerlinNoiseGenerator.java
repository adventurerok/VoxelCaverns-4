package vc4.api.util.noise;

import java.util.Random;

import vc4.api.util.XORShiftRandom;

public class PerlinNoiseGenerator extends NoiseGenerator {
	protected static final int[][] grad3 = { { 1, 1, 0 }, { -1, 1, 0 },
			{ 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 },
			{ 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 },
			{ 0, 1, -1 }, { 0, -1, -1 } };

	private static final PerlinNoiseGenerator instance = new PerlinNoiseGenerator();

	protected PerlinNoiseGenerator() {
		final int[] p = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53,
				194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240,
				21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94,
				252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56,
				87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134,
				139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60,
				211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102,
				143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187,
				208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164,
				100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5,
				202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227,
				47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248,
				152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
				129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178,
				185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210,
				144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239,
				107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176,
				115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222,
				114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156,
				180 };

		for (int i = 0; i < 512; ++i)
			this.perm[i] = p[(i & 0xFF)];
	}

	public PerlinNoiseGenerator(final long seed) {
		this(new XORShiftRandom(seed));
	}

	public PerlinNoiseGenerator(final Random rand) {
		this.offsetX = (rand.nextDouble() * 256.0D);
		this.offsetY = (rand.nextDouble() * 256.0D);
		this.offsetZ = (rand.nextDouble() * 256.0D);

		for (int i = 0; i < 256; ++i) {
			this.perm[i] = rand.nextInt(256);
		}

		for (int i = 0; i < 256; ++i) {
			final int pos = rand.nextInt(256 - i) + i;
			final int old = this.perm[i];

			this.perm[i] = this.perm[pos];
			this.perm[pos] = old;
			this.perm[(i + 256)] = this.perm[i];
		}
	}

	public static double getNoise(final double x) {
		return instance.noise(x);
	}

	public static double getNoise(final double x, final double y) {
		return instance.noise(x, y);
	}

	public static double getNoise(final double x, final double y, final double z) {
		return instance.noise(x, y, z);
	}

	public static PerlinNoiseGenerator getInstance() {
		return instance;
	}

	@Override
	public double noise(double x, double y, double z) {
		x += this.offsetX;
		y += this.offsetY;
		z += this.offsetZ;

		final int floorX = floor(x);
		final int floorY = floor(y);
		final int floorZ = floor(z);

		final int X = floorX & 0xFF;
		final int Y = floorY & 0xFF;
		final int Z = floorZ & 0xFF;

		x -= floorX;
		y -= floorY;
		z -= floorZ;

		final double fX = fade(x);
		final double fY = fade(y);
		final double fZ = fade(z);

		final int A = this.perm[X] + Y;
		final int AA = this.perm[A] + Z;
		final int AB = this.perm[(A + 1)] + Z;
		final int B = this.perm[(X + 1)] + Y;
		final int BA = this.perm[B] + Z;
		final int BB = this.perm[(B + 1)] + Z;

		return lerp(
				fZ,
				lerp(fY,
						lerp(fX, grad(this.perm[AA], x, y, z),
								grad(this.perm[BA], x - 1.0D, y, z)),
						lerp(fX, grad(this.perm[AB], x, y - 1.0D, z),
								grad(this.perm[BB], x - 1.0D, y - 1.0D, z))),
				lerp(fY,
						lerp(fX,
								grad(this.perm[(AA + 1)], x, y, z - 1.0D),
								grad(this.perm[(BA + 1)], x - 1.0D, y, z - 1.0D)),
						lerp(fX,
								grad(this.perm[(AB + 1)], x, y - 1.0D, z - 1.0D),
								grad(this.perm[(BB + 1)], x - 1.0D, y - 1.0D,
										z - 1.0D))));
	}

	public static double getNoise(final double x, final int octaves,
			final double frequency, final double amplitude) {
		return instance.noise(x, octaves, frequency, amplitude);
	}

	public static double getNoise(final double x, final double y,
			final int octaves, final double frequency, final double amplitude) {
		return instance.noise(x, y, octaves, frequency, amplitude);
	}

	public static double getNoise(final double x, final double y,
			final double z, final int octaves, final double frequency,
			final double amplitude) {
		return instance.noise(x, y, z, octaves, frequency, amplitude);
	}
}
