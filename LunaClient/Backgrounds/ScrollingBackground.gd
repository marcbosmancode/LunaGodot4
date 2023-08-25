extends ParallaxBackground

@export var scroll_speed: int = 10

func _process(delta):
	scroll_offset.x += scroll_speed * delta
