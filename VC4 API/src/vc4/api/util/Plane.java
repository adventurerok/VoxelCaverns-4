package vc4.api.util;

import vc4.api.vector.Vector3d;

public class Plane {

	Vector3d normal = new Vector3d(0, 0, 0), point = new Vector3d(0, 0, 0);
	double d;
	
	public Plane(Vector3d v1, Vector3d v2, Vector3d v3){
		set3Points(v1, v2, v3);
	}
	
	public Plane() {
		// TASK Auto-generated constructor stub
	}
	
	public void set3Points(Vector3d v1, Vector3d v2, Vector3d v3){
		Vector3d aux1, aux2;

		aux1 = v1.subtract(v2);
		aux2 = v3.subtract(v2);

		normal = aux2.cross(aux1);

		normal = normal.normalize();
		point = normal;
		d = (float) -(normal.innerProduct(point));
	}
	
	public void setNormalAndPoint(Vector3d normal, Vector3d point){
		this.normal = normal;
		this.point = point;
		d = (float) -(normal.innerProduct(point));
	}
	
	public void setCoefficients(double x, double y, double z, double d){
		// set the normal vector
		normal = new Vector3d(x, y, z);
		//compute the lenght of the vector
		float l = (float) normal.length();
		// normalize the vector
		normal = new Vector3d(x/l, y/l, z/l);
		// and divide d by th length as well
		this.d = d/l;
	}
	
	public float distance(Vector3d p){
		return (float) (d + normal.innerProduct(p));
	}
}
