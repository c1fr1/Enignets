#version 330 core

in vec2 lerpVals;

out vec4 color;

uniform float v;
uniform float u;

void main() {
    if (abs(lerpVals.x - v) < 0.005) {
        if (abs(lerpVals.y - u) < 0.005) {
            color = vec4(0, 1, 0, 1);
        } else {
            color = vec4(1, 0, 0, 1);
        }
    } else {
        color = vec4(lerpVals.x, 1, 1, 1);
    }
}
