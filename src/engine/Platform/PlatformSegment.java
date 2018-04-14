package engine.Platform;

import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class PlatformSegment {
    /**
     * finds the offset based on the tangent plane so that distance traveled is a constant
     * finds the magnitude of the velocity, then adds the y component, and resizes it
     * @param xPos
     * current x position
     * @param zPos
     * current z position
     * @param xVelocity
     * x velocity magnitude
     * @param zVelocity
     * y velocity magnitude
     * @return
     * the offset based on the tangent plane so that the distance traveled is a constant
     */
    public Vector2f getOffset(float xPos, float zPos, float xVelocity, float zVelocity) {
        Vector3f fullVector = new Vector3f(xVelocity, getTangentPlane(xPos, zPos).gety(xVelocity, zVelocity), zVelocity).normalize().mul((float) Math.sqrt(xVelocity * xVelocity + zVelocity * zVelocity));
        return new Vector2f(fullVector.x, fullVector.z);
    }
    public Vector2f getOffset(Vector2f pos, Vector2f velocity) {
        return getOffset(pos.x, pos.y, velocity.x, velocity.y);
    }
    public Vector2f getOffset(float posx, float posy, Vector2f velocity) {
        return getOffset(posx, posy, velocity.x, velocity.y);
    }
    public Vector2f getOffset(Vector2f pos, float xvelocity, float yvelocity) {
        return getOffset(pos.x, pos.y, xvelocity, yvelocity);
    }

    /**
     * finds the height of the object on the plane given the x and z positions
     * @param xPos
     * x position
     * @param zPos
     * z position
     * @return
     * y value that corresponds to the x and z positions
     */
    public abstract float getHeight(float xPos, float zPos);
    public float getHeight(Vector2f pos) {
        return getHeight(pos.x, pos.y);
    }

    /**
     * returns the plane tangent to the platform at a given x an z position
     * @param xPos
     * x positon
     * @param zPos
     * z position
     * @return
     * the plane tangent to the platform at a given x and z position
     */
    public abstract Plane getTangentPlane(float xPos, float zPos);
    public Plane getTangentPlane(Vector2f pos) {
        return getTangentPlane(pos.x, pos.y);
    }
}
