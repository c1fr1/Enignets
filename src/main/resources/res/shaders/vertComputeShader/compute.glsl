layout (local_size_x = 1) in;
layout (std430) buffer;

uniform mat4 matrix;

layout (binding = 0) readonly buffer SourcePos { vec3 pos[]; };
layout (binding = 1) readonly buffer SourceNormal { vec3 normal[]; };

layout (binding = 2) writeonly buffer PosTarget { vec4 oPos[]; };
layout (binding = 3) writeonly buffer NormalTarget { vec4 oNormal[]; };

void main() {
	uint vID = gl_GlobalInvocationID.x;
	oPos[vID] = matrix * vec4(pos[vID], 1.0);
	oNormal[vID] = matrix * vec4(normal[vID], 1.0);
}