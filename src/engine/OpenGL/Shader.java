package engine.OpenGL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

	public static ArrayList<Integer> shaderIDs = new ArrayList<>();

	private int id;

	public Attribute[] attributes;
	public Uniform[] uniforms;
	public Sampler[] samplers;

	/**
	 * creates a new shader
	 * @param path path to the shader
	 * @param stype type of shader
	 */
	public Shader(String path, int stype) {
		String shaderSource = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path)/*path*/));
			String line;
			ArrayList<Attribute> tempattriblist = new ArrayList<>();
			ArrayList<Uniform> tempuniflist = new ArrayList<>();
			ArrayList<String> tempSamplerList = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(">")) {
					if (line.startsWith(">noise4")) {
						shaderSource += "// Many thanks to Ian McEwan of Ashima Arts for the\n" +
								"// ideas for permutation and gradient selection.\n" +
								"//\n" +
								"// Copyright (c) 2011 Stefan Gustavson. All rights reserved.\n" +
								"// Distributed under the MIT license. See LICENSE file.\n" +
								"// https://github.com/stegu/webgl-noise\n" +
								"//\n" +
								"\n" +
								"vec4 mod289(vec4 x)\n" +
								"{\n" +
								"  return x - floor(x * (1.0 / 289.0)) * 289.0;\n" +
								"}\n" +
								"\n" +
								"vec4 permute(vec4 x)\n" +
								"{\n" +
								"  return mod289(((x*34.0)+1.0)*x);\n" +
								"}\n" +
								"\n" +
								"vec4 taylorInvSqrt(vec4 r)\n" +
								"{\n" +
								"  return 1.79284291400159 - 0.85373472095314 * r;\n" +
								"}\n" +
								"\n" +
								"vec4 fade(vec4 t) {\n" +
								"  return t*t*t*(t*(t*6.0-15.0)+10.0);\n" +
								"}\n" +
								"\n" +
								"// Classic Perlin noise\n" +
								"float gnoise(vec4 P)\n" +
								"{\n" +
								"  vec4 Pi0 = floor(P); // Integer part for indexing\n" +
								"  vec4 Pi1 = Pi0 + 1.0; // Integer part + 1\n" +
								"  Pi0 = mod289(Pi0);\n" +
								"  Pi1 = mod289(Pi1);\n" +
								"  vec4 Pf0 = fract(P); // Fractional part for interpolation\n" +
								"  vec4 Pf1 = Pf0 - 1.0; // Fractional part - 1.0\n" +
								"  vec4 ix = vec4(Pi0.x, Pi1.x, Pi0.x, Pi1.x);\n" +
								"  vec4 iy = vec4(Pi0.yy, Pi1.yy);\n" +
								"  vec4 iz0 = vec4(Pi0.zzzz);\n" +
								"  vec4 iz1 = vec4(Pi1.zzzz);\n" +
								"  vec4 iw0 = vec4(Pi0.wwww);\n" +
								"  vec4 iw1 = vec4(Pi1.wwww);\n" +
								"\n" +
								"  vec4 ixy = permute(permute(ix) + iy);\n" +
								"  vec4 ixy0 = permute(ixy + iz0);\n" +
								"  vec4 ixy1 = permute(ixy + iz1);\n" +
								"  vec4 ixy00 = permute(ixy0 + iw0);\n" +
								"  vec4 ixy01 = permute(ixy0 + iw1);\n" +
								"  vec4 ixy10 = permute(ixy1 + iw0);\n" +
								"  vec4 ixy11 = permute(ixy1 + iw1);\n" +
								"\n" +
								"  vec4 gx00 = ixy00 * (1.0 / 7.0);\n" +
								"  vec4 gy00 = floor(gx00) * (1.0 / 7.0);\n" +
								"  vec4 gz00 = floor(gy00) * (1.0 / 6.0);\n" +
								"  gx00 = fract(gx00) - 0.5;\n" +
								"  gy00 = fract(gy00) - 0.5;\n" +
								"  gz00 = fract(gz00) - 0.5;\n" +
								"  vec4 gw00 = vec4(0.75) - abs(gx00) - abs(gy00) - abs(gz00);\n" +
								"  vec4 sw00 = step(gw00, vec4(0.0));\n" +
								"  gx00 -= sw00 * (step(0.0, gx00) - 0.5);\n" +
								"  gy00 -= sw00 * (step(0.0, gy00) - 0.5);\n" +
								"\n" +
								"  vec4 gx01 = ixy01 * (1.0 / 7.0);\n" +
								"  vec4 gy01 = floor(gx01) * (1.0 / 7.0);\n" +
								"  vec4 gz01 = floor(gy01) * (1.0 / 6.0);\n" +
								"  gx01 = fract(gx01) - 0.5;\n" +
								"  gy01 = fract(gy01) - 0.5;\n" +
								"  gz01 = fract(gz01) - 0.5;\n" +
								"  vec4 gw01 = vec4(0.75) - abs(gx01) - abs(gy01) - abs(gz01);\n" +
								"  vec4 sw01 = step(gw01, vec4(0.0));\n" +
								"  gx01 -= sw01 * (step(0.0, gx01) - 0.5);\n" +
								"  gy01 -= sw01 * (step(0.0, gy01) - 0.5);\n" +
								"\n" +
								"  vec4 gx10 = ixy10 * (1.0 / 7.0);\n" +
								"  vec4 gy10 = floor(gx10) * (1.0 / 7.0);\n" +
								"  vec4 gz10 = floor(gy10) * (1.0 / 6.0);\n" +
								"  gx10 = fract(gx10) - 0.5;\n" +
								"  gy10 = fract(gy10) - 0.5;\n" +
								"  gz10 = fract(gz10) - 0.5;\n" +
								"  vec4 gw10 = vec4(0.75) - abs(gx10) - abs(gy10) - abs(gz10);\n" +
								"  vec4 sw10 = step(gw10, vec4(0.0));\n" +
								"  gx10 -= sw10 * (step(0.0, gx10) - 0.5);\n" +
								"  gy10 -= sw10 * (step(0.0, gy10) - 0.5);\n" +
								"\n" +
								"  vec4 gx11 = ixy11 * (1.0 / 7.0);\n" +
								"  vec4 gy11 = floor(gx11) * (1.0 / 7.0);\n" +
								"  vec4 gz11 = floor(gy11) * (1.0 / 6.0);\n" +
								"  gx11 = fract(gx11) - 0.5;\n" +
								"  gy11 = fract(gy11) - 0.5;\n" +
								"  gz11 = fract(gz11) - 0.5;\n" +
								"  vec4 gw11 = vec4(0.75) - abs(gx11) - abs(gy11) - abs(gz11);\n" +
								"  vec4 sw11 = step(gw11, vec4(0.0));\n" +
								"  gx11 -= sw11 * (step(0.0, gx11) - 0.5);\n" +
								"  gy11 -= sw11 * (step(0.0, gy11) - 0.5);\n" +
								"\n" +
								"  vec4 g0000 = vec4(gx00.x,gy00.x,gz00.x,gw00.x);\n" +
								"  vec4 g1000 = vec4(gx00.y,gy00.y,gz00.y,gw00.y);\n" +
								"  vec4 g0100 = vec4(gx00.z,gy00.z,gz00.z,gw00.z);\n" +
								"  vec4 g1100 = vec4(gx00.w,gy00.w,gz00.w,gw00.w);\n" +
								"  vec4 g0010 = vec4(gx10.x,gy10.x,gz10.x,gw10.x);\n" +
								"  vec4 g1010 = vec4(gx10.y,gy10.y,gz10.y,gw10.y);\n" +
								"  vec4 g0110 = vec4(gx10.z,gy10.z,gz10.z,gw10.z);\n" +
								"  vec4 g1110 = vec4(gx10.w,gy10.w,gz10.w,gw10.w);\n" +
								"  vec4 g0001 = vec4(gx01.x,gy01.x,gz01.x,gw01.x);\n" +
								"  vec4 g1001 = vec4(gx01.y,gy01.y,gz01.y,gw01.y);\n" +
								"  vec4 g0101 = vec4(gx01.z,gy01.z,gz01.z,gw01.z);\n" +
								"  vec4 g1101 = vec4(gx01.w,gy01.w,gz01.w,gw01.w);\n" +
								"  vec4 g0011 = vec4(gx11.x,gy11.x,gz11.x,gw11.x);\n" +
								"  vec4 g1011 = vec4(gx11.y,gy11.y,gz11.y,gw11.y);\n" +
								"  vec4 g0111 = vec4(gx11.z,gy11.z,gz11.z,gw11.z);\n" +
								"  vec4 g1111 = vec4(gx11.w,gy11.w,gz11.w,gw11.w);\n" +
								"\n" +
								"  vec4 norm00 = taylorInvSqrt(vec4(dot(g0000, g0000), dot(g0100, g0100), dot(g1000, g1000), dot(g1100, g1100)));\n" +
								"  g0000 *= norm00.x;\n" +
								"  g0100 *= norm00.y;\n" +
								"  g1000 *= norm00.z;\n" +
								"  g1100 *= norm00.w;\n" +
								"\n" +
								"  vec4 norm01 = taylorInvSqrt(vec4(dot(g0001, g0001), dot(g0101, g0101), dot(g1001, g1001), dot(g1101, g1101)));\n" +
								"  g0001 *= norm01.x;\n" +
								"  g0101 *= norm01.y;\n" +
								"  g1001 *= norm01.z;\n" +
								"  g1101 *= norm01.w;\n" +
								"\n" +
								"  vec4 norm10 = taylorInvSqrt(vec4(dot(g0010, g0010), dot(g0110, g0110), dot(g1010, g1010), dot(g1110, g1110)));\n" +
								"  g0010 *= norm10.x;\n" +
								"  g0110 *= norm10.y;\n" +
								"  g1010 *= norm10.z;\n" +
								"  g1110 *= norm10.w;\n" +
								"\n" +
								"  vec4 norm11 = taylorInvSqrt(vec4(dot(g0011, g0011), dot(g0111, g0111), dot(g1011, g1011), dot(g1111, g1111)));\n" +
								"  g0011 *= norm11.x;\n" +
								"  g0111 *= norm11.y;\n" +
								"  g1011 *= norm11.z;\n" +
								"  g1111 *= norm11.w;\n" +
								"\n" +
								"  float n0000 = dot(g0000, Pf0);\n" +
								"  float n1000 = dot(g1000, vec4(Pf1.x, Pf0.yzw));\n" +
								"  float n0100 = dot(g0100, vec4(Pf0.x, Pf1.y, Pf0.zw));\n" +
								"  float n1100 = dot(g1100, vec4(Pf1.xy, Pf0.zw));\n" +
								"  float n0010 = dot(g0010, vec4(Pf0.xy, Pf1.z, Pf0.w));\n" +
								"  float n1010 = dot(g1010, vec4(Pf1.x, Pf0.y, Pf1.z, Pf0.w));\n" +
								"  float n0110 = dot(g0110, vec4(Pf0.x, Pf1.yz, Pf0.w));\n" +
								"  float n1110 = dot(g1110, vec4(Pf1.xyz, Pf0.w));\n" +
								"  float n0001 = dot(g0001, vec4(Pf0.xyz, Pf1.w));\n" +
								"  float n1001 = dot(g1001, vec4(Pf1.x, Pf0.yz, Pf1.w));\n" +
								"  float n0101 = dot(g0101, vec4(Pf0.x, Pf1.y, Pf0.z, Pf1.w));\n" +
								"  float n1101 = dot(g1101, vec4(Pf1.xy, Pf0.z, Pf1.w));\n" +
								"  float n0011 = dot(g0011, vec4(Pf0.xy, Pf1.zw));\n" +
								"  float n1011 = dot(g1011, vec4(Pf1.x, Pf0.y, Pf1.zw));\n" +
								"  float n0111 = dot(g0111, vec4(Pf0.x, Pf1.yzw));\n" +
								"  float n1111 = dot(g1111, Pf1);\n" +
								"\n" +
								"  vec4 fade_xyzw = fade(Pf0);\n" +
								"  vec4 n_0w = mix(vec4(n0000, n1000, n0100, n1100), vec4(n0001, n1001, n0101, n1101), fade_xyzw.w);\n" +
								"  vec4 n_1w = mix(vec4(n0010, n1010, n0110, n1110), vec4(n0011, n1011, n0111, n1111), fade_xyzw.w);\n" +
								"  vec4 n_zw = mix(n_0w, n_1w, fade_xyzw.z);\n" +
								"  vec2 n_yzw = mix(n_zw.xy, n_zw.zw, fade_xyzw.y);\n" +
								"  float n_xyzw = mix(n_yzw.x, n_yzw.y, fade_xyzw.x);\n" +
								"  return 2.2 * n_xyzw;\n" +
								"}\n";
						continue;
					}
				}else if (line.startsWith("layout (location")) {
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
					if (type.startsWith("float")) {
						vecSize = 1;
						tempuniflist.add(new Uniform(name, vecSize));
					}else if (type.startsWith("vec")) {
						vecSize = Integer.parseInt(type.substring(3, 4));
						tempuniflist.add(new Uniform(name, vecSize));
					}else if (type.startsWith("mat")) {
						vecSize = Integer.parseInt(type.substring(3, 4));
						tempuniflist.add(new Uniform(name, vecSize, 2));
					}else if (type.startsWith("int")) {
						vecSize = 1;
						tempuniflist.add(new Uniform(name, vecSize, 1));
					}else if (type.startsWith("sampler")) {
						tempSamplerList.add(name);
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
			samplers = new Sampler[tempSamplerList.size()];
			for (int i = 0; i < samplers.length; ++i) {
				samplers[i] = new Sampler(tempSamplerList.get(i));
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		id = glCreateShader(stype);
		glShaderSource(id, shaderSource);
		glCompileShader(id);
		if(glGetShaderi(id, GL_COMPILE_STATUS) != 1) {
			throw new RuntimeException("Failed to compile shader: " + path + "! " + glGetShaderInfoLog(id));
		}
	}

	/**
	 * get the shader handle
	 * @return shader handle
	 */
	public int getID() {
		return id;
	}

	/**
	 * deletes the shader
	 */
	public void destroy() {
		glDeleteShader(id);
		for (int i = 0; i < shaderIDs.size(); ++i) {
			if (shaderIDs.get(i) == id) {
				shaderIDs.remove(i);
			}
		}
	}
}