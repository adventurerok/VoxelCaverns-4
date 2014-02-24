package vc4.server.task;

import java.util.concurrent.ConcurrentLinkedDeque;

import vc4.api.logging.Logger;
import vc4.api.packet.Packet;
import vc4.server.user.ServerUser;

public class TaskManager extends Thread {

	private static ConcurrentLinkedDeque<Task> tasks = new ConcurrentLinkedDeque<>();
	private static boolean running = true;
	
	public TaskManager() {
		setDaemon(true);
	}
	
	@Override
	public void run() {
		long start = 0;
		long time = 0;
		long nt = 0;
		Task t = null;
		try{
			while(running){
				start = System.nanoTime();
				time = 0;
				while((t = tasks.poll()) != null){
					t.run();
					nt = System.nanoTime();
					time += nt - start;
					start = nt;
					if(time > 10000000) break;
				}
				if(time > 10000000) time = 10000000;
				Thread.sleep(13 - (time / 1000000));
			}
		} catch (Exception e){
			Logger.getLogger("VC4").fatal("Task thread failed: ", e);
		}
	}
	
	public static void sendPackets(ServerUser user, Packet...packets){
		scheduleTask(new TaskSendPackets(user, packets));
	}
	
	public static void scheduleTask(Task t){
		tasks.add(t);
	}
	
}
