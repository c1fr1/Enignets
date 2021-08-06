layout (location = 0) in vec4 pos;

uniform mat4 matrix;

void main() {
	gl_Position = matrix * pos;
}