package vc4.launcher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import vc4.launcher.enumeration.UpdateStreamType;

public class Package {

	private Version version;
	
	private UpdateStream recommended, beta, alpha, dev;
	private UpdateStreamType type = UpdateStreamType.RECCOMENDED;
	private boolean plugin = true;
	private boolean manual = false;
	private String name, author, desc;
	
	private String packageRoot;	
	/**
	 * Null if not installed
	 * @return The installed version
	 */
	public Version getVersion() {
		return version;
	}
	
	
	public UpdateStream getRecommended() {
		return recommended;
	}
	
	public UpdateStream getAlpha() {
		return alpha;
	}
	
	public boolean isManual() {
		return manual;
	}
	
	public void setManual(boolean manual) {
		this.manual = manual;
	}
	
	public UpdateStream getBeta() {
		return beta;
	}
	
	public Version[] getVisibleVersions(){
		ArrayList<Version> result = new ArrayList<>();
		if(type == UpdateStreamType.DEV) result.addAll(dev.getVersions());
		if(type == UpdateStreamType.DEV || type == UpdateStreamType.ALPHA) result.addAll(alpha.getVersions());
		if(type != UpdateStreamType.RECCOMENDED) result.addAll(beta.getVersions());
		result.addAll(recommended.getVersions());
		Collections.sort(result);
		return result.toArray(new Version[result.size()]);
	}
	
	public String getName() {
		return name;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setType(UpdateStreamType type) {
		this.type = type;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getInfo(){
		return name + ":\n" + desc + "\n" + "Created by " + author + "\n\n" + getInstalledText();
	}
	
	private String getInstalledText() {
		if(isDownloaded()){
			return "Installed Version: " + version.toString();
		} else return "This package is not installed";
	}

	public boolean isDownloaded(){
		return version != null;
	}
	
	public UpdateStream getDev() {
		return dev;
	}
	
	public boolean isPlugin() {
		return plugin;
	}
	
	public void setPlugin(boolean plugin) {
		this.plugin = plugin;
	}
	
	public UpdateStreamType getType() {
		return type;
	}
	
	public Version getLatest(){
		Version latestRec = recommended.getLatest();
		if(type == UpdateStreamType.RECCOMENDED && latestRec != null) return latestRec;
		Version latestBeta = beta.getLatest();
		if(((type == UpdateStreamType.RECCOMENDED && latestRec == null)) && latestBeta != null) return latestBeta;
		Version latestAlpha = alpha.getLatest();
		if(((type == UpdateStreamType.RECCOMENDED && latestRec == null) || (type == UpdateStreamType.BETA && latestBeta == null)) && latestAlpha != null) return latestAlpha;
		Version latestDev = dev.getLatest();
		Version latest = null;
		if(type == UpdateStreamType.DEV && latestDev != null){
			latest = latestDev;
		}if((type == UpdateStreamType.ALPHA || type == UpdateStreamType.DEV) && latestAlpha != null){
			if(latest == null || latestAlpha.intVersion > latest.intVersion) latest = latestAlpha;
		}if(type != UpdateStreamType.RECCOMENDED && latestBeta != null){
			if(latest == null || latestBeta.intVersion > latest.intVersion) latest = latestBeta;
		}if(latestRec != null){
			if(latest == null || latestRec.intVersion > latest.intVersion) latest = latestRec;
		}
		return latest;
	}
	
	public void load(YamlMap map){
		YamlMap info = map.getSubMap("info");
		name = info.getString("name");
		author = info.getString("author");
		desc = info.getString("desc");
		plugin = info.getBoolean("plugin");
		YamlMap latest = map.getSubMap("latest");
		String latestRec = latest.getString("recommended");
		String latestBeta = latest.getString("beta");
		String latestAlpha = latest.getString("alpha");
		String latestDev = latest.getString("dev");
		recommended = new UpdateStream();
		recommended.load(map.getSubMap("recommended"), latestRec, UpdateStreamType.RECCOMENDED);
		beta = new UpdateStream();
		beta.load(map.getSubMap("beta"), latestBeta, UpdateStreamType.BETA);
		alpha = new UpdateStream();
		alpha.load(map.getSubMap("alpha"), latestAlpha, UpdateStreamType.ALPHA);
		dev = new UpdateStream();
		dev.load(map.getSubMap("dev"), latestDev, UpdateStreamType.DEV);
	}
	
	public void loadInfo(URL url) throws IOException{
		String s = url.toString();
		if(!s.endsWith("/")) s = s + "/";
		packageRoot = s;
		s = s + "updates.yml";
		url = new URL(s);
		YamlMap map = new YamlMap(url.openStream());
		load(map);
	}
	
	public String getPackageRoot() {
		return packageRoot;
	}

	public UpdateStream getCurrentStream() {
		switch(type){
			case RECCOMENDED:
				return recommended;
			case BETA:
				return beta;
			case ALPHA:
				return alpha;
			case DEV:
				return dev;
		}
		return dev;
	}
}
