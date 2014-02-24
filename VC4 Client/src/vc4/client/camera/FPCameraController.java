/**
 * 
 */
package vc4.client.camera;

import org.lwjgl.opengl.GL11;

import vc4.api.vector.Vector3d;

// First Person Camera Controller
public class FPCameraController extends FPCamera {

	double mouseSensitivity = 0.2f;
	double movementSpeed = 1; // move 10 units per second

	// the rotation around the Y axis of the camera
	private double yaw = 0.0f;
	// the rotation around the X axis of the camera
	private double pitch = 0.0f;

	// Constructor that takes the starting x, y, z location of the camera
	public FPCameraController(double x, double y, double z) {
		super(x, y, z);
	}

	// increment the camera's current yaw rotation
	@Override
	public void yaw(double amount) {
		// increment the yaw by the amount param
		yaw += amount;
	}

	// increment the camera's current yaw rotation
	@Override
	public void pitch(double amount) {
		// increment the pitch by the amount param
		pitch += amount;
		if (pitch > 90) pitch = 90;
		if (pitch < -90) pitch = -90;
	}

	// moves the camera forward relative to its current rotation (yaw)
	@Override
	public void walkForward(double distance) {
		position.x += distance * Math.sin(Math.toRadians(yaw));
		position.z -= distance * Math.cos(Math.toRadians(yaw));
	}

	// moves the camera backward relative to its current rotation (yaw)
	@Override
	public void walkBackwards(double distance) {
		position.x -= distance * Math.sin(Math.toRadians(yaw));
		position.z += distance * Math.cos(Math.toRadians(yaw));
	}

	// strafes the camera left relitive to its current rotation (yaw)
	@Override
	public void strafeLeft(double distance) {
		position.x -= distance * Math.sin(Math.toRadians(yaw + 90));
		position.z += distance * Math.cos(Math.toRadians(yaw + 90));
	}

	// strafes the camera right relitive to its current rotation (yaw)
	@Override
	public void strafeRight(double distance) {
		position.x -= distance * Math.sin(Math.toRadians(yaw - 90));
		position.z += distance * Math.cos(Math.toRadians(yaw - 90));
	}

	// translates and rotate the matrix so that it looks through the camera
	// this dose basic what gluLookAt() does
	@Override
	public void lookThrough() {
		rotate();
		translate();

	}

	@Override
	public void rotate() {
		// roatate the pitch around the X axis
		GL11.glRotated(pitch, 1.0f, 0.0f, 0.0f);
		// roatate the yaw around the Y axis
		GL11.glRotated(yaw, 0.0f, 1.0f, 0.0f);
	}
	
	@Override
	public void translate(){
		GL11.glTranslated(position.x, position.y, position.z);
	}

	@Override
	public Vector3d getLook() {
		double f = Math.cos(-yaw * 0.01745329F - Math.PI);
		double f2 = Math.sin(-yaw * 0.01745329F - Math.PI);
		double f4 = -Math.cos(pitch * 0.01745329F);
		double f6 = Math.sin(pitch * 0.01745329F);
		return new Vector3d(f2 * f4, f6, f * f4);
	}

}
