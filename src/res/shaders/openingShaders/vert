#version 400 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textCoords;
layout (location = 2) in vec3 normals;

out vec2 texCoord;
out vec3 vertexPos;

uniform mat4 matrix;

void main() {
	gl_Position = matrix * vec4(vertices, 1);
	vertexPos = vertices;
}