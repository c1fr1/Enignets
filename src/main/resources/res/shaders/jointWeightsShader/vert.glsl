layout (location = 0) in vec3 vertices;
layout (location = 3) in ivec4 boneIndexes;
layout (location = 4) in vec4 boneWeights;

uniform mat4 matrix;

out vec4 weightedSums;

void main() {
	gl_Position = matrix * vec4(vertices, 1);
	vec3 colors[4];
	colors[0] = vec3(1, 1, 1);
	colors[1] = vec3(1, 0, 0);
	colors[2] = vec3(0, 1, 0);
	colors[3] = vec3(0, 0, 1);
	weightedSums = vec4(colors[boneIndexes.x], 1) * boneWeights.x;
	weightedSums += vec4(colors[boneIndexes.y], 1) * boneWeights.y;
	weightedSums += vec4(colors[boneIndexes.z], 1) * boneWeights.z;
	weightedSums += vec4(colors[boneIndexes.w], 1) * boneWeights.w;
}