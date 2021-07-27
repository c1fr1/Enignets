#version 330 core

in vec4 weightedSums;

out vec4 color;

void main() {
    color = weightedSums;
}
