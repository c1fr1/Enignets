#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textCoords;

out vec2 texCoords;

uniform mat4 matrix;

void main() {
	gl_Position = matrix * vec4(vertices, 1);
    texCoords = textCoords;
}