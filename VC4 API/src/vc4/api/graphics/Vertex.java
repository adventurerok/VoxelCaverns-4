package vc4.api.graphics;

import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector4f;

public class Vertex{
		
		public Vector3d position;
		public Vector4f color;
		public Vector4f tex;
		
		
		public Vertex(Vector3d position, Vector4f color, Vector4f tex) {
			super();
			this.position = position;
			this.color = color;
			this.tex = tex;
		}


		public Vertex() {
			super();
		}



		
		
	}