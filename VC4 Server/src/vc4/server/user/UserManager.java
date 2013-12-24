package vc4.server.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import vc4.api.logging.Logger;
import vc4.api.server.SUID;
import vc4.api.util.DirectoryLocator;
import vc4.api.util.SuidUtils;
import vc4.api.yaml.YamlMap;
import vc4.impl.permissions.ImplPermissionGroup;

public class UserManager {
	
	private static Random rand = new Random();

	private static HashMap<String, UserGroup> groups = new HashMap<>();
	private static HashMap<SUID, UserInfo> userInfo = new HashMap<>();
	private static HashMap<String, UserInfo> userNames = new HashMap<>();
	private static ArrayList<String> chatNames = new ArrayList<>();
	private static boolean loaded = false;
	
	public static UserGroup getGroup(String name){
		return groups.get(name);
	}
	
	public static void load(){
		loadUsers();
		//loadNames();
	}
	
	public static UserInfo getUserInfo(SUID user){
		return userInfo.get(user);
	}
	
	public static UserInfo getUserInfo(String name){
		return userNames.get(name);
	}
	
	public static void setUserName(String name, UserInfo info){
		if(info == null){
			userNames.remove(name);
			return;
		}
		userNames.put(name, info);
	}
	
	public static String generateChatName(String chars, int startNum){
		int tries = 0;
		while(true){
			String test = chars + (startNum == 0 ? "" : startNum);
			if(!chatNames.contains(test)) return test;
			++startNum;
			++tries;
			if((tries % 5 == 0)) startNum += rand.nextInt(250);
		}
	}
	
	public static UserInfo getOrCreateUserInfo(SUID user){
		UserInfo u = userInfo.get(user);
		if(u == null) u = new UserInfo(generateChatName("generic", 1));
		chatNames.add(u.getChatName());
		userInfo.put(user, u);
		return u;
	}
	
	public static void removeChatName(String name){
		chatNames.remove(name);
	}
	
	public static void addChatName(String name){
		chatNames.add(name);
	}
	
//	private static void loadNames() {
//		String path = DirectoryLocator.getPath() + "/server/names.txt";
//		File file = new File(path);
//		if(!file.exists()){
//			file.getParentFile().mkdirs();
//			return;
//		}
//		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
//			String line;
//			while((line = reader.readLine()) != null){
//				line = line.trim();
//				chatNames.add(line);
//			}
//		} catch (IOException e) {
//			Logger.getLogger(UserManager.class).warning("Exception", e);
//		}
//	}

	private static void loadUsers(){
		String path = DirectoryLocator.getPath() + "/server/users.yml";
		File file = new File(path);
		if(!file.exists()){
			file.getParentFile().mkdirs();
			createDefault();
			return;
		}
		try {
			YamlMap map = new YamlMap(new FileInputStream(file));
			loadUsers(map);
			loaded = true;
		} catch (FileNotFoundException e) {
			Logger.getLogger(UserManager.class).warning("Exception", e);
		}
	}

	private static void createDefault() {
		Logger.getLogger("VC4").info("No /server/users.yml file found, creating");
		groups.put("default", new UserGroup("default", "", new ImplPermissionGroup()));
		loaded = true;
		
	}
	
	public static void saveUsers(){
		if(!loaded) return;
		YamlMap map = new YamlMap();
		map.setSubMap("groups", getGroupMap());
		map.setSubMap("users", getUserMap());
		String path = DirectoryLocator.getPath() + "/server/users.yml";
		File file = new File(path);
		if(!file.exists()) file.getParentFile().mkdirs();
		try(OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(file))){
			map.dump(w);
		} catch (IOException e) {
			Logger.getLogger(UserManager.class).warning("Exception", e);
		}
	}
	
	private static YamlMap getUserMap(){
		YamlMap res = new YamlMap();
		for(Entry<SUID, UserInfo> e : userInfo.entrySet()){
			res.setSubMap(SuidUtils.getSuidHex(e.getKey().getSuid()), e.getValue().toYaml());
		}
		return res;
	}
	
	private static YamlMap getGroupMap(){
		YamlMap res = new YamlMap();
		for(Entry<String, UserGroup> e : groups.entrySet()){
			res.setSubMap(e.getKey(), e.getValue().toYaml());
		}
		return res;
	}

	private static void loadUsers(YamlMap map) {
		if(map.hasKey("groups")){
			YamlMap groupMap = map.getSubMap("groups");
			for( Entry<Object, Object> e : groupMap.getBaseMap().entrySet()){
				if(!(e.getValue() instanceof Map)) continue;
				String name = e.getKey().toString();
				Logger.getLogger("VC4").fine("Found group: " + name);
				groups.put(name.trim(), new UserGroup(name, groupMap.getSubMap(name)));
			}
		}
		if(!groups.containsKey("default")){
			Logger.getLogger("VC4").warning("No \"default\" group settings found");
			groups.put("default", new UserGroup("default", "", new ImplPermissionGroup()));
		}
		if(map.hasKey("users")){
			YamlMap userMap = map.getSubMap("users");
			for( Entry<Object, Object> e : userMap.getBaseMap().entrySet()){
				if(!(e.getValue() instanceof Map)) continue;
				String name = e.getKey().toString();
				UserInfo info = new UserInfo(userMap.getSubMap(name));
				userInfo.put(new SUID(SuidUtils.parseSuid(name.trim())), info);
				userNames.put(info.getChatName(), info);
				chatNames.add(name);
			}
		}
	}
}
