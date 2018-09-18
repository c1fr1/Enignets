#version 400 core

layout (location = 0) in vec3 vertices;

out vec4 vecColor;

uniform mat4 matrix;
uniform int vertA;
uniform int vertB;
uniform int vertC;

void main() {
	gl_Position = matrix * vec4(vertices, 1);
	if (gl_VertexID == vertA || gl_VertexID == vertB || gl_VertexID == vertC) {
	    vecColor = vec4(1, 0, 0, 1);
	}else {
	    vecColor = vec4(0, 1, 0, 1);
	}
}