package vc4.launcher.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import vc4.launcher.repo.*;
import vc4.launcher.repo.Install.InstallType;
import vc4.launcher.repo.Package;
import vc4.launcher.util.DirectoryLocator;

public class DisablePackageTask implements Task {

	Package pack;
	int install;

	public DisablePackageTask(Package pack, int install) {
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
		if(!instal.canInstall(pack.getVersion())){
			progress.setPercent(100);
			progress.setDelete(true);
			if(install == pack.getInstalls().length - 1) pack.setDisabled(true);
		}
		String installPath = DirectoryLocator.getPath() + instal.getEnd();
		progress.setPercent(33);
		if (instal.getType() == InstallType.UNZIP) {
			progress.setText("Disabling: " + instal.getEnd());
			progress.setPercent(66);
			File files = new File(installPath);
			if(files.exists()){
				try {
					Files.move(files.toPath(), new File(installPath + ".disabled").toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			progress.setPercent(100);
		} else if(instal.getType() == InstallType.COPY){
			progress.setText("Disabling: " + instal.getEnd());
			progress.setPercent(50);
			File files = new File(installPath);
			if(files.exists()){
				try {
					Files.move(files.toPath(), new File(installPath + ".disabled").toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			progress.setPercent(100);
		}
		if(install == pack.getInstalls().length - 1) pack.setDisabled(true);
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
	


}
