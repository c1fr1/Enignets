#version 400 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 txtPos;

out vec3 verts;
out vec2 texCoords;

void main() {
	gl_Position = vec4(vertices, 1);
	verts = vertices;
	texCoords = txtPos;
}