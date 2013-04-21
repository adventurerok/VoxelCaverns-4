package vc4.launcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import vc4.launcher.enumeration.UpdateStreamType;

public class Version implements Comparable<Version>{

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	int intVersion;
	String version;
	String path;
	String type = "zip";
	Date date;
	boolean bugged = false;
	UpdateStreamType updateStream;
	
	public String getVersion() {
		return version;
	}

	
	
	public Version(int intVersion, String version, String path, Date date) {
		super();
		this.intVersion = intVersion;
		this.version = version;
		this.path = path;
		this.date = date;
	}



	public int getIntVersion() {
		return intVersion;
	}



	public static Version load(YamlMap map, String name) {
		String version = name;
		int i = map.getInt("int");
		String path = map.getString("file");
		Date d;
		try {
			d = dateFormat.parse(map.getString("release"));
		} catch (ParseException e) {
			throw new RuntimeException("Could not parse date");
		}
		Version v = new Version(i, version, path, d);
		if(map.hasKey("type")) v.type = map.getString("type");
		if(map.hasKey("bugged")) v.bugged = map.getBoolean("bugged");
		return v;
	}
	
	@Override
	public String toString() {
		return (updateStream == UpdateStreamType.RECCOMENDED ? "REC" : updateStream) + ": " + version + " (" + dateFormat.format(date) + ")";
	}



	@Override
	public int compareTo(Version o) {
		if(o.intVersion > intVersion) return 1;
		if(intVersion > o.intVersion) return -1;
		return 0;
	}
	
	public String getPath() {
		return path;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getType() {
		return type;
	}
}
