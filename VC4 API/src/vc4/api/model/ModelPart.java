package vc4.api.model;

import java.util.ArrayList;

import vc4.api.graphics.OpenGL;
import vc4.api.render.DataRenderer;
import vc4.api.vector.*;

public class ModelPart {

	private static OpenGL gl;
	
	private Vector3f rotation = new Vector3f();
	private Vector3d offset = new Vector3d();
	private DataRenderer render = new DataRenderer();
	private ArrayList<ModelPart> children;
	
	public void addChild(ModelPart child){
		children.add(child);
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public void load(String[] lines){
		String[] parts;
		for(int d = 0; d < lines.length; ++d){
			parts = lines[d].split(" ");
			if(parts[0].equals("offset")) offset = vect3d(parts);
			else if(parts[0].equals("c")) render.color(vect4f(parts, 1));
			else if(parts[0].equals("t")) render.tex(vect4f(parts, 0));
			else if(parts[0].equals("v")){
				render.addVertex(vect3d(parts));
			}
		}
		render.compile();
	}
	
	public void draw(){
		gl.pushMatrix();
		gl.rotate(rotation.z, 0, 0, 1);
		gl.rotate(rotation.x, 1, 0, 0);
		gl.rotate(rotation.y, 0, 1, 0);
		gl.translate(offset.x, offset.y, offset.z);
		render.render();
		for(int d = 0; d < children.size(); ++d) children.get(d).draw();
		gl.popMatrix();
	}
	
	
	private Vector3d vect3d(String[] parts){
		return new Vector3d(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
	}
	
	private Vector4f vect4f(String[] parts, float def){
		if(parts.length > 4) return new Vector4f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]), Float.parseFloat(parts[4]));
		return new Vector4f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]), def);
	}
}
