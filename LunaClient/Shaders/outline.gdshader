shader_type canvas_item;

uniform vec4 outline_color: source_color;

void fragment() {
	float alpha = texture(TEXTURE, UV).a;
	vec2 offset_x = vec2(TEXTURE_PIXEL_SIZE.x, 0);
	vec2 offset_y = vec2(0, TEXTURE_PIXEL_SIZE.y);
	
	if (alpha <= 0.0001) {
		if (texture(TEXTURE, UV - offset_x).a > 0.0001) {
			COLOR = outline_color;
		}
		if (texture(TEXTURE, UV + offset_x).a > 0.0001) {
			COLOR = outline_color;
		}
		if (texture(TEXTURE, UV - offset_y).a > 0.0001) {
			COLOR = outline_color;
		}
		if (texture(TEXTURE, UV + offset_y).a > 0.0001) {
			COLOR = outline_color;
		}
	}
}
