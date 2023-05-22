shader_type canvas_item;

uniform float step_range: hint_range(0.0, 1.0) = 0.1;

void fragment() {
	float a = texture(TEXTURE, UV).a;
	COLOR.a = step(step_range, a);
}
