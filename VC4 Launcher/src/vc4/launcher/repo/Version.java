package vc4.launcher.repo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import vc4.launcher.util.YamlMap;

public class Version implements Comparable<Version>{

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	int intVersion;
	String version;
	String path;
	Date date;
	boolean bugged = false;
	int stream;
	
	public String getVersion() {
		return version;
	}

	
	
	public Version(int intVersion, String version, String path, Date date, int stream) {
		super();
		this.intVersion = intVersion;
		this.version = version;
		this.path = path;
		this.date = date;
		this.stream = stream;
	}



	public int getIntVersion() {
		return intVersion;
	}



	public static Version load(YamlMap map, String name) {
		String version = map.getString("name");
		int i = Integer.parseInt(name);
		String path = map.getString("dir");
		Date d;
		try {
			d = dateFormat.parse(map.getString("release"));
		} catch (ParseException e) {
			throw new RuntimeException("Could not parse date");
		}
		Version v = new Version(i, version, path, d, map.getInt("stream"));
		if(map.hasKey("bugged")) v.bugged = map.getBoolean("bugged");
		return v;
	}
	
	@Override
	public String toString() {
		return version + " (" + dateFormat.format(date) + ")";
	}



	@Override
	public int compareTo(Version o) {
		if(o.intVersion > intVersion) return -1;
		if(intVersion > o.intVersion) return 1;
		return 0;
	}
	
	public String getPath() {
		return path;
	}
	
	public Date getDate() {
		return date;
	}
	
}
