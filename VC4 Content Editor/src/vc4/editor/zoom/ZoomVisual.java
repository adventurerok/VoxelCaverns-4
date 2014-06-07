package vc4.editor.zoom;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import vc4.api.biome.*;
import vc4.api.world.World;
import vc4.editor.util.TestWorld;

public class ZoomVisual {

	private static long seed = new Random().nextLong();
	// private static long seed = 1919273631L;
	private static int width = 16;
	// private static int minus = width / 2;
	private static int minus = -8;
	private static int size = 32;
	private static int imgSize = size * width;

	static World world = new TestWorld(seed);

	public static void main(String[] args) {
		int[] result = doFourZooms();
		displayImage(result, "zoomgentest");
		size *= width;
		width = 1;
		int[] correct = doFourZooms();
		System.out.println("Percentage equal: " + percentEqual(result, correct));
		displayImage(correct, "correctzoom");
	}

	public static String percentEqual(int[] a, int[] a2) {
		if (a == a2) return "100%";
		if (a == null || a2 == null) return "0%";

		int length = a.length;
		if (a2.length != length) return "0%";

		int neq = 0;

		for (int i = 0; i < length; i++)
			if (a[i] != a2[i]) {
				neq++;
				// System.out.println("Not equal at index: " + i);
			}

		return (100 - (neq / (double) a.length) * 100) + "% (" + neq + " errors)";
	}

	public static void displayImage(int[] img, String save) {
		// img = biomesToPixels(img);
		img = heightsToPixels(img);
		BufferedImage image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = (WritableRaster) image.getData();
		raster.setPixels(0, 0, imgSize, imgSize, img);
		image.setData(raster);
		BufferedImage display = image = scale(image, 640, 640);
		ImageIcon icon = new ImageIcon(display);
		int ret = JOptionPane.showConfirmDialog(null, "Result", "Result", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
		if (ret == JOptionPane.YES_OPTION) {
			String path = "C:\\Users\\paul\\Pictures\\" + save + ".png";
			try {
				OutputStream out = new FileOutputStream(path);
				ImageIO.write(image, "PNG", out);
				out.close();
				image.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static int[] doFourZooms() {
		int[][] funcs = new int[width * width][];
		for (int x = 0; x < width; ++x) {
			for (int z = 0; z < width; ++z) {
				funcs[z * width + x] = doZoomFunc(x - minus, z - minus);
			}
		}
		int[] result = new int[size * size * width * width];
		int size3 = size * width;
		for (int x = 0; x < size3; ++x) {
			int ax = x / size;
			int px = x % size;
			for (int z = 0; z < size3; ++z) {
				int az = z / size;
				int pz = z % size;
				int[] root = funcs[az * width + ax];
				result[x * size3 + z] = root[pz * size + px];
			}
		}
		return result;
	}

	private static int[] doZoomFunc(int x, int z) {
		ZoomGenerator par = new HeightGenSeed(world.getSeed());
		par = new HeightGenZoom(world.getSeed(), par);
		par = new HeightGenDisplace(world.getSeed(), par, 1 / 2f);
		par = new HeightGenZoom(world.getSeed(), par);
		par = new HeightGenDisplace(world.getSeed(), par, 1 / 4f);
		par = new HeightGenZoom(world.getSeed(), par);
		par = new HeightGenDisplace(world.getSeed(), par, 1 / 8f);
		par = new HeightGenZoom(world.getSeed(), par);
		par = new HeightGenDisplace(world.getSeed(), par, 1 / 16f);
		par = new HeightGenZoom(world.getSeed(), par);
		par = new HeightGenZoom(world.getSeed(), par);
		par = new BiomeGenZoom(world.getSeed(), par);
		long start = System.nanoTime();
		int[] result = par.generate(x * size, z * size, size);

		long time = (System.nanoTime() - start) / 1000000;
		System.out.println("Took " + time + " ms");
		return result;
	}

	private static BufferedImage scale(BufferedImage srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	public static int[] biomesToPixels(int[] result) {
		int[] out = new int[result.length * 3];
		for (int d = 0; d < result.length; ++d) {
			int col = Biome.byId(result[d]).mapColor.getRGB();
			out[d * 3] = col >> 16;
			out[d * 3 + 1] = col >> 8;
			out[d * 3 + 2] = col;
		}
		return out;
	}

	public static int[] heightsToPixels(int[] result) {
		int[] out = new int[result.length * 3];
		for (int d = 0; d < result.length; ++d) {
			int all = result[d] + 127;
			out[d * 3] = all;
			out[d * 3 + 1] = all;
			out[d * 3 + 2] = all;
		}
		return out;
	}
}
