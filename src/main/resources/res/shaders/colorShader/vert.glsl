layout (location = 0) in vec3 vertices;

uniform mat4 matrix;

void main() {
	gl_Position = matrix * vec4(vertices, 1);
}