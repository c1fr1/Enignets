in float brightness;

out vec4 color;

uniform vec3 ocolor;

void main() {
    color = vec4(ocolor * brightness, 1);
}
