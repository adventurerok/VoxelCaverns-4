package vc4.api.profile;

import java.util.HashMap;

public class Profiler {

	private static HashMap<Thread, Profile> threads = new HashMap<>();
	
	public static void start(String task){
		getProfile().start(task);
	}
	
	public static void stop(){
		getProfile().stop();
	}
	
	public static void stopStart(String task){
		getProfile().stopStart(task);
	}
	
	public static void clear(){
		getProfile().clear();
	}
	
	private static Profile getProfile(){
		Profile p = threads.get(Thread.currentThread());
		if(p != null) return p;
		p = new Profile();
		threads.put(Thread.currentThread(), p);
		return p;
	}
}
