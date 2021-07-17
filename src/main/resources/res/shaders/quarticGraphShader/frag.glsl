#version 330 core

in vec2 coordinates;

out vec4 color;

uniform float a;
uniform float b;
uniform float c;
uniform float d;
uniform float e;
uniform float v;

float evaldydx(float x) {
	float x2 = x * x;
	return 4 * a * x2 * x + 3 * b * x2 + 2 * c * x + d;
}

float dy(float x, float width) {
	float deriv = evaldydx(x);
	return width * sqrt(deriv * deriv + 1);
}

float eval(float x) {
	float x2 = x * x;
	return a * x2 * x2 + b * x2 * x + c * x2 + d * x + e;
}

void main() {
	float dy = dy(coordinates.x, 0.05);
	float val = eval(coordinates.x);
	float lower = val - dy;
	float upper = val + dy;
	if (coordinates.y > lower && coordinates.y < upper) {
		color = vec4(1, 1, 1, 1);
	} else if (abs(coordinates.x - v) < 0.01) {
		color = vec4(1, 0, 0, 1);
	} else if (abs(coordinates.y) < 25) {
		color = vec4(0, 1, 0, 1);
	} else if (coordinates.x > 0 && coordinates.x < 1) {
		color = vec4(0.05, 0.05, 0.05, 1);
	} else {
		color = vec4(0.25, 0.25, 0.25, 1);
	}
}