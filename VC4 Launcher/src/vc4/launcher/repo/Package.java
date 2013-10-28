package vc4.launcher.repo;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

import vc4.launcher.Launcher;
import vc4.launcher.gui.settings.PackageSettingsPanel;
import vc4.launcher.task.InstallVersionTask;
import vc4.launcher.task.UpdateGuiTask;
import vc4.launcher.util.*;

public class Package {
	
	private Version version;
	private int stream = 1;
	private String author;
	private String desc;
	private String name;
	private String data;
	
	private Version[] versions;
	private Install[] installs;

	private boolean manual;
	private boolean auto;
	private HashMap<String, Integer> updateStreams = new HashMap<>();
	private HashMap<Integer, String> updateStreamsRev = new HashMap<>();
	private PackageSettingsPanel panel;

	private String packageRoot;
	
	
	public HashMap<String, Integer> getUpdateStreams() {
		return updateStreams;
	}
	
	public String getUpdateStreamName(int id){
		return updateStreamsRev.get(id);
	}
	
	public int getUpdateStreamId(String name){
		return updateStreams.get(name);
	}
	
	public void autoUpdate() {
		if(!auto) return;
		Version latest = getLatest();
		if(version != null && latest.intVersion <= version.intVersion) return;
		try {
			install(latest);
		} catch (IOException e) {
			System.out.println("Failed to install latest");
		}
		
	}
	
	public boolean isManual() {
		return manual;
	}
	
	public Package setManual(boolean manual) {
		this.manual = manual;
		try {
			save();
		} catch (IOException e) {
			System.out.println("Failed to save package after update");
		}
		return this;
	}
	
	public PackageSettingsPanel getPanel() {
		return panel;
	}
	
	public void setPanel(PackageSettingsPanel panel) {
		this.panel = panel;
	}

	
	public String getAuthor() {
		return author;
	}
	
	
	public int getStream() {
		return stream;
	}
	
	
	public String getDesc() {
		return desc;
	}
	
	public String getInfo() {
		return name + ":\n" + desc + "\n" + "Created by " + author + "\n\n" + getInstalledText();
	}
	

	private String getInstalledText() {
		if (isDownloaded()) {
			return "Installed Version: " + version.toString();
		} else return "This package is not installed";
	}
	
	public Install[] getInstalls() {
		return installs;
	}

	public Version getLatest() {
		for(int d = versions.length - 1; d > -1; --d){
			if(versions[d].stream <= stream) return versions[d];
		}
		return null;
	}

	
	public String getName() {
		return name;
	}

	public String getPackageRoot() {
		return packageRoot;
	}
	

	public YamlMap getSaveData(){
		YamlMap data = new YamlMap();
		data.setInt("version", version == null ? -1 : version.intVersion);
		data.setBoolean("auto", auto);
		data.setInt("stream", stream);
		data.setBoolean("manual", manual);
		return data;
	}


	/**
	 * Null if not installed
	 * 
	 * @return The installed version
	 */
	public Version getVersion() {
		return version;
	}
	
	

	public Version getVersion(int ver){
		return versions[ver - 1];
	}

	public Version[] getVisibleVersions() {
		ArrayList<Version> result = new ArrayList<>();
		for(int d = versions.length - 1; d >= 0; --d){
			if(versions[d].stream <= stream) result.add(versions[d]);
		}
		Collections.sort(result);
		return result.toArray(new Version[result.size()]);
	}

	public void install(Version version) throws IOException {
		for(int d = 0; d < installs.length; ++d) Launcher.getSingleton().getTasks().addTask(new InstallVersionTask(this, version, d));
		final Package pak = this;
		Runnable run = new Runnable() {
			
			@Override
			public void run() {
				if(pak.panel != null){
					pak.panel.packageUpdate();
				}
			}
		};
		Launcher.getSingleton().getTasks().addTask(new UpdateGuiTask(run));
	}

	public boolean isAuto() {
		return auto;
	}

	
	public boolean isDownloaded() {
		return version != null;
	}
	

	public void load() throws FileNotFoundException{
		String path = DirectoryLocator.getPath() + "/launcher/" + data + ".yml";
		YamlMap map = new YamlMap(new FileInputStream(path));
		loadSaveData(map);
	}
	
	public Package setStream(int stream) {
		this.stream = stream;
		return this;
	}

	@SuppressWarnings("unchecked")
	public void load(YamlMap map) {
		YamlMap info = map.getSubMap("package");
		name = info.getString("name");
		author = info.getString("author");
		desc = info.getString("desc");
		data = info.getString("data");
		YamlMap streamData = map.getSubMap("streams");
		for(Entry<Object, Object> en : streamData.getBaseMap().entrySet()){
			if(en.getValue() instanceof Number){
				updateStreams.put(en.getKey().toString(), ((Number)en.getValue()).intValue());
				updateStreamsRev.put(((Number)en.getValue()).intValue(), en.getKey().toString());
			}
		}
		Object[] ins = map.getList("install");
		installs = new Install[ins.length];
		for(int d = 0; d < ins.length; ++d) installs[d] = new Install(ins[d].toString());
		YamlMap versionData = map.getSubMap("versions");
		ArrayList<Version> vsns = new ArrayList<>();
		for(Entry<Object, Object>  obj: versionData.getBaseMap().entrySet()){
			if(!(obj.getValue() instanceof Map<?, ?>)) continue;
			YamlMap ver = new YamlMap((Map<Object, Object>) obj.getValue());
			Version vsn = Version.load(ver, obj.getKey().toString());
			vsns.add(vsn);
		}
		Collections.sort(vsns);
		versions = vsns.toArray(new Version[vsns.size()]);
		try {
			load();
		} catch (FileNotFoundException e) {
		}
	}
	
	public void loadInfo(URL url) throws IOException {
		String s = url.toString();
		if (!s.endsWith("/")) s = s + "/";
		packageRoot = s;
		s = s + "package.yml";
		url = new URL(s);
		YamlMap map = new YamlMap(url.openStream());
		load(map);
	}
	
	public void loadSaveData(YamlMap data){
		int ver = data.getInt("version");
		if(ver != -1) version = getVersion(ver);
		auto = data.getBoolean("auto");
		stream = data.getInt("stream");
		manual = data.getBoolean("manual");
	}
	
	public void refresh() throws IOException{
		URL url = new URL(packageRoot + "package.yml");
		YamlMap map = new YamlMap(url.openStream());
		load(map);
	}
	
	public void save() throws IOException{
		String path = DirectoryLocator.getPath() + "/launcher/" + data + ".yml";
		Yaml yaml = ThreadYaml.getYamlForThread();
		YamlMap map = getSaveData();
		Writer r = new FileWriter(path);
		yaml.dump(map.getBaseMap(), r);
		r.close();
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
		try {
			save();
		} catch (IOException e) {
			System.out.println("Failed to save package after update");
		}
	}
	

	public void setVersion(Version version) {
		this.version = version;
		try {
			save();
		} catch (IOException e) {
			System.out.println("Failed to save package after update");
		}
	}
}
