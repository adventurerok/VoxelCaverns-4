package vc4.api.util;

import java.io.File;
import java.io.FilenameFilter;
import java.net.*;
import java.util.ArrayList;

public class FileSorter {

	public static URL[] getFiles(URL base) {
		ArrayList<URL> list = new ArrayList<URL>();
		File file;
		try {
			file = new File(base.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
		File dir = file.getParentFile();
		File[] files = dir.listFiles(createFilter(file.getName()));
		if (files == null) return null;
		for (int dofor = 0; dofor < files.length; ++dofor) {
			try {
				list.add(files[dofor].toURI().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return list.toArray(new URL[list.size()]);
	}

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
