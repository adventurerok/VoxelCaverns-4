package vc4.launcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import vc4.launcher.gui.frame.LauncherGui;

public class Launcher {

	private static Launcher singleton;
	
	private LauncherGui gui;
	private Repo vc4;
	
	private ArrayList<Repo> repos = new ArrayList<>();
	
	public static Launcher getSingleton() {
		return singleton;
	}
	
	public Repo getVc4() {
		return vc4;
	}
	
	public Launcher() {
		singleton = this;
		Repo rec = new Repo();
		try {
			rec.loadInfo(new URL("https://raw.github.com/adventurerok/VoxelCaverns-4/master/VC4%20Downloads"));
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
		gui = new LauncherGui();
		gui.setVisible(true);
	}
	
	public LauncherGui getGui() {
		return gui;
	}
	
	public ArrayList<Repo> getRepos() {
		return repos;
	}

	public Package getApiPackage() {
		return vc4.getPackage("VC4-API");
	}

	public Package getImplPackage() {
		return vc4.getPackage("VC4-Impl");
	}

	public Package getClientPackage() {
		return vc4.getPackage("VC4-Client");
	}

	public Package getServerPackage() {
		return vc4.getPackage("VC4-Server");
	}

	public Package getVanillaPackage() {
		return vc4.getPackage("VC4-Vanilla");
	}

	public Package getEditorPackage() {
		return vc4.getPackage("VC4-Editor");
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
	
}
