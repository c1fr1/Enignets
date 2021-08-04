#version 430 core

layout(local_size_x = 1) in;

const int MAX_WEIGHTS = 4;

uniform mat4 matrix;
uniform mat4 jointMatrix[50];

layout(binding=0) readonly buffer SourcePos { vec3 pos[]; };
layout(binding=1) readonly buffer SourceBoneIndex { ivec4 boneIndex[]; };
layout(binding=2) readonly buffer SourceBoneWeight { vec4 boneWeight[]; };

layout(binding=3) writeonly buffer Target { vec4 oPos[]; };

void main() {
	uint vID = gl_GlobalInvocationID.x;
	vec4 posSum = vec4(0, 0, 0, 0);
	int count = 0;
	for (int i = 0; i < MAX_WEIGHTS; i++) {
		float weight = boneWeight[vID][i];
		if (weight > 0) {
			count++;
			vec4 tmpPos = jointMatrix[boneIndex[vID][i]] * vec4(pos[vID], 1.0);
			posSum += weight * tmpPos;
		}
	}
	if (count == 0) {
		posSum = vec4(pos[vID], 1.0);
	}
	oPos[vID] = matrix * posSum;
}