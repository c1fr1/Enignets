package engine.Entities;

import Game.UserControls;
import engine.EnigUtils;
import engine.OpenGL.EnigWindow;
import engine.Platform.PlatformSegment;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Vector;

import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alListener3f;

public class Player extends Camera {
	public int health;
	public int mana;
	
	public float speed;
	
	public float yVel = 0f;
	
	/**
	 * creates a player given stats
	 * @param hp starting hp
	 * @param mna starting mana
	 * @param sped speed that the player travels
	 */
	public Player(int hp, int mna, float sped) {
		super(70f, 0.1f, 1000f);
		health = hp;
		mana = mna;
		speed = sped;
	}
	
	/**
	 * height off the ground the player is
	 * @return height of the player
	 */
	
	public float height() {
		return 1f;
	}
	
	/**
	 * gets the position with the offset of the player's height
	 * @return
	 */
	public Vector3f getPos() {
		return super.getPos().add(0, height(), 0);
	}
	
	/**
	 * move the player a direction on the plane, the distance will be held as a constant
	 * @param dx change in x
	 * @param dz change in z
	 * @param plane plane the player is on
	 */
	public void moveAlongPlane(float dx, float dz, PlatformSegment plane) {
		Vector3f pos = getPos();
		Vector2f npos = plane.getOffset(pos.x, pos.z, dx * speed, dz * speed);
		npos.add(pos.x, pos.z);
		Vector3f nextPosition = new Vector3f(npos.x, plane.getHeight(npos) + height(), npos.y);
		setPos(nextPosition);
		/*Vector3f pos = getPos();
		Vector3f unChangednpos = new Vector3f(x + dx*speed, plane.getTangentPlane(x, z).gety(x + dx*speed, z + dz*speed), z + dx *speed);
		setPos(unChangednpos);*/
		
	}
	
	/**
	 * move the player a direction on the plane, the distance will be held as a constant
	 * @param dir change in position
	 * @param plane plane the player is on
	 */
	public void moveAlongPlane(Vector2f dir, PlatformSegment plane) {
		moveAlongPlane(dir.x, dir.y, plane);
	}
	
	/**
	 * the player as represented by a sting
	 * @return string value
	 */
	public String toString() {
		return "(x = " + EnigUtils.format(x, 4) + " y = " + EnigUtils.format(y, 4) + " z = " + EnigUtils.format(z, 4) + ") hp = " + health + " mana = " + mana;
	}
	
	/**
	 * changes the players position according to the current state of controls
	 * @param plane plane that the player is on
	 * @param w window to get controls
	 */
	public void updatePosition(PlatformSegment plane, EnigWindow w) {
		if (UserControls.forward(w)) {
			moveAlongPlane(getRotated2DVector(0f, -1f, 1), plane);
		}else if (UserControls.backward(w)) {
			moveAlongPlane(getRotated2DVector(0f, 1f, 1), plane);
		}
		
		if (UserControls.left(w)) {
			moveAlongPlane(getRotated2DVector(-1f, 0, 1), plane);
		}else if (UserControls.right(w)) {
			moveAlongPlane(getRotated2DVector(1f, 0f, 1), plane);
		}
	}
	
	/**
	 * updates the rotations of the player according to the change in cursor postion
	 */
	public void updateRotations() {
		yaw(-(float) EnigWindow.mainWindow.cursorXOffset * UserControls.sensitivity);
		pitch(-(float) EnigWindow.mainWindow.cursorYOffset * UserControls.sensitivity);
		if (getPitch() > Math.PI/2) {
			setPitch((float) Math.PI/2);
		}else if (getPitch() < -Math.PI/2) {
			setPitch((float) -Math.PI/2);
		}
	}
}
