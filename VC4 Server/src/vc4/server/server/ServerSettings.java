package vc4.server.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import vc4.api.logging.Logger;
import vc4.api.util.DirectoryLocator;
import vc4.api.util.SuidUtils;
import vc4.api.yaml.YamlMap;

public class ServerSettings {

	public static byte[] usid; //Unique Server ID
	
	public static byte[] getUsid() {
		return usid;
	}
	
	public static void load(){
		String path = DirectoryLocator.getPath() + "/server/server.yml";
		File file = new File(path);
		if(!file.exists()) {
			createDefault();
			return;
		}
		try {
			YamlMap map = new YamlMap(new FileInputStream(file));
			if(map.getBaseMap() == null){ 
				createDefault();
				return;
			} else load(map);
		} catch (FileNotFoundException e) {
			Logger.getLogger(ServerSettings.class).warning("Exception", e);
		}
		Logger.getLogger("VC4").info("Server USID: " + SuidUtils.getSuidHex(usid));
	}
	
	public static void save(){
		String path = DirectoryLocator.getPath() + "/server/server.yml";
		File file = new File(path);
		if(!file.exists()) file.getParentFile().mkdirs();
		try(OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(file))){
			getSaveMap().dump(w);
		} catch (IOException e) {
			Logger.getLogger(ServerSettings.class).warning("Exception", e);
		}
	}

	private static void load(YamlMap map) {
		if(map.hasKey("usid")) usid = SuidUtils.parseSuid(map.getString("usid"));
		else usid = SuidUtils.generateRandomSuid();
	}
	
	private static YamlMap getSaveMap(){
		YamlMap save = new YamlMap();
		save.setString("usid", SuidUtils.getSuidHex(usid));
		return save;
	}

	private static void createDefault() {
		Logger.getLogger("VC4").warning("Failed to load /server/server.yml, creating new settings");
		usid = SuidUtils.generateRandomSuid();
		save();
		Logger.getLogger("VC4").info("Server USID: " + SuidUtils.getSuidHex(usid));
	}
	
}
