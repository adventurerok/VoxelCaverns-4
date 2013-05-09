package vc4.api.util;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileSorter {

	public static ArrayList<URL> getFiles(URL base) {
		ArrayList<URL> names = new ArrayList<URL>();
		String folderStr = base.toString();
		URL folderURL;
		try {
			folderURL = new URL(folderStr.substring(0, folderStr.lastIndexOf("/") + 1));
		} catch (MalformedURLException e1) {
			return names;
		}
		String fileName = base.toString();
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		try {
			if (folderURL.getProtocol().equals("jar")) {
				String jarFileName;
				Enumeration<JarEntry> jarEntries;
				String entryName;

				// build jar file name, then loop through zipped entries
				jarFileName = URLDecoder.decode(folderURL.getFile(), "UTF-8");
				String packName = jarFileName.substring(jarFileName.indexOf("!") + 2);
				jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));

				try (JarFile jf = new JarFile(jarFileName)) {
					jarEntries = jf.entries();
					while (jarEntries.hasMoreElements()) {
						JarEntry je = jarEntries.nextElement();

						entryName = je.getName();
						// System.out.println(entryName);
						if (entryName.startsWith(packName) && entryName.length() > 4) {
							entryName = entryName.substring(packName.length(), entryName.length());
							String noNum = removeDigitsAtEnd(entryName.substring(0, entryName.lastIndexOf(".")));
							if(noNum.equals(fileName)) names.add(new URL(folderURL + entryName));
						}
					}
				}

				// loop through files in classpath
			} else {
				File folder = new File(URLDecoder.decode(folderURL.getFile(), "UTF-8"));
				File[] contenuti = folder.listFiles();
				if (contenuti == null) return names;
				String entryName;
				for (File actual : contenuti) {
					entryName = actual.getName();
					if (!entryName.endsWith(".ogg") && !entryName.endsWith(".wav")) continue;
					String noNum = removeDigitsAtEnd(entryName.substring(0, entryName.lastIndexOf(".")));
					if(noNum.equals(fileName)) names.add(actual.toURI().toURL());
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		return names;
	}

	// public static URL[] getFiles(URL base) {
	// ArrayList<URL> list = new ArrayList<URL>();
	// File file;
	// try {
	// file = new File(base.toURI());
	// } catch (URISyntaxException e) {
	// e.printStackTrace();
	// return null;
	// }
	// File dir = file.getParentFile();
	// File[] files = dir.listFiles(createFilter(file.getName()));
	// if (files == null) return null;
	// for (int dofor = 0; dofor < files.length; ++dofor) {
	// try {
	// list.add(files[dofor].toURI().toURL());
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// }
	// }
	// return list.toArray(new URL[list.size()]);
	// }

	public static FilenameFilter createFilter(final String baseName) {
		return new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				name = removeDigitsAtEnd(removeFileType(name));
				if (name.equals(baseName)) return true;
				else return false;
			}

		};
	}

	public static String removeDigitsAtEnd(String s) {
		while (true) {
			if (Character.isDigit(s.charAt(s.length() - 1))) {
				s = s.substring(0, s.length() - 1);
			} else break;
		}
		return s;
	}

	public static String removeFileType(String s) {
		int i = s.lastIndexOf(".");
		if (i < 0) return s;
		return s.substring(0, i);
	}
}
