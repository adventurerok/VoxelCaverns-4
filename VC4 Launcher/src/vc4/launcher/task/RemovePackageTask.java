package vc4.launcher.task;

import java.io.File;
import java.io.IOException;

import vc4.launcher.repo.*;
import vc4.launcher.repo.Install.InstallType;
import vc4.launcher.repo.Package;
import vc4.launcher.util.DirectoryLocator;

public class RemovePackageTask implements Task {

	Package pack;
	int install;

	public RemovePackageTask(Package pack, int install) {
		super();
		this.pack = pack;
		this.install = install;
	}

	@Override
	public void run(Progress progress) {
		if (pack.getVersion() == null) {
			progress.setPercent(100);
			progress.setDelete(true);
			return;
		}
		Install instal = pack.getInstalls()[install];
		if (!instal.canInstall(pack.getVersion())) {
			progress.setPercent(100);
			progress.setDelete(true);
			if (install == pack.getInstalls().length - 1) pack.setVersion(null);
			;
		}
		String installPath = DirectoryLocator.getPath() + instal.getEnd();
		if (pack.isDisabled()) installPath = installPath + ".disabled";
		progress.setPercent(33);
		if (instal.getType() == InstallType.UNZIP) {
			progress.setText("Deleting: " + instal.getEnd());
			installPath = installPath + "/";
			progress.setPercent(66);
			File files = new File(installPath);
			if (files.exists()) deleteDirectory(files);
			progress.setPercent(100);
		} else if (instal.getType() == InstallType.COPY) {
			progress.setText("Deleting: " + instal.getEnd());
			progress.setPercent(50);
			File files = new File(installPath);
			if (files.exists()) files.delete();
			progress.setPercent(100);
		}
		if (install == pack.getInstalls().length - 1) pack.setVersion(null);
		progress.setDelete(true);
		try {
			pack.save();
		} catch (IOException e) {
			System.out.println("Failed to save package after delete");
		}
	}

	@Override
	public boolean canRun() {
		return true;

	}

	public boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectory(new File(dir, children[i]));
				if (!success) { return dir.delete(); }
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

}
