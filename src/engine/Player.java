package engine;

import Game.UserControls;
import engine.Platform.Platform;
import engine.Platform.PlatformSegment;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Player {
	public int health;
	public int mana;
	public Camera cam;
	public float speed;
	public float yVel = 0f;
	public Player(int hp, int mna, float sped) {
		health = hp;
		mana = mna;
		speed = sped;
		cam = new Camera(70f, 0.1f, 1000f);
	}
	public float height() {
		return -1f;
	}
	public Vector3f getPos() {
		return cam.getPos().add(0, -height(), 0);
	}
	public void moveAlongPlane(float x, float y, PlatformSegment plane) {
		Vector3f pos = getPos();
		Vector2f npos = plane.getOffset(pos.x, pos.z, x * speed, y * speed).add(pos.x, pos.z);
		cam.setPos(new Vector3f(npos.x, -plane.getHeight(npos) + height(), npos.y));
	}
	public void moveAlongPlane(Vector2f dir, PlatformSegment plane) {
		moveAlongPlane(dir.x, dir.y, plane);
	}
	public void setPos(float x, float y, float z) {
		cam.setPos(x, y + height(), z);
	}
	public void setPos(Vector3f v) {
		setPos(v.x, v.y, v.z);
	}
	public void updatePosition(PlatformSegment plane) {
		/*if (EnigWindow.mainWindow.keys[UserControls.up] > 0) {
			cam.translate(cam.getRotatedVector(0f, -1f, 0f, 0.1f));
		}else if (EnigWindow.mainWindow.keys[UserControls.down] > 0) {
			cam.translate(cam.getRotatedVector(0f, 1f, 0f, 0.1f));
		}*/
		if (EnigWindow.mainWindow.keys[UserControls.forward] > 0) {
			moveAlongPlane(cam.getRotated2DVector(0f, 1f, 1), plane);
		}else if (EnigWindow.mainWindow.keys[UserControls.backward] > 0) {
			moveAlongPlane(cam.getRotated2DVector(0f, -1f, 1), plane);
		}
		
		if (EnigWindow.mainWindow.keys[UserControls.left] > 0) {
			moveAlongPlane(cam.getRotated2DVector(1f, 0, 1), plane);
		}else if (EnigWindow.mainWindow.keys[UserControls.right] > 0) {
			moveAlongPlane(cam.getRotated2DVector(-1f, 0f, 1), plane);
		}
	}
	public void updateRotations() {
		cam.yaw(-(float) EnigWindow.mainWindow.cursorXOffset * UserControls.sensitivity);
		cam.pitch(-(float) EnigWindow.mainWindow.cursorYOffset * UserControls.sensitivity);
		if (cam.getPitch() > Math.PI/2) {
			cam.setPitch((float) Math.PI/2);
		}else if (cam.getPitch() < -Math.PI/2) {
			cam.setPitch((float) -Math.PI/2);
		}
	}
}
