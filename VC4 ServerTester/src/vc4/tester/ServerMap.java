package vc4.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map.Entry;

import vc4.api.logging.Logger;
import vc4.api.server.SUID;
import vc4.api.util.DirectoryLocator;
import vc4.api.util.SuidUtils;

public class ServerMap {

	private static HashMap<SUID, SUID> servers = new HashMap<>();

	public static SUID getSuid(byte[] usid) {
		return servers.get(new SUID(usid));
	}

	public static void addSuid(byte[] server, byte[] suid) {
		servers.put(new SUID(server), new SUID(suid));
	}
	
	public static boolean hasServer(byte[] suid){
		return servers.containsKey(new SUID(suid));
	}

	public static void saveSuids() {
		Logger.getLogger("TST").info("Saving server:suid map");
		String path = DirectoryLocator.getPath() + "/settings/suids";
		File file = new File(path);
		if (!file.exists())
			file.getParentFile().mkdirs();
		try (PrintStream out = new PrintStream(file)) {
			for (Entry<SUID, SUID> e : servers.entrySet()) {
				out.println(SuidUtils.getSuidHex(e.getKey().getSuid())
						+ SuidUtils.getSuidHex(e.getValue().getSuid()));
			}
		} catch (FileNotFoundException e) {
			Logger.getLogger(ServerMap.class).warning("Exception", e);
		}
	}
	
	public static void loadSuids(){
		Logger.getLogger("TST").info("Loading server:suid map");
		String path = DirectoryLocator.getPath() + "/settings/suids";
		File file = new File(path);
		if(!file.exists()) return;
		try (BufferedReader in = new BufferedReader(new FileReader(file))){
			String line;
			while((line = in.readLine()) != null){
				line = line.trim();
				if(line.length() != 64) continue;
				SUID server = new SUID(SuidUtils.parseSuid(line.substring(0, 32)));
				SUID client = new SUID(SuidUtils.parseSuid(line.substring(32, 64)));
				servers.put(server, client);
			}
		} catch (IOException e) {
			Logger.getLogger(ServerMap.class).warning("Exception", e);
		}
	}
}
