#version 430 core

layout(local_size_x = 1) in;

layout(binding=0) buffer Source
{
	vec4 positions[];
};

layout(binding=1) buffer Target
{
	vec4 nexPositions[];
};
void main() {
	uint i = gl_GlobalInvocationID.x;
	nexPositions[i] = positions[i] + 1.0;
}