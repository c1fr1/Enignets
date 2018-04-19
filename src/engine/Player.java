package engine;

import engine.Platform.PlatformSegment;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Player {
	public int health;
	public int mana;
	public Camera cam;
	public float speed;
	public Player(int hp, int mana, float speed) {
	
	}
	public float height() {
		return 5f;
	}
	public Vector3f getPos() {
		return cam.getPos().add(0, -height(), 0);
	}
	public void moveAlongPlane(Vector2f dir, PlatformSegment plane) {
		Vector3f pos = getPos();
		Vector2f ndir = dir.mul(speed);
		Vector2f npos = plane.getOffset(pos.x, pos.z, ndir.x, ndir.y);
		cam.setPos(new Vector3f(npos.x, plane.getHeight(npos) + height(), npos.y));
	}
	public void setPos(float x, float y, float z) {
		cam.setPos(x, y + height(), z);
	}
	public void setPos(Vector3f v) {
		setPos(v.x, v.y, v.z);
	}
}
