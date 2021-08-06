layout (binding = 3) readonly buffer Target { vec4 pos[]; };

uniform mat4 matrix;

void main() {
	gl_Position = matrix * pos[gl_VertexID];
}