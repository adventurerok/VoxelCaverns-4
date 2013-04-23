package vc4.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.yaml.snakeyaml.Yaml;

import vc4.launcher.enumeration.UpdateStreamType;

public class Package {

	private Version version;

	private UpdateStream recommended, beta, alpha, dev;
	private UpdateStreamType type = UpdateStreamType.RECCOMENDED;
	private boolean manual = false;
	private boolean backup = false;
	private boolean auto = true;
	private String name, author, desc, folder, install, fileType = "file", data;

	private String packageRoot;

	/**
	 * Null if not installed
	 * 
	 * @return The installed version
	 */
	public Version getVersion() {
		return version;
	}
	
	public boolean isBackup() {
		return backup;
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

	
	public boolean isAuto() {
		return auto;
	}
	public void setManual(boolean manual) {
		this.manual = manual;
		try {
			save();
		} catch (IOException e) {
			System.out.println("Failed to save package after update");
		}
	}
	
	public String getFileType() {
		return fileType;
	}

	public void install(Version version) throws IOException {
		if(this.version != null && version.intVersion == this.version.intVersion) return;
		String installPath = DirectoryLocator.getPath() + "/" + folder + "/" + install;
		if(backup && this.version != null && !type.equals("zip")){
			String bakPath = this.version.path;
			if(bakPath.contains("/")) bakPath = bakPath.substring(bakPath.lastIndexOf("/"), bakPath.length());
			Files.move(new File(installPath).toPath(), new File(bakPath).toPath());
		}
		String downloadPath = packageRoot + version.getPath();
		if (fileType.equals("zip")) {
			installPath = DirectoryLocator.getPath() + "/" + folder + "/";
			String tempPath = DirectoryLocator.getPath() + "/temp/" + install;
			URL download = new URL(downloadPath);
			ReadableByteChannel rbc = Channels.newChannel(download.openStream());
			FileOutputStream out = new FileOutputStream(tempPath);
			out.getChannel().transferFrom(rbc, 0, 1 << 28);
			out.close();
			
			byte[] buffer = new byte[1024];
			File folder = new File(installPath);
			if (!folder.exists()) {
				folder.mkdir();
			}

			ZipInputStream zis = new ZipInputStream(new FileInputStream(tempPath));
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(installPath + fileName);
				if (newFile.isDirectory()) {
					newFile.mkdirs();
				} else {
					FileOutputStream fos = new FileOutputStream(newFile);

					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}

					fos.close();
					ze = zis.getNextEntry();
				}
			}
			zis.closeEntry();
			zis.close();
		} else {
			String checkPath = version.path;
			if(checkPath.contains("/")) checkPath = checkPath.substring(checkPath.lastIndexOf("/"), checkPath.length());
			if(new File(checkPath).exists()){
				Files.copy(new File(checkPath).toPath(), new File(installPath).toPath());
			} else {
				URL download = new URL(downloadPath);
				ReadableByteChannel rbc = Channels.newChannel(download.openStream());
				FileOutputStream out = new FileOutputStream(installPath);
				out.getChannel().transferFrom(rbc, 0, 1 << 28);
				out.close();
			}
		}
		this.version = version;
		try {
			save();
		} catch (IOException e) {
			System.out.println("Failed to save package after update");
		}
	}

	public UpdateStream getBeta() {
		return beta;
	}

	public Version[] getVisibleVersions() {
		ArrayList<Version> result = new ArrayList<>();
		if (type == UpdateStreamType.DEV) result.addAll(dev.getVersions());
		if (type == UpdateStreamType.DEV || type == UpdateStreamType.ALPHA) result.addAll(alpha.getVersions());
		if (type != UpdateStreamType.RECCOMENDED) result.addAll(beta.getVersions());
		result.addAll(recommended.getVersions());
		if(!result.contains(getLatest())) result.add(getLatest());
		Collections.sort(result);
		return result.toArray(new Version[result.size()]);
	}
	
	public Version getVersion(int ver){
		Version v = recommended.getVersion(ver);
		if(v != null) return v;
		v = beta.getVersion(ver);
		if(v != null) return v;
		v = alpha.getVersion(ver);
		if(v != null) return v;
		v = dev.getVersion(ver);
		if(v != null) return v;
		return null;
	}

	public String getName() {
		return name;
	}
	
	public String getData() {
		return data;
	}

	public String getAuthor() {
		return author;
	}

	public void setType(UpdateStreamType type) {
		this.type = type;
		try {
			save();
		} catch (IOException e) {
			System.out.println("Failed to save package after update");
		}
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

	public boolean isDownloaded() {
		return version != null;
	}

	public UpdateStream getDev() {
		return dev;
	}

	public boolean isPlugin() {
		return folder.equals("plugins");
	}
	
	public void setAuto(boolean auto) {
		this.auto = auto;
		try {
			save();
		} catch (IOException e) {
			System.out.println("Failed to save package after update");
		}
	}
	
	public void setBackup(boolean backup) {
		this.backup = backup;
		try {
			save();
		} catch (IOException e) {
			System.out.println("Failed to save package after update");
		}
	}

	public void setPlugin(boolean plugin) {
		folder = plugin ? "plugins" : "bin";
	}

	public UpdateStreamType getType() {
		return type;
	}

	public Version getLatest() {
		Version latestRec = recommended.getLatest();
		if (type == UpdateStreamType.RECCOMENDED && latestRec != null) return latestRec;
		Version latestBeta = beta.getLatest();
		if (((type == UpdateStreamType.RECCOMENDED && latestRec == null)) && latestBeta != null) return latestBeta;
		Version latestAlpha = alpha.getLatest();
		if (((type == UpdateStreamType.RECCOMENDED && latestRec == null) || (type == UpdateStreamType.BETA && latestBeta == null)) && latestAlpha != null) return latestAlpha;
		Version latestDev = dev.getLatest();
		Version latest = null;
		if (type == UpdateStreamType.DEV && latestDev != null) {
			latest = latestDev;
		}
		if ((type == UpdateStreamType.ALPHA || type == UpdateStreamType.DEV) && latestAlpha != null) {
			if (latest == null || latestAlpha.intVersion > latest.intVersion) latest = latestAlpha;
		}
		if (type != UpdateStreamType.RECCOMENDED && latestBeta != null) {
			if (latest == null || latestBeta.intVersion > latest.intVersion) latest = latestBeta;
		}
		if (latestRec != null) {
			if (latest == null || latestRec.intVersion > latest.intVersion) latest = latestRec;
		}
		if(latest == null){
			if(latestBeta != null) latest = latestBeta;
			else if(latestAlpha != null) latest = latestAlpha;
			else if(latestDev != null) latest = latestDev;
		}
		return latest;
	}
	
	public YamlMap getSaveData(){
		YamlMap data = new YamlMap();
		data.setInt("version", version == null ? -1 : version.intVersion);
		data.setBoolean("manual", manual);
		data.setBoolean("auto", auto);
		data.setBoolean("backup", backup);
		data.setString("stream", type.toString());
		return data;
	}
	
	public void save() throws IOException{
		String path = DirectoryLocator.getPath() + "/launcher/" + data;
		Yaml yaml = ThreadYaml.getYamlForThread();
		YamlMap map = getSaveData();
		Writer r = new FileWriter(path);
		yaml.dump(map.getBaseMap(), r);
		r.close();
	}
	
	public void load() throws FileNotFoundException{
		String path = DirectoryLocator.getPath() + "/launcher/" + data;
		YamlMap map = new YamlMap(new FileInputStream(path));
		loadSaveData(map);
	}
	
	public void loadSaveData(YamlMap data){
		int ver = data.getInt("version");
		version = getVersion(ver);
		manual = data.getBoolean("manual");
		auto = data.getBoolean("auto");
		backup = data.getBoolean("backup");
		type = UpdateStreamType.valueOf(data.getString("stream"));
	}

	public void load(YamlMap map) {
		YamlMap info = map.getSubMap("info");
		name = info.getString("name");
		author = info.getString("author");
		desc = info.getString("desc");
		folder = info.getString("folder");
		install = info.getString("install");
		data = info.getString("data");
		if(map.hasKey("type")) fileType = map.getString("type");
		YamlMap latest = map.getSubMap("latest");
		String latestRec = latest.getString("recommended");
		String latestBeta = latest.getString("beta");
		String latestAlpha = latest.getString("alpha");
		String latestDev = latest.getString("dev");
		recommended = new UpdateStream();
		if(map.hasKey("recommended"))recommended.load(map.getSubMap("recommended"), latestRec, UpdateStreamType.RECCOMENDED);
		beta = new UpdateStream();
		if(map.hasKey("beta"))beta.load(map.getSubMap("beta"), latestBeta, UpdateStreamType.BETA);
		alpha = new UpdateStream();
		if(map.hasKey("alpha"))alpha.load(map.getSubMap("alpha"), latestAlpha, UpdateStreamType.ALPHA);
		dev = new UpdateStream();
		if(map.hasKey("dev"))dev.load(map.getSubMap("dev"), latestDev, UpdateStreamType.DEV);
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
	
	public void refresh() throws IOException{
		URL url = new URL(packageRoot + "package.yml");
		YamlMap map = new YamlMap(url.openStream());
		load(map);
	}

	public String getPackageRoot() {
		return packageRoot;
	}

	public UpdateStream getCurrentStream() {
		switch (type) {
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
