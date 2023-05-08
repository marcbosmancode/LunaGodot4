shader_type canvas_item;

void fragment() {
	float a = texture(TEXTURE, UV).a;
	COLOR.a = step(0.1, a);
}
