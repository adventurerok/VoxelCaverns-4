package vc4.client.graphics;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.*;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL30;

import vc4.api.Resources;
import vc4.api.graphics.*;
import vc4.api.graphics.texture.AnimatedTexture;
import vc4.api.logging.Logger;
import vc4.api.util.BufferUtils;
import vc4.api.yaml.YamlMap;

public class ClientAnimatedTexture implements AnimatedTexture {

	private static OpenGL gl;

	int tex;
	int arraysize;
	int width, height;
	boolean smooth = true, mipmap = true;
	boolean mipmapGen = false;
	boolean prefix = true;
	double yRatio = 1;

	GLTexture type = GLTexture.TEX_2D_ARRAY;

	private HashMap<String, Integer> indexNames = new HashMap<>();
	private ArrayList<Animation> animatedParts = new ArrayList<>();

	public ClientAnimatedTexture() {
		if (gl == null) gl = Graphics.getOpenGL();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.texture.Texture#getTexture(int)
	 */
	@Override
	public int getTexture() {
		return tex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.texture.Texture#getWidth()
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.texture.Texture#getHeight()
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.texture.Texture#getArraySize()
	 */
	@Override
	public int getArraySize() {
		return arraysize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.texture.Texture#getType()
	 */
	@Override
	public GLTexture getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.vc3d.texture.Texture#setSmooth(boolean)
	 */
	@Override
	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
		magMinChanged();
	}

	private void magMinChanged() {
		gl.bindTexture(type, tex);
		GLTextureFilter f;
		GLTextureFilter mip = null;
		if (smooth) {
			f = GLTextureFilter.LINEAR;
		} else {
			f = GLTextureFilter.NEAREST;
		}
		if (mipmap) mip = f;
		gl.texParameterMinFilter(type, f, mip);
		gl.texParameterMagFilter(type, f);
		if (mipmap && !mipmapGen) {
			GL30.glGenerateMipmap(type.getGlInt());
			mipmapGen = true;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.vc3d.texture.Texture#setMipmap(boolean)
	 */
	@Override
	public void setMipmap(boolean mipmap) {
		this.mipmap = mipmap;
		magMinChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.texture.Texture#bind()
	 */
	@Override
	public void bind() {
		Graphics.getOpenGL().bindTexture(getType(), getTexture());
	}

	@Override
	public int getArrayIndex(String tex) {
		return indexNames.get(tex);
	}

	@Override
	public boolean hasArrayIndex(String tex) {
		return indexNames.containsKey(tex);
	}

	@Override
	public void updateAnimation(int tickTime) {
		bind();
		for (Animation a : animatedParts) {
			a.update(tickTime, type);
		}
	}

	public AnimatedTexture load(ArrayList<LoadableAnimatedImage> images) {
		if (images.size() < 1) return this;
		Collections.sort(images);
		String yml = images.get(0).image.toString();
		yml = yml.substring(0, yml.lastIndexOf("/"));
		yml = yml.substring(yml.lastIndexOf("/"), yml.length());
		for (URL res : Resources.getResourceURLs()) {
			try {
				URL ymlFile = new URL(res.toString() + "/animatedtexture" + yml + "/texture.yml");
				YamlMap map = new YamlMap(ymlFile.openStream());
				if (map.hasKey("type")) type = GLTexture.valueOf(map.getString("type").toUpperCase());
				if (map.hasKey("prefix")) prefix = map.getBoolean("prefix");
				if (map.hasKey("yscale")) yRatio = map.getDouble("yscale");
				break;
			} catch (Exception e) {
			}
		}
		int widest = 0; // So we know which image is widest

		ArrayList<LoadingImage> toLoad = new ArrayList<>();
		for (LoadableAnimatedImage i : images) {
			try {
				BufferedImage img = ImageIO.read(i.getImageStream()); // Begin testing image
				if ((img.getWidth() & -img.getWidth()) != img.getWidth()) continue;
				if (img.getWidth() > 1024) continue; // 1024 is too big
				if (img.getHeight() % (int) (img.getWidth() * yRatio) != 0 || img.getHeight() < (img.getWidth() * yRatio)) continue; // Test passed if false
				if (img.getWidth() > widest) widest = img.getWidth();
				toLoad.add(new LoadingImage(i.texName, img, i.txt));
			} catch (IOException e) {
			}
		}
		ArrayList<LoadingImage> forAnimation = new ArrayList<>();
		ArrayList<LoadingImage> sizedImages = new ArrayList<>();
		for (LoadingImage i : toLoad) {
			int bigger = widest / i.image.getWidth();
			if (bigger > 1) {
				int h = (int) (i.image.getHeight() * bigger * yRatio);
				i.image = scale(i.image, widest, h);
			}
			if ((i.image.getWidth() * yRatio) != i.image.getHeight()) {
				forAnimation.add(i);
				i = new LoadingImage(i.name, i.image.getSubimage(0, 0, widest, (int) (widest * yRatio)), i.txt);
			}
			sizedImages.add(i);

		}
		arraysize = sizedImages.size();
		if (prefix) arraysize += 2;
		width = widest;
		height = (int) (widest * yRatio);
		ByteBuffer data = BufferUtils.createByteBuffer(width * height * arraysize * 4);
		int index = prefix ? 2 : 0;
		if (prefix) {
			indexNames.put("transparent", 0);
			indexNames.put("white", 1);
			int oneFrame = width * height;
			for (int d = 0; d < oneFrame; ++d) {
				data.put((byte) 255);
				data.put((byte) 255);
				data.put((byte) 255);
				data.put((byte) 0);
			}
			for (int d = 0; d < oneFrame; ++d) {
				data.put((byte) 255);
				data.put((byte) 255);
				data.put((byte) 255);
				data.put((byte) 255);
			}
		}
		for (LoadingImage i : sizedImages) {
			indexNames.put(i.name, index++);
			BufferedImage b = i.image;
			int[] pixels = new int[b.getWidth() * b.getHeight()];
			b.getRGB(0, 0, b.getWidth(), b.getHeight(), pixels, 0, b.getWidth());
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = pixels[y * b.getWidth() + x];
					if (((pixel >> 24) & 0xFF) < 2) {
						data.put((byte) 255);
						data.put((byte) 255);
						data.put((byte) 255);
						data.put((byte) 0);
					} else {
						data.put((byte) ((pixel >> 16) & 0xFF)); // Red component
						data.put((byte) ((pixel >> 8) & 0xFF)); // Green component
						data.put((byte) (pixel & 0xFF)); // Blue component
						data.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
					}
				}
			}
		}
		data.flip();
		int texid = gl.genTextures();
		gl.bindTexture(type, texid);
		gl.texImage3D(type, 0, GLInternalFormat.RGBA8, width, height, arraysize, false, GLFormat.RGBA, GLType.UNSIGNED_BYTE, data);

		setMipmap(false);
		setSmooth(false);

		gl.bindTexture(type, 0);

		tex = texid;

		// gl.texParameterMagFilter(GLTexture.TEX_2D_ARRAY, GLTextureFilter.NEAREST);

		for (LoadingImage i : forAnimation) {
			Animation ani = new Animation();
			ani.zOffset = getArrayIndex(i.name);
			ani.width = widest;
			ani.height = (int) (widest * yRatio);
			ani.depth = 1;
			BufferedImage img = i.image;
			ani.parts = new ByteBuffer[(int) (i.image.getHeight() / (i.image.getWidth() * yRatio))];
			for (int d = 0; d < ani.parts.length; ++d) {
				BufferedImage sub = img.getSubimage(0, d * height, width, height);
				ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
				int[] pixels = new int[sub.getWidth() * sub.getHeight()];
				sub.getRGB(0, 0, sub.getWidth(), sub.getHeight(), pixels, 0, sub.getWidth());
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int pixel = pixels[y * sub.getWidth() + x];
						if (((pixel >> 24) & 0xFF) < 2) {
							buf.put((byte) 255);
							buf.put((byte) 255);
							buf.put((byte) 255);
							buf.put((byte) 0);
						} else {
							buf.put((byte) ((pixel >> 16) & 0xFF)); // Red component
							buf.put((byte) ((pixel >> 8) & 0xFF)); // Green component
							buf.put((byte) (pixel & 0xFF)); // Blue component
							buf.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
						}
					}
				}
				buf.flip();
				ani.parts[d] = buf;
			}
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(i.txt.openStream()))) {
				ani.loadFrames(reader.readLine());
			} catch (IOException e) {
				Logger.getLogger(ClientAnimatedTexture.class).warning("Error while loading " + i.name, e);
			}
			animatedParts.add(ani);
		}
		return this;
	}

	private BufferedImage scale(BufferedImage srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	private static class LoadingImage {
		public String name;
		public BufferedImage image;
		public URL txt;

		public LoadingImage(String name, BufferedImage image, URL txt) {
			super();
			this.name = name;
			this.image = image;
			this.txt = txt;
		}

	}

}
