package vc4.launcher.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import vc4.launcher.repo.Package;
import vc4.launcher.repo.Version;
import vc4.launcher.util.DirectoryLocator;

public class InstallVersionTask implements Task {
	
	Package pack;
	Version version;
	
	

	public InstallVersionTask(Package pack, Version version) {
		super();
		this.pack = pack;
		this.version = version;
	}

	@Override
	public void run(Progress progress) {
		try {
			if(pack.getVersion() != null && version.getIntVersion() == pack.getVersion().getIntVersion()) return;
			String installPath = DirectoryLocator.getPath() + "/" + pack.getFolder() + "/" + pack.getInstall();
			if(pack.isBackup() && pack.getVersion() != null && !pack.getFileType().equals("zip")){
				progress.setText("Backing up version: " + pack.getVersion().getVersion());
				progress.setPercent(0);
				String bakPath = DirectoryLocator.getPath() + "/" + pack.getFolder() + "/" + pack.getVersion().getPath();
				if(bakPath.contains("/")) bakPath = bakPath.substring(bakPath.lastIndexOf("/"), bakPath.length());
				Files.move(new File(installPath).toPath(), new File(bakPath).toPath());
			}
			progress.setPercent(33);
			String downloadPath = pack.getPackageRoot() + version.getPath();
			if (pack.getFileType().equals("zip")) {
				progress.setText("Downloading version");
				installPath = DirectoryLocator.getPath() + "/" + pack.getFolder() + "/";
				String tempPath = DirectoryLocator.getPath() + "/temp/" + pack.getInstall();
				URL download = new URL(downloadPath);
				ReadableByteChannel rbc = Channels.newChannel(download.openStream());
				FileOutputStream out = new FileOutputStream(tempPath);
				out.getChannel().transferFrom(rbc, 0, 1 << 28);
				out.close();
				progress.setPercent(55);
				byte[] buffer = new byte[1024];
				File folder = new File(installPath);
				if (!folder.exists()) {
					folder.mkdir();
				}
				progress.setPercent(77);
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
				progress.setPercent(100);
			} else {
				String checkPath = version.getPath();
				if(checkPath.contains("/")) checkPath = checkPath.substring(checkPath.lastIndexOf("/"), checkPath.length());
				if(new File(checkPath).exists()){
					progress.setText("Installing backup version");
					progress.setPercent(66);
					Files.copy(new File(checkPath).toPath(), new File(installPath).toPath());
				} else {
					progress.setText("Downloading version");
					progress.setPercent(50);
					URL download = new URL(downloadPath);
					ReadableByteChannel rbc = Channels.newChannel(download.openStream());
					FileOutputStream out = new FileOutputStream(installPath);
					out.getChannel().transferFrom(rbc, 0, 1 << 28);
					out.close();
				}
				progress.setPercent(100);
			}
			pack.setVersion(version);
			progress.setDelete(true);
			try {
				pack.save();
			} catch (IOException e) {
				System.out.println("Failed to save package after update");
			}
		} catch (IOException e) {
			progress.setDelete(true);
		}
	}

	@Override
	public boolean canRun() {
		return true;

	}

}
