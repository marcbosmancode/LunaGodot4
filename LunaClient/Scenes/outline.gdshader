shader_type canvas_item;

uniform vec4 outline_color: source_color;

void fragment() {
	float a = texture(TEXTURE, UV).a;
	if (a == 0.0) {
		COLOR = outline_color;
	}
}
