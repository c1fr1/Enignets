layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textCoords;

out vec2 coordinates;

uniform mat4 matrix;

void main() {
	gl_Position = matrix * vec4(vertices, 1);
    coordinates = textCoords;
	coordinates.x = (coordinates.x - 0.5) * 5;
	coordinates.y = (coordinates.y - 0.5) * 10;
}