/**
 * 
 */
package vc4.client.graphics;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import vc4.api.graphics.Graphics;
import vc4.api.graphics.texture.AnimatedTexture;
import vc4.api.graphics.texture.AnimatedTextureLoader;
import vc4.api.logging.Logger;
import vc4.impl.plugin.PluginManager;

/**
 * @author paul
 * 
 */
public class ClientAnimatedTextureLoader implements AnimatedTextureLoader {


	private static ArrayList<URL> getImageURLs(String tex) {
		ArrayList<URL> result = new ArrayList<>();
		for (URL url : PluginManager.getResourceURLs()) {
			try {
				URL mod = new URL(url.toString() + "/animatedtexture/" + tex + "/");
				result.addAll(getImageUrlsElsewhere(mod));
			} catch (IOException e) {
				Logger.getLogger(ClientAnimatedTextureLoader.class).warning("Exception occured", e);
			}
		}
		return result;
	}

	private static ArrayList<URL> getImageUrlsElsewhere(URL baseURL) throws IOException {
		ArrayList<URL> names = new ArrayList<>();

		if (baseURL.getProtocol().equals("jar")) {
			String jarFileName;
			Enumeration<JarEntry> jarEntries;
			String entryName;

			// build jar file name, then loop through zipped entries
			jarFileName = URLDecoder.decode(baseURL.getFile(), "UTF-8");
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
						names.add(new URL(baseURL + entryName + ".png"));
					}
				}
			}

			// loop through files in classpath
		} else {
			File folder = new File(URLDecoder.decode(baseURL.getFile(), "UTF-8"));
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
	public ClientAnimatedTextureLoader() {
		Graphics.setAnimatedLoader(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vc4.api.graphics.texture.TextureLoader#loadTexture(java.lang.String)
	 */
	@Override
	public AnimatedTexture loadTexture(String path) throws IOException {
		path = path.replace(".", "/");
		ArrayList<LoadableAnimatedImage> imgs = getLoadableImages(getImageURLs(path));
		return new ClientAnimatedTexture().load(imgs);
	}

	private static ArrayList<LoadableAnimatedImage> getLoadableImages(ArrayList<URL> urls) throws IOException {
		ArrayList<LoadableAnimatedImage> result = new ArrayList<LoadableAnimatedImage>();
		for (URL l : urls) {
			String url = l.toString();
			String end;
			{
				String parts[] = url.split("/");
				end = parts[parts.length - 1];
				end = end.substring(0, end.lastIndexOf('.'));
			}
			try {
				result.add(new LoadableAnimatedImage(l, end));
			} catch (Exception e) {
				continue;
			}
		}
		return result;
	}

	// /* (non-Javadoc)
	// * @see game.vc3d.texture.TextureLoader#loadTexture(java.net.URL)
	// */
	// @Override
	// public AnimatedTexture loadTexture(URL url) throws IOException {
	// String urlDir = url.toString();
	// urlDir = urlDir.substring(0, urlDir.lastIndexOf("/"));
	// ArrayList<LoadableAnimatedImage> imgs = getLoadableImages(getImageUrlsElsewhere(new URL(urlDir)));
	// return new ImplAnimatedTexture().load(imgs);
	// }

}
