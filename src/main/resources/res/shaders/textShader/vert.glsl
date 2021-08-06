layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textCoords;
out vec2 texCoords;

uniform mat4 matrix;
uniform mat4 tcMat;

void main() {
	gl_Position = matrix * vec4(vertices, 1);
    texCoords = (tcMat * vec4(textCoords, 0, 1)).xy;
}