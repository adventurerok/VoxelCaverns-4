package vc4.launcher.task;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import vc4.launcher.repo.*;
import vc4.launcher.repo.Install.InstallType;
import vc4.launcher.repo.Package;
import vc4.launcher.util.DirectoryLocator;

public class InstallVersionTask implements Task {

	Package pack;
	Version version;
	int install;

	public InstallVersionTask(Package pack, Version version, int install) {
		super();
		this.pack = pack;
		this.version = version;
		this.install = install;
	}

	@Override
	public void run(Progress progress) {
		try {
			if (pack.getVersion() != null && version.getIntVersion() == pack.getVersion().getIntVersion()) {
				progress.setPercent(100);
				progress.setDelete(true);
				if (install == pack.getInstalls().length - 1) pack.setVersion(version);
				return;
			}
			Install instal = pack.getInstalls()[install];
			if (!instal.canInstall(version)) {
				progress.setPercent(100);
				progress.setDelete(true);
				if (install == pack.getInstalls().length - 1) pack.setVersion(version);
				return;
			}
			// System.out.println("Installing package: " + pack.getName() + ", version: " + version.getVersion());
			String installPath = DirectoryLocator.getPath() + instal.getEnd();
			if (pack.isDisabled()) installPath = installPath + ".disabled";
			progress.setPercent(33);
			String downloadPath = pack.getPackageRoot() + version.getPath() + instal.getStart();
			if (instal.getType() == InstallType.UNZIP) {
				new File(DirectoryLocator.getPath() + "/temp/").mkdirs();
				progress.setText("Downloading: " + instal.getEnd());
				installPath = installPath + "/";
				String tempPath = DirectoryLocator.getPath() + "/temp/" + instal.getStart();
				URL download = new URL(downloadPath);
				ReadableByteChannel rbc = Channels.newChannel(download.openStream());
				FileOutputStream out = new FileOutputStream(tempPath);
				out.getChannel().transferFrom(rbc, 0, 1 << 28);
				out.close();
				progress.setPercent(55);
				File folder = new File(installPath);
				if (!folder.exists()) {
					folder.mkdir();
				}
				progress.setText("Extracting zip");
				progress.setPercent(77);
				byte[] buffer = new byte[1024];
				ZipInputStream zis = new ZipInputStream(new FileInputStream(tempPath));
				ZipEntry ze = zis.getNextEntry();
				while (ze != null) {
					String fileName = ze.getName();
					File newFile = new File(installPath + fileName);
					if (fileName.endsWith("/")) {
						newFile.mkdirs();
						ze = zis.getNextEntry();
					} else {
						newFile.getParentFile().mkdirs();
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
				progress.setPercent(90);
				new File(tempPath).delete();
				progress.setPercent(100);
			} else if (instal.getType() == InstallType.COPY) {
				progress.setText("Downloading: " + instal.getEnd());
				progress.setPercent(50);
				URL download = new URL(downloadPath);
				ReadableByteChannel rbc = Channels.newChannel(download.openStream());
				new File(installPath).getParentFile().mkdirs();
				FileOutputStream out = new FileOutputStream(installPath);
				out.getChannel().transferFrom(rbc, 0, 1 << 28);
				out.close();

				progress.setPercent(100);
			}
			if (install == pack.getInstalls().length - 1) {
				pack.setVersion(version);
				System.out.println("Installed package: " + pack.getName());
			}
			progress.setDelete(true);
			try {
				pack.save();
			} catch (IOException e) {
				System.out.println("Failed to save package after update");
			}
		} catch (IOException e) {
			System.out.println("Failed to install package: " + pack.getName());
			e.printStackTrace();
			progress.setDelete(true);
		}
	}

	@Override
	public boolean canRun() {
		return true;

	}

}
