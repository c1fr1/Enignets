#version 400 core

in vec4 vecColor;

out vec4 color;

void main() {
    if (gl_FrontFacing) {
        color = vecColor;
    }else {
        color = vec4(0, 0, 1, 1);
    }
}