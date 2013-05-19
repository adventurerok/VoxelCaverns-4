/**
 * 
 */
package vc4.client.camera;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import vc4.api.client.Client;
import vc4.api.entity.EntityPlayer;
import vc4.api.entity.MovementStyle;
import vc4.api.input.Input;
import vc4.api.input.Key;
import vc4.api.math.MathUtils;
import vc4.api.vector.Vector3d;
import vc4.api.vector.Vector3l;

// First Person Camera Controller
public class PlayerController extends FPCamera {

	double mouseSensitivity = 0.2f;
	double timeSinceForward = 1000;
	double timeSinceJump = 1000;
	double walkTime = 0;
	double jumpTime = 0;

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

		// MouseSet mice = Client.getGame().getMouseSet();

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
		
		vc4.api.input.Keyboard keys = Input.getClientKeyboard();

		double divisor = 50;
		boolean spe = keys.isKeyDown(Key.BACKSLASH);
		if (spe) delta *= 5;

		double forward = 0;
		double sideways = 0;

		// when passing in the distance to move
		// we times the movementSpeed with dt this is a time scale
		// so if its a slow frame u move more then a fast frame
		// so on a slow computer you move just as fast as on a fast computer
		if (keys.isKeyDown(Key.W)){
			forward += delta / divisor;
			walkTime += delta;
			if(timeSinceForward < 200){
				player.setMovement(MovementStyle.SPRINT);
			}
		}
		else if(keys.keyReleased(Key.W)){
			if(walkTime < 200) timeSinceForward = 0;
			walkTime = 0;
			if (!keys.isKeyDown(Key.LSHIFT) && player.getMovement() != MovementStyle.FLY) player.setMovement(MovementStyle.WALK);
		}
		
		if (keys.isKeyDown(Key.S)) forward -= delta / divisor;
		if (keys.isKeyDown(Key.A)) sideways -= delta / divisor;
		if (keys.isKeyDown(Key.D)) sideways += delta / divisor;
		if (keys.isKeyDown(Key.SPACE)){
			player.jump();
			if(spe) for(int d = 0; d < 4; ++d) player.jump();
			jumpTime += delta;
			if(timeSinceJump < 200){
				if(player.getMovement() != MovementStyle.FLY) player.setMovement(MovementStyle.FLY);
				else player.setMovement(MovementStyle.WALK);
			}
		} else if(keys.keyReleased(Key.SPACE)){
			if(jumpTime < 200) timeSinceJump = 0;
			jumpTime = 0;
		}
		if (keys.isKeyDown(Key.LSHIFT)){
			player.sneak();
			if(spe) for(int d = 0; d < 4; ++d) player.sneak();
		}
		else if(keys.keyReleased(Key.LSHIFT) && player.getMovement() == MovementStyle.SNEAK) player.setMovement(MovementStyle.WALK);
		player.walk(forward, sideways);
		if(timeSinceForward < 1000) timeSinceForward += delta;
	}

	@Override
	public void rotate() {
		// roatate the pitch around the X axis
		GL11.glRotated(player.pitch, 1.0f, 0.0f, 0.0f);
		// roatate the yaw around the Y axis
		GL11.glRotated(player.yaw, 0.0f, 1.0f, 0.0f);
	}

	@Override
	public void translate() {
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
		return player.getEyePos().clone();
	}

}
