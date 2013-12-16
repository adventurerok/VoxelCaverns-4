package vc4.launcher;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JTextArea;

import vc4.launcher.gui.frame.LauncherGui;
import vc4.launcher.repo.*;
import vc4.launcher.repo.Package;
import vc4.launcher.repo.Runnable;
import vc4.launcher.task.TaskSystem;
import vc4.launcher.util.*;

public class Launcher {

	private static Launcher singleton;
	
	private LauncherGui gui;
	private Repo vc4;
	private TaskSystem tasks = new TaskSystem();
	private JTextArea consoleOut = new JTextArea();
	
	private ArrayList<Repo> repos = new ArrayList<>();
	
	public static Launcher getSingleton() {
		return singleton;
	}
	
	public Repo getVc4() {
		return vc4;
	}
	
	public JTextArea getConsoleOut() {
		return consoleOut;
	}
	
	
	public Launcher() {
		PrintStream con = new PrintStream(new TextAreaOutputStream(consoleOut));
		System.setOut(con);
		System.setErr(con);
		System.out.println("Redirected system output to console tab");
		singleton = this;
		tasks.start();
		Repo rec = new Repo();
		try {
			rec.loadInfo(new URL("http://repo.voxelcaverns.org.uk"));
			vc4 = rec;
			vc4.setCanDisable(false);
			repos.add(vc4);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try{
			loadRepos();
		} catch(Exception e){
			e.printStackTrace();
		}
		for(Repo r : repos){
			r.autoUpdate();
		}
		System.out.println("Creating GUI...");
		gui = new LauncherGui();
		gui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				tasks.setStop(true);
				try {
					saveRepos();
				} catch (IOException e1) {
				}
			}
		});
		gui.setVisible(true);
		System.out.println("GUI created successfully");
	}
	
	public LauncherGui getGui() {
		return gui;
	}
	
	public ArrayList<Repo> getRepos() {
		return repos;
	}


	public Package getClientPackage() {
		return vc4.getPackage("VoxelCaverns");
	}

	
	public YamlMap getRepoMap(){
		YamlMap map = new YamlMap();
		ArrayList<String> reps = new ArrayList<>();
		for(Repo r : repos){
			if(r == vc4) continue;
			reps.add(r.getRepoRoot());
		}
		map.setList("repos", reps);
		return map;
	}
	
	public void saveRepos() throws IOException{
		String path = DirectoryLocator.getPath() + "/launcher/repos.yml"; 
		YamlMap map = getRepoMap();
		map.save(new FileOutputStream(path));
	}
	
	public void loadRepos() throws IOException{
		String path = DirectoryLocator.getPath() + "/launcher/repos.yml"; 
		YamlMap map = new YamlMap(new FileInputStream(path));
		loadRepoMap(map);
	}

	public void loadRepoMap(YamlMap map) throws IOException {
		Object[] lis = map.getList("repos");
		for(Object o : lis){
			String s = o.toString();
			Repo repo = new Repo();
			repo.loadInfo(new URL(s));
			repos.add(repo);
		}
		
	}
	
	public void launch(){
		launchRunnable(getClientPackage().getRuns()[0]);
	}

	public void launchRunnable(Runnable r) {
		System.out.println("Launching runnable: " + r.getName());
		String run = DirectoryLocator.getPath() + r.getPath();
		String separator = System.getProperty("file.separator");
	    String path = System.getProperty("java.home") + separator + "bin" + separator + "javaw";
	    String[] launchOptions = new String[] { path, "-Xmx" + r.getXmx(), "-Xms" + r.getXms(), "-cp", run, r.getLaunch(), "heapset" };
	    ProcessBuilder processBuilder = new ProcessBuilder(launchOptions);
	    
	    StringBuilder launchString = new StringBuilder();
	    for(String s : launchOptions) launchString.append(s).append(' ');
		try {
			System.out.println(launchString);
			Process proc = processBuilder.start();
			new StreamGobbler(proc.getInputStream(), r.getName()).start();
		} catch (Throwable e) {
			System.out.println("Failed to launch rubbable: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public TaskSystem getTasks() {
		return tasks;
	}
	
}
