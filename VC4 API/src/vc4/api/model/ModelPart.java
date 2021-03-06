package vc4.api.model;

import java.util.ArrayList;

import vc4.api.graphics.Graphics;
import vc4.api.graphics.OpenGL;
import vc4.api.render.DataRenderer;
import vc4.api.vector.*;

public class ModelPart {

	private static OpenGL gl;

	private Vector3f rotation = new Vector3f();
	private Vector3d offset = new Vector3d();
	private DataRenderer render = new DataRenderer();
	private ArrayList<ModelPart> children = new ArrayList<>();

	public void addChild(ModelPart child) {
		children.add(child);
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public void load(String[] lines) {
		String[] parts;
		for (int d = 0; d < lines.length; ++d) {
			if (lines[d].startsWith("#")) continue;
			parts = lines[d].split(" ");
			if (parts[0].equals("offset")) offset = vect3d(parts);
			else if (parts[0].equals("c")) render.color(vect4f(parts, 1));
			else if (parts[0].equals("t")) render.tex(vect4f(parts, 0));
			else if (parts[0].equals("v")) {
				render.addVertex(vect3d(parts));
			} else if (parts[0].equals("qi")) {
				render.useQuadInputMode(Boolean.parseBoolean(parts[1]));
			}
		}
		render.compile();
	}

	public void draw() {
		if (gl == null) gl = Graphics.getOpenGL();
		gl.pushMatrix();
		gl.translate(offset.x, offset.y, offset.z);
		gl.rotate(rotation.y, 0, 1, 0);
		gl.rotate(rotation.z, 0, 0, 1);
		gl.rotate(rotation.x, 1, 0, 0);
		render.render();
		for (int d = 0; d < children.size(); ++d)
			children.get(d).draw();
		gl.popMatrix();
	}

	private Vector3d vect3d(String[] parts) {
		return new Vector3d(parseDouble(parts[1]), parseDouble(parts[2]), parseDouble(parts[3]));
	}

	private Vector4f vect4f(String[] parts, float def) {
		if (parts.length > 4) return new Vector4f(parseFloat(parts[1]), parseFloat(parts[2]), parseFloat(parts[3]), parseFloat(parts[4]));
		return new Vector4f(parseFloat(parts[1]), parseFloat(parts[2]), parseFloat(parts[3]), def);
	}

	private float parseFloat(String s) {
		String[] parts = s.split("/");
		double top = Double.parseDouble(parts[0]);
		double bot = parts.length > 1 ? Double.parseDouble(parts[1]) : 1d;
		return (float) (top / bot);
	}

	private double parseDouble(String s) {
		String[] parts = s.split("/");
		double top = Double.parseDouble(parts[0]);
		double bot = parts.length > 1 ? Double.parseDouble(parts[1]) : 1d;
		return top / bot;
	}
}
