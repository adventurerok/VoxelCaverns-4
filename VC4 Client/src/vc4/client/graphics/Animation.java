package vc4.client.graphics;

import java.nio.ByteBuffer;

import vc4.api.graphics.*;

public class Animation {

	OpenGL gl;

	int xOffset, yOffset, zOffset, width, height, depth;
	int length; // Length in 10ths of second
	ByteBuffer[] parts;
	Frame[] frames;
	int currentPart = 0;

	public Animation() {
		if (gl == null) gl = Graphics.getOpenGL();
	}

	public void loadFrames(String s) {
		String[] sects = s.split(",");
		frames = new Frame[sects.length];
		String[] ns;
		int part, ticks;
		for (int d = 0; d < sects.length; ++d) {
			ns = sects[d].split("\\*");
			part = Integer.parseInt(ns[0]);
			ticks = 1;
			if (ns.length > 1) ticks = Integer.parseInt(ns[1]);
			frames[d] = new Frame(ticks, part);
			length += ticks;
		}
	}

	public void update(int tickTime, GLTexture texture) {
		tickTime = tickTime % length;
		int part = currentPart;
		for (int f = 0; f < frames.length; ++f) {
			tickTime -= frames[f].ticks;
			if (tickTime < 0) {
				part = frames[f].part;
				break;
			}
		}
		if (part == currentPart) return;
		gl.texSubImage3D(texture, 0, xOffset, yOffset, zOffset, width, height, depth, GLFormat.RGBA, GLType.UNSIGNED_BYTE, parts[part]);
		currentPart = part;
	}

	private static class Frame {
		int ticks;
		int part;

		public Frame(int ticks, int part) {
			super();
			this.ticks = ticks;
			this.part = part;
		}

	}
}
