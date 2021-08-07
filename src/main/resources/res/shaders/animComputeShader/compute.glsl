layout (local_size_x = 1) in;
layout (std430) buffer;

const int MAX_WEIGHTS = 4;
const int MAX_JOINTS = 50;

uniform mat4 matrix;
uniform mat4 jointMatrix[MAX_JOINTS];

layout (binding = 0) readonly buffer SourcePos { vec3 pos[]; };
layout (binding = 1) readonly buffer SourceNormal { vec3 normal[]; };
layout (binding = 2) readonly buffer SourceBoneIndex { ivec4 boneIndex[]; };
layout (binding = 3) readonly buffer SourceBoneWeight { vec4 boneWeight[]; };

layout (binding = 4) writeonly buffer PosTarget { vec4 oPos[]; };
layout (binding = 5) writeonly buffer NormalTarget { vec4 oNormal[]; };

void main() {
	uint vID = gl_GlobalInvocationID.x;
	vec4 posSum = vec4(0, 0, 0, 0);
	vec4 normSum = vec4(0, 0, 0, 0);
	int count = 0;
	for (int i = 0; i < MAX_WEIGHTS; i++) {
		float weight = boneWeight[vID][i];
		if (weight > 0) {
			count++;
			vec4 tmpPos = jointMatrix[boneIndex[vID][i]] * vec4(pos[vID], 1.0);
			vec4 tmpNormal = jointMatrix[boneIndex[vID][i]] * vec4(normal[vID], 0.0);
			posSum += weight * tmpPos;
			normSum += weight * tmpNormal;
		}
	}
	if (count == 0) {
		posSum = vec4(pos[vID], 1.0);
		normSum = vec4(normal[vID], 1.0);
	}
	oPos[vID] = matrix * posSum;
	oNormal[vID] = matrix * normSum;
}