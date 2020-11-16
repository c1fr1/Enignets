#version 330 core

in vec3 vertexPos;
in vec2 texCoord;

out vec4 color;

uniform float time;
uniform sampler2D textureSampler;
uniform vec2 dims;

float rand(vec2 co){
    return fract(sin(dot(co, vec2(12.9897,72.233))) * 43958.5453);
}

void main() {
    vec4 ocolor = vec4(0, 0, 1, 1);
    bool xp = mod(vertexPos.x, 1) < 0.05 || mod(vertexPos.x, 1) > 0.95;
    bool yp = mod(vertexPos.y, 1) < 0.05 || mod(vertexPos.y, 1) > 0.95;
    bool zp = mod(vertexPos.z, 1) < 0.05 || mod(vertexPos.z, 1) > 0.95;
    int total = 0;
    if (xp) {
        total = total + 1;
    }
    if (yp) {
        total = total + 1;
    }
    if (zp) {
        total = total + 1;
    }
    if (total >= 2) {
        ocolor.xyz = vec3(1, 1, 1);
    }else if (mod(gl_FragCoord.x/dims.x + gl_FragCoord.y/dims.x, 0.02) < 0.005) {
        ocolor.xyz = vec3(0, 0.25, 0.6);
    }else {
        float britness = rand(vertexPos.xz);
        ocolor.xyz = vec3(britness, britness, britness);
    }
    color = ocolor;
}