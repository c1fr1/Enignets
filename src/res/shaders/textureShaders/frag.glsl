#version 400 core

uniform sampler2D textureSampler;

in vec3 verts;
in vec2 texCoords;

out vec4 color;

void main() {
    color = texture(textureSampler, vec2(texCoords.x, 1-texCoords.y));
}