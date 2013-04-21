package vc4.launcher;

import java.io.IOException;
import java.net.URL;

import vc4.launcher.enumeration.UpdateStreamType;

public class Package {

	private Version version;
	
	private UpdateStream recommended, beta, alpha, dev;
	private UpdateStreamType type;
	private boolean plugin = true;
	
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
	
	public UpdateStream getBeta() {
		return beta;
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
		if(((type == UpdateStreamType.RECCOMENDED && latestRec == null) || type == UpdateStreamType.BETA) && latestBeta != null) return latestBeta;
		Version latestAlpha = alpha.getLatest();
		if(((type == UpdateStreamType.RECCOMENDED && latestRec == null) || (type == UpdateStreamType.BETA && latestBeta == null) || type == UpdateStreamType.ALPHA) && latestAlpha != null) return latestAlpha;
		Version latestDev = dev.getLatest();
		Version latest = null;
		if(type == UpdateStreamType.DEV && latestDev != null){
			latest = latestDev;
		} else if((type == UpdateStreamType.ALPHA || type == UpdateStreamType.DEV) && latestAlpha != null){
			if(latest == null || latestAlpha.intVersion > latest.intVersion) latest = latestAlpha;
		} else if(type != UpdateStreamType.RECCOMENDED && latestBeta != null){
			if(latest == null || latestBeta.intVersion > latest.intVersion) latest = latestBeta;
		} else if(latestRec != null){
			if(latest == null || latestRec.intVersion > latest.intVersion) latest = latestRec;
		}
		return latest;
	}
	
	public void load(YamlMap map){
		YamlMap latest = map.getSubMap("latest");
		String latestRec = latest.getString("recommended");
		String latestBeta = latest.getString("beta");
		String latestAlpha = latest.getString("alpha");
		String latestDev = latest.getString("dev");
		recommended = new UpdateStream();
		recommended.load(map.getSubMap("recommended"), latestRec);
		beta = new UpdateStream();
		beta.load(map.getSubMap("beta"), latestBeta);
		alpha = new UpdateStream();
		alpha.load(map.getSubMap("alpha"), latestAlpha);
		dev = new UpdateStream();
		dev.load(map.getSubMap("dev"), latestDev);
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
}
