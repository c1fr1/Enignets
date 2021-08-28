layout (location = 0) in vec3 vertices;
layout (location = 1) in vec3 color;

out vec4 vertcolor;

uniform mat4 matrix;

void main() {
	gl_Position = matrix * vec4(vertices, 1);
	vertcolor = vec4(color, 1);
}