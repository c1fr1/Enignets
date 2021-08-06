const int MAX_JOINTS = 50;
const int MAX_WEIGHTS = 4;

layout (location = 0) in vec3 pos;
layout (location = 3) in ivec4 boneIndex;
layout (location = 4) in vec4 boneWeight;

uniform mat4 matrix;
uniform mat4 jointsMatrix[50];

out vec4 weightedSums;

void main() {
	vec3 colors[4];
	colors[0] = vec3(1, 1, 1);
	colors[1] = vec3(1, 0, 0);
	colors[2] = vec3(0, 1, 0);
	colors[3] = vec3(0, 0, 1);
	weightedSums = vec4(0, 0, 0, 0);

	vec4 oPos = vec4(0, 0, 0, 0);
	int count = 0;
	for (int i = 0; i < MAX_WEIGHTS; i++) {
		weightedSums += vec4(colors[boneIndex[i]], 1) * boneWeight[i];
		float weight = boneWeight[i];
		if (weight > 0) {
			count++;
			vec4 tmpPos = jointsMatrix[boneIndex[i]] * vec4(pos, 1.0);
			oPos += weight * tmpPos;
		}
	}
	if (count == 0) {
		oPos = vec4(pos, 1.0);
	}
	gl_Position = matrix * oPos;
}