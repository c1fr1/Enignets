package engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

	private int shaderID;

	public Attribute[] attributes;
	public Uniform[] uniforms;

	/**
	 * creates a new shader
	 * @param path
	 * path to the shader
	 * @param stype
	 * type of shader
	 */
	public Shader(String path, int stype) {
		String shaderSource = "";

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
			String line;
			ArrayList<Attribute> tempattriblist = new ArrayList<>();
			ArrayList<Uniform> tempuniflist = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("layout (location")) {
					int startIndex = 15;
					for (char c : line.substring(15).toCharArray()) {
						startIndex++;
						if (c == '=') {
							break;
						}
					}
					String locStr = "";
					for (char c: line.substring(startIndex).toCharArray()) {
						startIndex++;
						if (c == ')')  {
							break;
						}else if (c != ' ') {
							locStr += c;
						}
					}
					startIndex++;
					int loc = Integer.parseInt(locStr);
					String type = "";
					for (char c:line.substring(startIndex).toCharArray()) {
						startIndex++;
						if (c == ' ') {
							break;
						}
					}
					for (char c:line.substring(startIndex).toCharArray()) {
						startIndex++;
						if (c != ' ') {
							type += c;
						}else if (type.length() > 0) {
							break;
						}
					}
					String name = "";
					for (char c:line.substring(startIndex).toCharArray()) {
						if (c != ' ' && c != ';'){
							name += c;
						}
					}
					int vecSize = 0;
					if (type.equals("float")) {
						vecSize = 1;
					}else if (type.startsWith("vec")) {
						vecSize = Integer.parseInt(type.substring(3));
					}else if (type.startsWith("mat")) {
						vecSize = Integer.parseInt(type.substring(3));
					}else {
						throw new IOException("Invalid attribute type in shader");
					}
					tempattriblist.add(new Attribute(vecSize, name, loc));
				}else if (line.startsWith("uniform ")) {//uniform vecn name;
					int startIndex = 8;
					String type = "";
					for (char c: line.substring(startIndex).toCharArray()) {
						startIndex++;
						if (c == ' ') {
							break;
						}
						type += c;
					}
					String name = "";
					for (char c: line.substring(startIndex).toCharArray()) {
						if (c != ' ' && c != ';') {
							name += c;
						}
					}
					int vecSize = 0;
					if (type.equals("float")) {
						vecSize = 1;
						tempuniflist.add(new Uniform(name, vecSize));
					}else if (type.startsWith("vec")) {
						vecSize = Integer.parseInt(type.substring(3));
						tempuniflist.add(new Uniform(name, vecSize));
					}else if (type.startsWith("mat")) {
						vecSize = Integer.parseInt(type.substring(3));
						tempuniflist.add(new Uniform(name, vecSize, true));
					}
				}
				shaderSource += line + "\n";
			}
			attributes = new Attribute[tempattriblist.size()];
			for (int i = 0; i<tempattriblist.size();i++) {
				attributes[i] = tempattriblist.get(i);
			}
			uniforms = new Uniform[tempuniflist.size()];
			for (int i = 0; i<tempuniflist.size();i++) {
				uniforms[i] = tempuniflist.get(i);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		shaderID = glCreateShader(stype);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		if(glGetShaderi(shaderID, GL_COMPILE_STATUS) != 1) {
			throw new RuntimeException("Failed to compile shader: " + path + "! " + glGetShaderInfoLog(shaderID));
		}
	}

	public int getID() {
		return shaderID;
	}

	public void destroy() {
		glDeleteShader(shaderID);
	}
}