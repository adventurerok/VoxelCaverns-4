package vc4.client.gui;

import java.util.ArrayList;
import java.util.LinkedList;

import vc4.api.biome.Biome;
import vc4.api.client.Client;
import vc4.api.entity.EntityPlayer;
import vc4.api.font.FontRenderer;
import vc4.api.font.RenderedText;
import vc4.api.gui.Component;
import vc4.api.vector.Vector3l;
import vc4.api.world.World;
import vc4.impl.world.*;

public class ScreenDebug extends Component {

	private static long ONE_SECOND = 1000000L * 1000L; // 1 second is 1000ms which is 1000000ns

	LinkedList<Long> frames = new LinkedList<>(); // List of frames within 1 second
	int[] fpsSamples = new int[50];
	int fpsSampleNo = 0;
	long[] memorySamples = new long[50];
	int memorySampleNo = 0;

	FontRenderer font = FontRenderer.createFontRenderer("unispaced_14", 14);

	int debugLine;

	ArrayList<RenderedText> oldDebugLines = new ArrayList<>(20);
	ArrayList<RenderedText> newDebugLines = new ArrayList<>(20);

	public int calcFPS() {
		long time = System.nanoTime(); // Current time in nano seconds
		frames.add(time); // Add this frame to the list
		while (true) {
			long f = frames.getFirst(); // Look at the first element in frames
			if (time - f > ONE_SECOND) { // If it was more than 1 second ago
				frames.remove(); // Remove it from the list of frames
			} else break;
			/*
			 * If it was within 1 second we know that all other frames in the list are also within 1 second
			 */
		}
		fpsSamples[fpsSampleNo] = frames.size();
		if (++fpsSampleNo >= fpsSamples.length) fpsSampleNo = 0;
		int tot = 0;
		for (int d = 0; d < fpsSamples.length; ++d)
			tot += fpsSamples[d];
		return tot / fpsSamples.length;
	}

	public long calcMemory() {
		long curr = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		memorySamples[memorySampleNo] = curr;
		if (++memorySampleNo >= memorySamples.length) memorySampleNo = 0;
		long tot = 0;
		for (int d = 0; d < memorySamples.length; ++d)
			tot += memorySamples[d];
		return tot / memorySamples.length;
	}

	@Override
	public void draw() {
		oldDebugLines.clear();
		ArrayList<RenderedText> temp = oldDebugLines;
		oldDebugLines = newDebugLines;
		newDebugLines = temp;

		debugLine = 0;
		EntityPlayer player = Client.getGame().getPlayer();
		World world = player.getWorld();
		Vector3l pPos = player.position.toVector3l();
		ImplChunk chunk = (ImplChunk) world.getChunk(pPos.x >> 5, pPos.y >> 5, pPos.z >> 5);
		int bsn = (int) ((((pPos.x & 31) >> 4) * 2 + ((pPos.y & 31) >> 4)) * 2 + ((pPos.z & 31) >> 4));
		BlockStore blockstore = null;
		if (chunk != null) blockstore = chunk.getBlockStore(bsn);
		Vector3l blockPos = player.position.toVector3l();
		String[] dInfo = world.getDebugInfo();
		for (int d = 0; d < dInfo.length; ++d) {
			renderDebugLine(dInfo[d]);
		}
		long maxMemory = Runtime.getRuntime().maxMemory();
		long totalMemory = Runtime.getRuntime().totalMemory();
		// long freeMemory = Runtime.getRuntime().freeMemory();
		long availableMemory = calcMemory();
		renderDebugLine("Used memory: " + availableMemory * 100L / maxMemory + "% (" + availableMemory / 1024L / 1024L + "MB) of " + maxMemory / 1024L / 1024L + "MB");
		renderDebugLine("Allocated memory: " + totalMemory * 100L / maxMemory + "% (" + totalMemory / 1024L / 1024L + "MB)");
		renderDebugLine("FPS: " + calcFPS());
		renderDebugLine("X: " + blockPos.x);
		renderDebugLine("Y: " + blockPos.y);
		renderDebugLine("Z: " + blockPos.z);
		renderDebugLine("SF: " + player.getSimpleFacing() + ", AF: " + player.getAdvancedFacing());
		Biome biome = world.getBiome(blockPos.x, blockPos.z);
		renderDebugLine("Biome: " + biome.getName());
		renderDebugLine("Time: " + world.getTimeText());
		if (chunk != null) renderDebugLine("Chunk: p=" + chunk.isPopulated() + ", l=" + chunk.isLit());
		if (blockstore != null) renderDebugLine("Store: cs=" + blockstore.compileState);
	}

	public void renderDebugLine(String line) {
		RenderedText old = null;
		if(oldDebugLines.size() > debugLine && (old = oldDebugLines.get(debugLine)) != null && line.equals(old.getText())){
			newDebugLines.add(old);
			old.draw();
		} else {
			if(old != null) old.release();
			RenderedText text = font.renderString(10, 10 + 16 * debugLine, line);
			newDebugLines.add(text);
			text.draw();
		}
		++debugLine;
	}
}
