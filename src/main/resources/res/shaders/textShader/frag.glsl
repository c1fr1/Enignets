#version 330 core

in vec2 texCoords;

out vec4 color;

uniform sampler2D texSampler;

void main() {
    color = texture(texSampler, texCoords);
}