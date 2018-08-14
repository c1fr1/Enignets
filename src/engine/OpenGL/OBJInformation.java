package engine.OpenGL;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OBJInformation {//based off of https://github.com/MCRewind/3DGame/blob/dev/src/engine/graph/OBJLoader.java (which is based off of a tutorial I believe)
	public float[] vertices;
	public float[] textCoords;
	public float[] normals;
	public int[] indexArray;
	public OBJInformation() {
	
	}
	public OBJInformation getInfo(String fileName) {
		List<String> lines = null;
		try {
			lines = readAllLines(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Face> faces = new ArrayList<>();

		for (String line : lines) {
			//split("\\s+") gets a string without whitespace
			String[] tokens = line.split("\\s+");

			switch (tokens[0]) {
				case "v":
					// Geometric vertex
					Vector3f vec3f = new Vector3f(
							Float.parseFloat(tokens[1]),
							Float.parseFloat(tokens[2]),
							Float.parseFloat(tokens[3]));
					vertices.add(vec3f);
					break;
				case "vt":
					// Texture coordinate
					Vector2f vec2f = new Vector2f(
							Float.parseFloat(tokens[1]),
							Float.parseFloat(tokens[2]));
					textures.add(vec2f);
					break;
				case "vn":
					// Vertex normal
					Vector3f vec3fNorm = new Vector3f(
							Float.parseFloat(tokens[1]),
							Float.parseFloat(tokens[2]),
							Float.parseFloat(tokens[3]));
					normals.add(vec3fNorm);
					break;
				case "f":
					OBJInformation.Face face = new OBJInformation.Face(tokens[1], tokens[2], tokens[3]);
					faces.add(face);
					break;
				default:
					// Ignore other lines
					break;
			}
		}
		return new OBJInformation(vertices, textures, normals, faces);
	}

	//reorders lists from obj's into usable form
	private OBJInformation(List<Vector3f> posList, List<Vector2f> textCoordList,
						   List<Vector3f> normList, List<Face> facesList) {

		List<Integer> indices = new ArrayList();
		// Create position array in the order it has been declared
		float[] posArr = new float[posList.size() * 3];
		int i = 0;
		for (Vector3f pos : posList) {
			posArr[i * 3] = pos.x;
			posArr[i * 3 + 1] = pos.y;
			posArr[i * 3 + 2] = pos.z;
			i++;
		}
		float[] textCoordArr = new float[posList.size() * 2];
		float[] normArr = new float[posList.size() * 3];

		for (OBJInformation.Face face : facesList) {
			OBJInformation.IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
			for (OBJInformation.IdxGroup indValue : faceVertexIndices) {
				processFaceVertex(indValue, textCoordList, normList,
						indices, textCoordArr, normArr);
			}
		}
		int[] indicesArr = new int[indices.size()];
		indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();//positions, texturecoordinates, normals, indices;
		vertices = posArr;
		indexArray = indicesArr;
		textCoords = textCoordArr;
		normals = normArr;
	}
	private static void processFaceVertex(OBJInformation.IdxGroup indices, List<Vector2f> textCoordList,
										  List<Vector3f> normList, List<Integer> indicesList,
										  float[] texCoordArr, float[] normArr) {

		// Set index for vertex coordinates
		int posIndex = indices.idxPos;
		indicesList.add(posIndex);

		// Reorder texture coordinates
		if (indices.idxTextCoord >= 0) {
			Vector2f textCoord = textCoordList.get(indices.idxTextCoord);
			texCoordArr[posIndex * 2] = textCoord.x;
			texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
		}
		if (indices.idxVecNormal >= 0) {
			// Reorder vector normals
			Vector3f vecNorm = normList.get(indices.idxVecNormal);
			normArr[posIndex * 3] = vecNorm.x;
			normArr[posIndex * 3 + 1] = vecNorm.y;
			normArr[posIndex * 3 + 2] = vecNorm.z;
		}
	}

	//class that defines a face with helper methods
	protected static class Face {
		/**
		 * List of idxGroup groups for a face triangle (3 vertices per face).
		 */
		private OBJInformation.IdxGroup[] idxGroups = new OBJInformation.IdxGroup[3];

		public Face(String v1, String v2, String v3) {
			idxGroups = new OBJInformation.IdxGroup[3];
			// Parse the lines
			idxGroups[0] = parseLine(v1);
			idxGroups[1] = parseLine(v2);
			idxGroups[2] = parseLine(v3);
		}

		private OBJInformation.IdxGroup parseLine(String line) {
			OBJInformation.IdxGroup idxGroup = new OBJInformation.IdxGroup();

			String[] lineTokens = line.split("/");
			int length = lineTokens.length;
			//I subtract 1 since arrays start at 0 but OBJ file format assumes that they start at 1
			idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
			//if found more than one thing in obj line
			if (length > 1){
				// It can be empty if the obj does not define text coords
				String textCoord = lineTokens[1];
				idxGroup.idxTextCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : OBJInformation.IdxGroup.NO_VALUE;
				//if normals present
				if (length > 2) {
					idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
				}
			}
			return idxGroup;
		}
		public OBJInformation.IdxGroup[] getFaceVertexIndices() {
			return idxGroups;
		}
		
	}
	
	//inner class that holds the information for a group
	protected static class IdxGroup {
		
		public static final int NO_VALUE = -1;
		
		public int idxPos;
		
		public int idxTextCoord;
		
		public int idxVecNormal;
		
		public IdxGroup(){
			idxPos = NO_VALUE;
			idxTextCoord = NO_VALUE;
			idxVecNormal = NO_VALUE;
		}
	}
	public List<String> readAllLines(String fileName) throws Exception {
		List<String> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName)))) {
			String line;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		}
		return list;
	}
}
