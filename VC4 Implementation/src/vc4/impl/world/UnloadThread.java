package vc4.impl.world;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import vc4.api.logging.Logger;
import vc4.api.world.Chunk;

public class UnloadThread extends Thread {

	public static ImplWorld world;
	public static ConcurrentLinkedQueue<ImplChunk> toSave = new ConcurrentLinkedQueue<>();

	@Override
	public void run() {
		Chunk c;
		while ((c = toSave.poll()) != null) {
			try {
				world.getSaveFormat().writeChunk(c);
			} catch (IOException e) {
				Logger.getLogger(UnloadThread.class).warning("Failed to save chunk", e);
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
		}
	}
}
