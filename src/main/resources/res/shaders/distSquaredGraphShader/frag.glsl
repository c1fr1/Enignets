in vec2 coordinates;

out vec4 color;

uniform vec3 ea;
uniform vec3 ead;
uniform vec3 eb;
uniform vec3 ebd;
uniform vec3 oa;
uniform vec3 oad;
uniform vec3 ob;
uniform vec3 obd;
uniform float lv;

float evaldydx(float x) {
	vec3 ed = (ead + x * (ebd - ead));
	vec3 od = (oad + x * (obd - oad));
	vec3 ded = ebd - ead;
	vec3 dod = obd - oad;
	vec3 e = (ea + x * (eb - ea));
	vec3 o = (oa + x * (ob - oa));
	vec3 de = eb - ea;
	vec3 dO = ob - oa;

	float lower = dot(ed, ed) * dot(od, od) - dot(ed, od) * dot(ed, od);
	float dlower = 2 * ((dot(ed, ded) * dot(od, od) + dot(ed, ed) * dot(od, dod)) - dot(ed, od) * (dot(ed, dod) + dot(ded, od)));
	float uo = dot(od, od) * dot(o - e, ed) - dot(o - e, od) * dot(ed, od);
	float vo = dot(ed, ed) * dot(e - o, od) - dot(e - o, ed) * dot(od, ed);

	float duo = 2 * dot(dod, od) * dot(o - e, ed) + dot(od, od) * (dot(o - e, ded) + dot(dO - de, ed)) - (dot(dO - de, od) + dot(o - e, dod)) * dot(ed, od) - dot(o - e, od) * (dot(ed, dod) + dot(ded, od));
	float dvo = 2 * dot(ded, ed) * dot(e - o, od) + dot(ed, ed) * (dot(e - o, dod) + dot(de - dO, od)) - (dot(de - dO, ed) + dot(e - o, ded)) * dot(od, ed) - dot(e - o, ed) * (dot(od, ded) + dot(dod, ed));


	float u = uo / lower;
	float du = (lower * duo - uo * dlower) / (lower * lower);
	float v = vo / lower;
	float dv = (lower * dvo - vo * dlower) / (lower * lower);

	vec3 ept = e + ed * uo / lower;
	vec3 opt = o + od * vo / lower;
	vec3 dept = de + ded * u + ed * du;
	vec3 dopt = dO + dod * v + od * dv;

	return 2 * dot(ept - opt, dept - dopt);
}

float eval(float x) {
	vec3 ed = (ead + x * (ebd - ead));
	vec3 od = (oad + x * (obd - oad));
	vec3 e = (ea + x * (eb - ea));
	vec3 o = (oa + x * (ob - oa));

	float lower = dot(ed, ed) * dot(od, od) - dot(ed, od) * dot(ed, od);
	float uo = dot(od, od) * dot(o - e, ed) - dot(o - e, od) * dot(ed, od);
	float vo = dot(ed, ed) * dot(e - o, od) - dot(e - o, ed) * dot(od, ed);

	vec3 ept = e + ed * uo / lower;
	vec3 opt = o + od * vo / lower;

	return dot(ept - opt, ept - opt);
}

float evalSimp(float x) {
	vec3 ed = (ead + x * (ebd - ead));
	vec3 od = (oad + x * (obd - oad));
	vec3 e = (ea + x * (eb - ea));
	vec3 o = (oa + x * (ob - oa));

	float lower = dot(ed, ed) * dot(od, od) - dot(ed, od) * dot(ed, od);
	float uo = dot(od, od) * dot(o - e, ed) - dot(o - e, od) * dot(ed, od);
	float vo = dot(ed, ed) * dot(e - o, od) - dot(e - o, ed) * dot(od, ed);

	vec3 ept = e * lower + ed * uo;
	vec3 opt = o * lower + od * vo;

	//return dot(ept - opt, ept - opt);
	return (ept - opt).x;
}

float evaldydxSimp(float x) {
	vec3 ed = (ead + x * (ebd - ead));
	vec3 od = (oad + x * (obd - oad));
	vec3 ded = ebd - ead;
	vec3 dod = obd - oad;
	vec3 e = (ea + x * (eb - ea));
	vec3 o = (oa + x * (ob - oa));
	vec3 de = eb - ea;
	vec3 dO = ob - oa;

	float lower = dot(ed, ed) * dot(od, od) - dot(ed, od) * dot(ed, od);
	float dlower = 2 * ((dot(ed, ded) * dot(od, od) + dot(ed, ed) * dot(od, dod)) - dot(ed, od) * (dot(ed, dod) + dot(ded, od)));
	float uo = dot(od, od) * dot(o - e, ed) - dot(o - e, od) * dot(ed, od);
	float vo = dot(ed, ed) * dot(e - o, od) - dot(e - o, ed) * dot(od, ed);

	float duo = 2 * dot(dod, od) * dot(o - e, ed) + dot(od, od) * (dot(o - e, ded) + dot(dO - de, ed)) - (dot(dO - de, od) + dot(o - e, dod)) * dot(ed, od) - dot(o - e, od) * (dot(ed, dod) + dot(ded, od));
	float dvo = 2 * dot(ded, ed) * dot(e - o, od) + dot(ed, ed) * (dot(e - o, dod) + dot(de - dO, od)) - (dot(de - dO, ed) + dot(e - o, ded)) * dot(od, ed) - dot(e - o, ed) * (dot(od, ded) + dot(dod, ed));

	vec3 ept = e * lower + ed * uo;
	vec3 opt = o * lower + od * vo;
	vec3 dept = e * dlower + de * lower + ed * duo + ded * uo;
	vec3 dopt = o * dlower + dO * lower + od * dvo + dod * vo;

	//return 2 * dot(ept - opt, dept - dopt);
	return (dept - dopt).x;
}

float dy(float x, float width) {
	float deriv = evaldydx(x);
	return width * sqrt(deriv * deriv + 1);
}

float dySimp(float x, float width) {
	float deriv = evaldydxSimp(x);
	return width * sqrt(deriv * deriv + 1);
}

void main() {
	float dy = dySimp(coordinates.x, 0.05);
	float val = evalSimp(coordinates.x);
	float lower = val - dy;
	float upper = val + dy;
	if (coordinates.y > lower && coordinates.y < upper) {
		color = vec4(1, 1, 1, 1);
	} else if (abs(coordinates.x - lv) < 0.01) {
		color = vec4(1, 0, 0, 1);
	} else if (abs(coordinates.y) < 0.025) {
		color = vec4(0, 1, 0, 1);
	} else if (coordinates.x > 0 && coordinates.x < 1) {
		color = vec4(0.05, 0.05, 0.05, 1);
	} else {
		color = vec4(0.25, 0.25, 0.25, 1);
	}
}