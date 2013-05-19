package vc4.api.util;

import vc4.api.graphics.*;
import vc4.api.math.MathUtils;
import vc4.api.vector.Vector3d;

public class Fustrum {

	public static int OUTSIDE = 0;
	public static int INTERSECT = 1;
	public static int INSIDE = 2;

	public static int TOP = 0, BOTTOM = 1, LEFT = 2, RIGHT = 3, NEARP = 4, FARP = 5;

	public static double HALF_ANG2RAD = 3.14159265358979323846d / 360.0d;

	Plane pl[] = new Plane[6];

	Vector3d ntl, ntr, nbl, nbr, ftl, ftr, fbl, fbr, X, Y, Z, camPos;
	double nearD, farD, ratio, angle;
	double sphereFactorX, sphereFactorY;
	double tang;
	double nw, nh, fw, fh;

	public Fustrum() {
		for (int d = 0; d < 6; ++d)
			pl[d] = new Plane();
	}

	public float m(float[] m, int row, int col) {
		return m[row * 4 + col];
	}

	public void setFrustum(float... m) {
		pl[NEARP].setCoefficients(m(m, 2, 0) + m(m, 3, 0), m(m, 2, 1) + m(m, 3, 1), m(m, 2, 2) + m(m, 3, 2), m(m, 2, 3) + m(m, 3, 3));
		pl[FARP].setCoefficients(-m(m, 2, 0) + m(m, 3, 0), -m(m, 2, 1) + m(m, 3, 1), -m(m, 2, 2) + m(m, 3, 2), -m(m, 2, 3) + m(m, 3, 3));
		pl[BOTTOM].setCoefficients(m(m, 1, 0) + m(m, 3, 0), m(m, 1, 1) + m(m, 3, 1), m(m, 1, 2) + m(m, 3, 2), m(m, 1, 3) + m(m, 3, 3));
		pl[TOP].setCoefficients(-m(m, 1, 0) + m(m, 3, 0), -m(m, 1, 1) + m(m, 3, 1), -m(m, 1, 2) + m(m, 3, 2), -m(m, 1, 3) + m(m, 3, 3));
		pl[LEFT].setCoefficients(m(m, 0, 0) + m(m, 3, 0), m(m, 0, 1) + m(m, 3, 1), m(m, 0, 2) + m(m, 3, 2), m(m, 0, 3) + m(m, 3, 3));
		pl[RIGHT].setCoefficients(-m(m, 0, 0) + m(m, 3, 0), -m(m, 0, 1) + m(m, 3, 1), -m(m, 0, 2) + m(m, 3, 2), -m(m, 0, 3) + m(m, 3, 3));
	}

	public void setCamInternals(double angle, double ratio, double nearD, double farD) {
		this.ratio = ratio;
		this.angle = angle * HALF_ANG2RAD;
		this.nearD = nearD;
		this.farD = farD;

		// compute width and height of the near and far plane sections
		tang = Math.tan(this.angle);
		sphereFactorY = 1.0 / MathUtils.cos((float) this.angle);// tang * sin(this->angle) + cos(this->angle);

		double anglex = Math.atan(tang * ratio);
		sphereFactorX = 1.0 / MathUtils.cos((float) anglex); // tang*ratio * sin(anglex) + cos(anglex);

		nh = nearD * tang;
		nw = nh * ratio;

		fh = farD * tang;
		fw = fh * ratio;
	}

