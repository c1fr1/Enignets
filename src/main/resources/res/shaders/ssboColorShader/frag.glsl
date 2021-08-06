#version 330 core

out vec4 color;

uniform vec3 ocolor;

void main() {
    color = vec4(ocolor, 1);
}
