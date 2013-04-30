/**
 * 
 */
package vc4.client.camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import vc4.api.client.Client;
import vc4.api.entity.EntityPlayer;
import vc4.api.math.MathUtils;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;

// First Person Camera Controller
public class PlayerController extends FPCamera {

	double mouseSensitivity = 0.2f;
	double movementSpeed = 1; // move 10 units per second

	EntityPlayer player;

	// Constructor that takes the starting x, y, z location of the camera
	public PlayerController(EntityPlayer player) {
		this.player = player;
	}

	// increment the camera's current yaw rotation
	@Override
	public void yaw(double amount) {
		// increment the yaw by the amount param
		player.yaw += amount;
	}

	// increment the camera's current yaw rotation
	@Override
	public void pitch(double amount) {
		// increment the pitch by the amount param
		player.pitch += amount;
		if (player.pitch > 90) player.pitch = 90;
		if (player.pitch < -90) player.pitch = -90;
	}

	// moves the camera forward relative to its current rotation (yaw)
	@Override
	public void walkForward(double distance) {
		player.motionX -= distance * Math.sin(Math.toRadians(player.yaw));
		player.motionZ += distance * Math.cos(Math.toRadians(player.yaw));
	}

	// moves the camera backward relative to its current rotation (yaw)
	@Override
	public void walkBackwards(double distance) {
		player.motionX += distance * Math.sin(Math.toRadians(player.yaw));
		player.motionZ -= distance * Math.cos(Math.toRadians(player.yaw));
	}

	// strafes the camera left relitive to its current rotation (yaw)
	@Override
	public void strafeLeft(double distance) {
		player.motionX += distance * Math.sin(Math.toRadians(player.yaw + 90));
		player.motionZ -= distance * Math.cos(Math.toRadians(player.yaw + 90));
	}

	// strafes the camera right relitive to its current rotation (yaw)
	@Override
	public void strafeRight(double distance) {
		player.motionX += distance * Math.sin(Math.toRadians(player.yaw - 90));
		player.motionZ -= distance * Math.cos(Math.toRadians(player.yaw - 90));
	}

	// translates and rotate the matrix so that it looks through the camera
	// this dose basic what gluLookAt() does
	@Override
	public void lookThrough() {
		rotate();
		translate();

	}
	
	@Override
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

		double divisor = 100;
		if(Keyboard.isKeyDown(Keyboard.KEY_BACKSLASH)) delta *= 5;

		// when passing in the distance to move
		// we times the movementSpeed with dt this is a time scale
		// so if its a slow frame u move more then a fast frame
		// so on a slow computer you move just as fast as on a fast computer
		if (Keyboard.isKeyDown(Keyboard.KEY_W))// move forward
		{
			walkForward((movementSpeed * (delta / divisor)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S))// move backwards
		{
			walkBackwards((movementSpeed * (delta / divisor)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A))// strafe left
		{
			strafeLeft((movementSpeed * (delta / divisor)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))// strafe right
		{
			strafeRight((movementSpeed * (delta / divisor)));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			player.jump();
		}
		player.setSneaking(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT));
	}

	@Override
	public void rotate() {
		// roatate the pitch around the X axis
		GL11.glRotated(player.pitch, 1.0f, 0.0f, 0.0f);
		// roatate the yaw around the Y axis
		GL11.glRotated(player.yaw, 0.0f, 1.0f, 0.0f);
	}
	
	@Override
	public void translate(){
		Vector3d pos = player.getEyePos();
		GL11.glTranslated(-pos.x, -pos.y, -pos.z);
	}

	public Vector3d getLook() {
		double f = Math.cos(-player.yaw * 0.01745329F - Math.PI);
		double f2 = Math.sin(-player.yaw * 0.01745329F - Math.PI);
		double f4 = -Math.cos(player.pitch * 0.01745329F);
		double f6 = Math.sin(player.pitch * 0.01745329F);
		return new Vector3d(f2 * f4, f6, f * f4);
	}
	
	@Override
	public Vector3l getWorldPosition() {
		return new Vector3l(MathUtils.round(player.position.x), MathUtils.round(player.position.y), MathUtils.round(player.position.z));
	}

	@Override
	public Vector3d getPosition() {
		return player.position.clone();
	}

}
