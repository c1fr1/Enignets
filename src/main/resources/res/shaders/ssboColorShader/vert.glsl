layout (location = 0) in vec4 pos;
layout (location = 1) in vec4 normal;

uniform mat4 matrix;
uniform vec3 playerPos;

out float brightness;

void main() {
	gl_Position = matrix * pos;
	brightness = 1 - dot(normalize(normal.xyz), playerPos);
}