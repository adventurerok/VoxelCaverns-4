package vc4.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import vc4.launcher.enumeration.UpdateStreamType;

public class Package {

	private Version version;

	private UpdateStream recommended, beta, alpha, dev;
	private UpdateStreamType type = UpdateStreamType.RECCOMENDED;
	private boolean manual = false;
	private String name, author, desc, folder;

	private String packageRoot;

	/**
	 * Null if not installed
	 * 
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

	public void install(Version version) throws IOException {
		String name = version.getPath();
		if (name.contains("/")) name = name.substring(name.lastIndexOf("/"), name.length());
		String installPath = DirectoryLocator.getPath() + "/" + folder + "/" + name;
		String downloadPath = packageRoot + version.getPath();
		if (type.equals("zip")) {
			installPath = DirectoryLocator.getPath() + "/" + folder + "/";
			String tempPath = DirectoryLocator.getPath() + "/temp/" + name;
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
			URL download = new URL(downloadPath);
			ReadableByteChannel rbc = Channels.newChannel(download.openStream());
			FileOutputStream out = new FileOutputStream(installPath);
			out.getChannel().transferFrom(rbc, 0, 1 << 28);
			out.close();
		}
		this.version = version;
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
		return latest;
	}

	public void load(YamlMap map) {
		YamlMap info = map.getSubMap("info");
		name = info.getString("name");
		author = info.getString("author");
		desc = info.getString("desc");
		folder = info.getString("folder");
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

	public void loadInfo(URL url) throws IOException {
		String s = url.toString();
		if (!s.endsWith("/")) s = s + "/";
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
