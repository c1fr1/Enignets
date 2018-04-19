package engine.Platform;

public class Platform extends PlatformSegment {

	private PlatformSegment[] segments;

	private int[][] segmentReferences;

	private float segmentSize;

	public Platform(float size, int width, int height, int segmentNums) {
		segmentSize = size;
		segmentReferences = new int[width][height];
		segments = new PlatformSegment[segmentNums];
	}

	public Platform(int[][] segmentRef, PlatformSegment[] parts, float size) {
		segments = parts;
		segmentReferences = segmentRef;
		segmentSize = size;
	}

	@Override
	public float getHeight(float xPos, float zPos) {
		return segments[segmentReferences[(int) (xPos/segmentSize)][(int) (xPos/segmentSize)]].getHeight(xPos % segmentSize, zPos % segmentSize);
	}

	@Override
	public Plane getTangentPlane(float xPos, float zPos) {
		return segments[segmentReferences[(int) (xPos/segmentSize)][(int) (xPos/segmentSize)]].getTangentPlane(xPos % segmentSize, zPos % segmentSize);
	}
}
