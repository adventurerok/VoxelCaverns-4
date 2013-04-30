/**
 * 
 */
package vc4.client.camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import vc4.api.client.Client;
import vc4.api.math.MathUtils;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;

/**
 * @author Paul Durbaba
 * 
 */
public abstract class FPCamera {

	double mouseSensitivity = 0.2f;
	double movementSpeed = 1; // move 10 units per second

	// 3d vector to store the camera's position in
	public Vector3d position = null;

	public FPCamera() {
		// TASK Auto-generated constructor stub
	}
	
	// Constructor that takes the starting x, y, z location of the camera
	public FPCamera(double x, double y, double z) {
		// instantiate position Vector3f to the x y z params.
		position = new Vector3d(x, y, z);
	}

	// increment the camera's current yaw rotation
	public abstract void yaw(double amount);

	// increment the camera's current yaw rotation
	public abstract void pitch(double amount);

	// moves the camera forward relative to its current rotation (yaw)
	public abstract void walkForward(double distance);

	// moves the camera backward relative to its current rotation (yaw)
	public abstract void walkBackwards(double distance);

	// strafes the camera left relitive to its current rotation (yaw)
	public abstract void strafeLeft(double distance);

	// strafes the camera right relitive to its current rotation (yaw)
	public abstract void strafeRight(double distance);

	// translates and rotate the matrix so that it looks through the camera
	// this dose basic what gluLookAt() does
	public abstract void lookThrough();

	public void handleInput(double delta) {

		double dx = 0.0f;
		double dy = 0.0f;
		
		//MouseSet mice = Client.getGame().getMouseSet();

		boolean paused = Client.getGame().isPaused();
		if (Display.isActive() && !paused) {
			// distance in mouse movement from the last getDX() call.
			dx = Mouse.getDX();
			// distance in mouse movement from the last getDY() call.
			dy = Mouse.getDY();

			// controll camera yaw from x movement fromt the mouse
			yaw(dx * mouseSensitivity);
			// controll camera pitch from y movement fromt the mouse
			pitch(dy * mouseSensitivity);

			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		}
		Mouse.setGrabbed(!paused);

		if(Keyboard.isKeyDown(Keyboard.KEY_BACKSLASH)) delta *= 5;

		// when passing in the distance to move
		// we times the movementSpeed with dt this is a time scale
		// so if its a slow frame u move more then a fast frame
		// so on a slow computer you move just as fast as on a fast computer
		if (Keyboard.isKeyDown(Keyboard.KEY_W))// move forward
		{
			walkForward((movementSpeed * (delta / 15)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S))// move backwards
		{
			walkBackwards((movementSpeed * (delta / 15)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A))// strafe left
		{
			strafeLeft((movementSpeed * (delta / 15)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))// strafe right
		{
			strafeRight((movementSpeed * (delta / 15)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y -= movementSpeed * (delta / 15);
			if(Keyboard.isKeyDown(Keyboard.KEY_T)){
				position.y = -15;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y += movementSpeed * (delta / 15);
			if(Keyboard.isKeyDown(Keyboard.KEY_T)){
				position.y = 5950;
			}
		}
	}

	public Vector3l getWorldPosition() {
		return new Vector3l(MathUtils.round(-position.x), MathUtils.round(-position.y), MathUtils.round(-position.z));
	}

	public Vector3d getPosition() {
		return new Vector3d(-position.x, -position.y, -position.z);
	}

	/**
	 * 
	 */
	public void rotate() {
	}

	/**
	 * 
	 */
	public void translate() {
	}

}
