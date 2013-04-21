package vc4.launcher;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class Version {

	int intVersion;
	String version;
	String path;
	String type = "zip";
	Date date;
	boolean bugged = false;
	
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
			d = DateFormat.getInstance().parse(map.getString("release"));
		} catch (ParseException e) {
			throw new RuntimeException("Could not parse date");
		}
		Version v = new Version(i, version, path, d);
		if(map.hasKey("type")) v.type = map.getString("type");
		if(map.hasKey("bugged")) v.bugged = map.getBoolean("bugged");
		return v;
	}
}
