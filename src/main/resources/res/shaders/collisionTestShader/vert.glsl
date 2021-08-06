layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 lerpVal;

out vec2 lerpVals;

uniform mat4 matrix;

void main() {
	gl_Position = matrix * vec4(vertices, 1);
	lerpVals = lerpVal;
}