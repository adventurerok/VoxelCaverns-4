/**
 * 
 */
package vc4.client.graphics;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import vc4.api.graphics.*;
import vc4.api.graphics.texture.*;
import vc4.api.logging.Logger;
import vc4.impl.plugin.PluginManager;

/**
 * @author paul
 * 
 */
public class ClientSheetTextureLoader implements SheetTextureLoader {

	private static HashMap<String, LoadableTexture> registeredTextures;
	private static OpenGL gl;

	private static ArrayList<URL> getImageUrlsInPackage(String packageName) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL packageURL;
		ArrayList<URL> names = new ArrayList<URL>();

		packageName = packageName.replace(".", "/");
		packageURL = classLoader.getResource(packageName);

		if (packageURL.getProtocol().equals("jar")) {
			String jarFileName;
			Enumeration<JarEntry> jarEntries;
			String entryName;

			// build jar file name, then loop through zipped entries
			jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
			jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
			try (JarFile jf = new JarFile(jarFileName)) {
				jarEntries = jf.entries();
				while (jarEntries.hasMoreElements()) {
					JarEntry je = jarEntries.nextElement();

					entryName = je.getName();
					// System.out.println(entryName);
					if (entryName.startsWith(packageName) && entryName.length() > 5 && entryName.endsWith(".png")) {
						entryName = entryName.substring(packageName.length(), entryName.lastIndexOf('.'));
						names.add(new URL(packageURL + entryName + ".png"));
					}
				}
			}

			// loop through files in classpath
		} else {
			File folder = new File(URLDecoder.decode(packageURL.getFile(), "UTF-8"));
			File[] contenuti = folder.listFiles();
			// if(contenuti == null) return names;
			String entryName;
			for (File actual : contenuti) {
				entryName = actual.getName();
				if (!entryName.endsWith(".png")) continue;
				names.add(actual.toURI().toURL());
			}
		}
		return names;
	}

	private static ArrayList<URL> getImageURLs() {
		ArrayList<URL> result = new ArrayList<>();
		for (URL url : PluginManager.getResourceURLs()) {
			try {
				URL mod = new URL(url.toString() + "/sheettexture/");
				result.addAll(getImageUrlsElsewhere(mod));
			} catch (IOException e) {
				Logger.getLogger(ClientAnimatedTextureLoader.class).warning("Exception occured", e);
			}
		}
		return result;
	}

	private static ArrayList<URL> getImageUrlsElsewhere(URL baseURL) throws IOException {
		URL packageURL;
		ArrayList<URL> names = new ArrayList<URL>();

		packageURL = baseURL;

		if (packageURL.getProtocol().equals("jar")) {
			String jarFileName;
			Enumeration<JarEntry> jarEntries;
			String entryName;

			// build jar file name, then loop through zipped entries
			jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
			String packName = jarFileName.substring(jarFileName.indexOf("!") + 2);
			jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));

			try (JarFile jf = new JarFile(jarFileName)) {
				jarEntries = jf.entries();
				while (jarEntries.hasMoreElements()) {
					JarEntry je = jarEntries.nextElement();

					entryName = je.getName();
					// System.out.println(entryName);
					if (entryName.startsWith(packName) && entryName.length() > 5 && entryName.endsWith(".png")) {
						entryName = entryName.substring(packName.length(), entryName.lastIndexOf('.'));
						names.add(new URL(packageURL + entryName + ".png"));
					}
				}
			}

			// loop through files in classpath
		} else {
			File folder = new File(URLDecoder.decode(packageURL.getFile(), "UTF-8"));
			File[] contenuti = folder.listFiles();
			if (contenuti == null) return names;
			String entryName;
			for (File actual : contenuti) {
				entryName = actual.getName();
				if (!entryName.endsWith(".png")) continue;
				names.add(actual.toURI().toURL());
			}
		}
		return names;
	}

	/**
	 * 
	 */
	public ClientSheetTextureLoader() {
		gl = Graphics.getOpenGL();
		Graphics.setSheetLoader(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.texture.TextureLoader#loadTexture(java.lang.String)
	 */
	@Override
	public SheetTexture loadTexture(String path) throws IOException {
		path = path.replace(".", "/");
		if (path.contains("/")) {
			String pack = path.substring(0, path.lastIndexOf("/"));
			HashMap<String, LoadableTexture> loadPoss = getLoadableTextures(getImageUrlsInPackage(pack));
			String name = path.substring(path.lastIndexOf("/") + 1);
			LoadableTexture l = loadPoss.get(name);
			return l.load();
		} else {
			if (registeredTextures == null) {
				registeredTextures = getLoadableTextures(getImageURLs());
			}
			String name = path.substring(path.lastIndexOf("/") + 1);
			LoadableTexture l = registeredTextures.get(name);
			return l.load();
		}
	}

	private static ArrayList<LoadableSheetImage> getLoadableImages(ArrayList<URL> urls) throws IOException {
		ArrayList<LoadableSheetImage> result = new ArrayList<LoadableSheetImage>();
		for (URL l : urls) {
			String url = l.toString();
			String end;
			{
				String parts[] = url.split("/");
				end = parts[parts.length - 1];
				end = end.substring(0, end.lastIndexOf('.'));
			}
			String segments[] = end.split("_");
			if (segments.length < 4) continue;
			try {
				int numOfSprites = Integer.parseInt(segments[segments.length - 3]);
				int arrayNumber = Integer.parseInt(segments[segments.length - 2]);
				int frameNumber = Integer.parseInt(segments[segments.length - 1]);
				String name = segments[0];
				for (int d = 1; d < segments.length - 3; ++d) {
					name = name + "_" + segments[d];
				}
				result.add(new LoadableSheetImage(l, numOfSprites, arrayNumber, frameNumber, name));
			} catch (Exception e) {
				continue;
			}
		}
		return result;
	}

	private static HashMap<String, LoadableTexture> getLoadableTextures(ArrayList<URL> urls) throws IOException {
		HashMap<String, LoadableTexture> result = new HashMap<String, LoadableTexture>();
		ArrayList<LoadableSheetImage> imgs = getLoadableImages(urls);
		for (LoadableSheetImage l : imgs) {
			LoadableTexture t = result.get(l.texName);
			if (t == null) {
				t = new LoadableTexture(1);
				t.name = l.texName;
				t.addFile(l);
				result.put(l.texName, t);
			} else {
				t.addFile(l);
			}

		}
		return result;
	}

	private static class LoadableSheetImage implements Comparable<LoadableSheetImage> {

		URL url;
		int numSprites;
		int arrayIndex;
		int frameNumber;
		String texName;

		public LoadableSheetImage(URL url, int numSprites, int arrayIndex, int frameNumber, String name) {
			super();
			this.url = url;
			this.numSprites = numSprites;
			this.arrayIndex = arrayIndex;
			this.frameNumber = frameNumber;
			this.texName = name;
		}

		public InputStream getInputStream() throws IOException {
			return url.openStream();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(LoadableSheetImage o) {
			if (o.frameNumber < frameNumber) return 1;
			else if (o.frameNumber > frameNumber) return -1;
			if (o.arrayIndex < arrayIndex) return 1;
			else return -1;
		}

	}

	protected static class LoadableTexture {

		LoadableSheetImage[] files;
		int arraySize;
		int numFrames;
		String name;
		int size;

		public LoadableTexture(int initialSize) {
			files = new LoadableSheetImage[initialSize];
		}

		public void addFile(LoadableSheetImage file) {
			if (file.arrayIndex >= arraySize) {
				arraySize = file.arrayIndex + 1;
			}
			if (file.frameNumber >= numFrames) {
				numFrames = file.frameNumber + 1;
			}
			if (size >= files.length) files = Arrays.copyOf(files, files.length + 1);
			files[size] = file;
			++size;
		}

		public SheetTexture load() throws IOException {
			return loadWithModded(new ArrayList<SheetTextureAddition>());
		}

		/**
		 * @param extra
		 * @return The loaded texture
		 * @throws IOException
		 */
		public SheetTexture loadWithModded(List<SheetTextureAddition> extra) throws IOException {
			for (int d = 0; d < extra.size(); ++d) {
				SheetTextureAddition t = extra.get(d);
				addFile(new LoadableSheetImage(t.url, t.sprites, arraySize, 0, name));
			}
			Arrays.sort(files);
			int sprites = files[0].numSprites;
			int rootSprites = (int) (Math.sqrt(sprites) + 0.001D);
			TextureImpl texture = new TextureImpl();
			texture.numFrames = numFrames;
			texture.arraysize = arraySize;
			texture.frames = new int[numFrames];
			int finalArraySize = 0;
			for (int frame = 0; frame < numFrames; ++frame) {
				ByteBuffer texData = null;
				for (int ai = 0; ai < arraySize; ++ai) {
					int arraypos = frame * arraySize + ai;
					InputStream in = files[arraypos].getInputStream();
					BufferedImage b = ImageIO.read(in);
					sprites = files[arraypos].numSprites;
					finalArraySize += sprites;
					rootSprites = (int) (Math.sqrt(sprites) + 0.001D);
					texture.width = b.getWidth() / rootSprites;
					texture.height = b.getHeight() / rootSprites;
					if (texData == null) {
						texData = BufferUtils.createByteBuffer(b.getWidth() * b.getHeight() * arraySize * 4);
					}
					int[] pixels = new int[b.getWidth() * b.getHeight()];
					b.getRGB(0, 0, b.getWidth(), b.getHeight(), pixels, 0, b.getWidth());
					for (int sy = 0; sy < rootSprites; ++sy) {
						int ny = texture.height * sy;
						for (int sx = 0; sx < rootSprites; ++sx) {
							int nx = texture.width * sx;
							for (int y = ny; y < ny + texture.height; y++) {
								for (int x = nx; x < nx + texture.width; x++) {
									int pixel = pixels[y * b.getWidth() + x];
									if (((pixel >> 24) & 0xFF) < 2) {
										texData.put((byte) 255);
										texData.put((byte) 255);
										texData.put((byte) 255);
										texData.put((byte) 0);
									} else {
										texData.put((byte) ((pixel >> 16) & 0xFF)); // Red component
										texData.put((byte) ((pixel >> 8) & 0xFF)); // Green component
										texData.put((byte) (pixel & 0xFF)); // Blue component
										texData.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component. Only for RGBA
									}
								}
							}
						}
					}
					in.close();
					b.flush();
				}
				texData.flip();
				int texid = gl.genTextures();
				gl.bindTexture(GLTexture.TEX_2D_ARRAY, texid);
				gl.texImage3D(GLTexture.TEX_2D_ARRAY, 0, GLInternalFormat.RGBA8, texture.width, texture.height, finalArraySize, false, GLFormat.RGBA, GLType.UNSIGNED_BYTE, texData);
				texture.setMipmap(false);
				texture.setSmooth(false);

				gl.bindTexture(GLTexture.TEX_2D_ARRAY, 0);

				texture.frames[frame] = texid;
			}
			return texture;
		}

	}

	protected static class TextureImpl implements SheetTexture {

		int numFrames;
		int frames[];
		int arraysize;
		int width, height;
		boolean smooth = true, mipmap = true;

		GLTexture type = GLTexture.TEX_2D_ARRAY;

		/*
		 * (non-Javadoc)
		 * 
		 * @see vc4.api.graphics.texture.Texture#getTexture(int)
		 */
		@Override
		public int getTexture(int frame) {
			return frames[frame];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see vc4.api.graphics.texture.Texture#getTextureCurrentFrame()
		 */
		@Override
		public int getTexture() {
			return frames[getCurrentFrame()];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see vc4.api.graphics.texture.Texture#getCurrentFrame()
		 */
		@Override
		public int getCurrentFrame() {
			if (numFrames == 1) return 0;
			long time = (System.nanoTime() / 1000000) / 100;
			int frame = (int) (time % numFrames);
			return frame;
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
		 * @see vc4.api.graphics.texture.Texture#getFrameTime()
		 */
		@Override
		public int getFrameTime() {
			return 100;
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
		 * @see vc4.api.graphics.texture.Texture#getNumberOfFrames()
		 */
		@Override
		public int getNumberOfFrames() {
			return numFrames;
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
			for (int d = 0; d < numFrames; ++d) {
				gl.bindTexture(GLTexture.TEX_2D_ARRAY, frames[d]);
				GLTextureFilter f;
				GLTextureFilter mip = null;
				if (smooth) {
					f = GLTextureFilter.LINEAR;
				} else {
					f = GLTextureFilter.NEAREST;
				}
				if (mipmap) mip = f;
				gl.texParameterMinFilter(GLTexture.TEX_2D_ARRAY, f, mip);
				gl.texParameterMagFilter(GLTexture.TEX_2D_ARRAY, f);

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

	}

	// /* (non-Javadoc)
	// * @see game.vc3d.texture.TextureLoader#loadTexture(java.net.URL)
	// */
	// @Override
	// public SheetTexture loadTexture(URL url) throws IOException {
	// String urlDir = url.toString();
	// urlDir = urlDir.substring(0, urlDir.lastIndexOf("/"));
	// HashMap<String, LoadableTexture> t = registeredTextures.get(urlDir);
	// if(t == null){
	// registeredTextures.put(urlDir, getLoadableTextures(getImageUrlsElsewhere(new URL(urlDir))));
	// t = registeredTextures.get(urlDir);
	// }
	// String name = url.toString();
	// name = name.substring(name.lastIndexOf("/") + 1);
	// LoadableTexture l = t.get(name);
	//
	//
	// return l.load();
	// }

	/**
	 * @param string
	 * @param extra
	 * @return The loaded texture
	 * @throws IOException
	 */
	public SheetTexture loadTextureWithModded(String path, List<SheetTextureAddition> extra) throws IOException {
		path = path.replace(".", "/");
		if (registeredTextures == null) {
			registeredTextures = getLoadableTextures(getImageURLs());
		}
		String name = path.substring(path.lastIndexOf("/") + 1);
		LoadableTexture l = registeredTextures.get(name);

		return l.loadWithModded(extra);
	}

}