	public void setCamDef(Vector3d p, Vector3d l, Vector3d u) {

		Vector3d nc, fc;

		camPos = p;

		// compute the Z axis of camera
		Z = p.subtract(l);
		Z = Z.normalize();

		// X axis of camera of given "up" vector and Z axis
		X = u.cross(Z);
		X = X.normalize();

		// the real "up" vector is the cross product of Z and X
		Y = Z.cross(X);

		// compute the center of the near and far planes
		nc = p.subtract(Z.multiply(nearD));
		fc = p.subtract(Z.multiply(farD));

		// compute the 8 corners of the frustum
		ntl = nc.add(Y.multiply(nh)).subtract(X.multiply(nw));
		ntr = nc.add(Y.multiply(nh)).add(X.multiply(nw));
		nbl = nc.subtract(Y.multiply(nh)).subtract(X.multiply(nw));
		nbr = nc.subtract(Y.multiply(nh)).add(X.multiply(nw));

		ftl = fc.add(Y.multiply(fh)).subtract(X.multiply(fw));
		fbr = fc.subtract(Y.multiply(fh)).add(X.multiply(fw));
		ftr = fc.add(Y.multiply(fh)).add(X.multiply(fw));
		fbl = fc.subtract(Y.multiply(fh)).subtract(X.multiply(fw));

		// compute the six planes
		// the function set3Points asssumes that the points
		// are given in counter clockwise order
		pl[TOP].set3Points(ntr, ntl, ftl);
		pl[BOTTOM].set3Points(nbl, nbr, fbr);
		pl[LEFT].set3Points(ntl, nbl, fbl);
		pl[RIGHT].set3Points(nbr, ntr, fbr);
		// pl[NEARP].set3Points(ntl,ntr,nbr);
		// pl[FARP].set3Points(ftr,ftl,fbl);

		pl[NEARP].setNormalAndPoint(Z.negate(), nc);
		pl[FARP].setNormalAndPoint(Z, fc);

		Vector3d aux, normal;

		aux = (nc.add(Y.multiply(nh))).subtract(p);
		normal = aux.cross(X);
		pl[TOP].setNormalAndPoint(normal, nc.add(Y.multiply(nh)));

		aux = (nc.subtract(Y.multiply(nh))).subtract(p);
		normal = X.cross(aux);
		pl[BOTTOM].setNormalAndPoint(normal, nc.subtract(Y.multiply(nh)));

		aux = (nc.subtract(X.multiply(nw))).subtract(p);
		normal = aux.cross(Y);
		pl[LEFT].setNormalAndPoint(normal, nc.subtract(X.multiply(nw)));

		aux = (nc.add(X.multiply(nw))).subtract(p);
		normal = Y.cross(aux);
		pl[RIGHT].setNormalAndPoint(normal, nc.add(X.multiply(nw)));
	}

	public int boxInFrustum(AABB b) {

		int result = INSIDE;
		// for each plane do ...
		for (int i = 0; i < 6; i++) {

			// is the positive vertex outside?
			boolean p = pl[i].distance(b.getVertexP(pl[i].normal)) < 0;
			// is the negative vertex outside?
			boolean n = pl[i].distance(b.getVertexN(pl[i].normal)) < 0;
			if (p && n) return OUTSIDE;
			else if (p || n) result = INTERSECT;
		}
		return (result);
	}

	public void drawLines() {
		OpenGL gl = Graphics.getClientOpenGL();
		gl.begin(GLPrimative.LINE_LOOP);
		// near plane
		gl.vertex(ntl.x, ntl.y, ntl.z);
		gl.vertex(ntr.x, ntr.y, ntr.z);
		gl.vertex(nbr.x, nbr.y, nbr.z);
		gl.vertex(nbl.x, nbl.y, nbl.z);
		gl.end();

		gl.begin(GLPrimative.LINE_LOOP);
		// far plane
		gl.vertex(ftr.x, ftr.y, ftr.z);
		gl.vertex(ftl.x, ftl.y, ftl.z);
		gl.vertex(fbl.x, fbl.y, fbl.z);
		gl.vertex(fbr.x, fbr.y, fbr.z);
		gl.end();

		gl.begin(GLPrimative.LINE_LOOP);
		// bottom plane
		gl.vertex(nbl.x, nbl.y, nbl.z);
		gl.vertex(nbr.x, nbr.y, nbr.z);
		gl.vertex(fbr.x, fbr.y, fbr.z);
		gl.vertex(fbl.x, fbl.y, fbl.z);
		gl.end();

		gl.begin(GLPrimative.LINE_LOOP);
		// top plane
		gl.vertex(ntr.x, ntr.y, ntr.z);
		gl.vertex(ntl.x, ntl.y, ntl.z);
		gl.vertex(ftl.x, ftl.y, ftl.z);
		gl.vertex(ftr.x, ftr.y, ftr.z);
		gl.end();

		gl.begin(GLPrimative.LINE_LOOP);
		// left plane
		gl.vertex(ntl.x, ntl.y, ntl.z);
		gl.vertex(nbl.x, nbl.y, nbl.z);
		gl.vertex(fbl.x, fbl.y, fbl.z);
		gl.vertex(ftl.x, ftl.y, ftl.z);
		gl.end();

		gl.begin(GLPrimative.LINE_LOOP);
		// right plane
		gl.vertex(nbr.x, nbr.y, nbr.z);
		gl.vertex(ntr.x, ntr.y, ntr.z);
		gl.vertex(ftr.x, ftr.y, ftr.z);
		gl.vertex(fbr.x, fbr.y, fbr.z);

		gl.end();
	}
}
