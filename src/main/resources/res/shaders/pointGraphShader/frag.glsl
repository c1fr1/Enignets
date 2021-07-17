#version 330 core

in vec2 coordinates;

out vec4 color;

uniform vec2 pt;

void main() {
	if (distance(pt, coordinates) < 0.05) {
		color = vec4(1, 1, 1, 1);
	} else {
		discard;
	}
}