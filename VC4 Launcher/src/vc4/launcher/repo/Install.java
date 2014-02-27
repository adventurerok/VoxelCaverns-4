package vc4.launcher.repo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import vc4.launcher.util.OS;

public class Install {

	public static enum InstallType {
		COPY, UNZIP;

		public static InstallType parse(String s) {
			switch (s) {
				case "->":
					return UNZIP;
				case "<>":
					return COPY;
			}
			return null;
		}
	}

	private static HashMap<String, Constructor<? extends InstallCheck>> checkers = new HashMap<>();

	static {
		try {
			Constructor<? extends InstallCheck> c = OsCheck.class.getConstructor(String.class);
			checkers.put("os", c);
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("Failed to add InstallCheck");
		}
	}

	String start;
	String end;
	InstallType type;
	ArrayList<InstallCheck> checks = new ArrayList<>();

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}

	public InstallType getType() {
		return type;
	}

	public Install(String parse) {
		parse = parse.trim();
		if (parse.contains(";")) {
			parseChecks(parse.substring(0, parse.indexOf(';')));
			parse = parse.substring(parse.indexOf(';') + 1).trim();
		}
		String[] parts = parse.split(" ");
		if (parts.length != 3) throw new RuntimeException("Must have 3 parts");
		start = parts[0];
		end = parts[2];
		type = InstallType.parse(parts[1]);

	}

	public boolean canInstall(Version v) {
		for (int d = 0; d < checks.size(); ++d) {
			if (!checks.get(d).canInstall(v)) return false;
		}
		return true;
	}

	private void parseChecks(String parse) {
		String[] parts = parse.split(",");
		for (int d = 0; d < parts.length; ++d) {
			String sel[] = parts[d].split(":");
			Constructor<? extends InstallCheck> cons = checkers.get(sel[0].trim().toLowerCase());
			try {
				checks.add(cons.newInstance(sel[1]));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public static abstract class InstallCheck {

		public InstallCheck(String data) {

		}

		public abstract boolean canInstall(Version v);
	}

	public static class OsCheck extends InstallCheck {

		OS os;

		public OsCheck(String data) {
			super(data);
			os = OS.valueOf(data.toUpperCase());
		}

		@Override
		public boolean canInstall(Version v) {
			return os == OS.getOs();
		}

	}

}
