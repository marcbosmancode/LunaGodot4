extends Camera2D

const ZOOM_FACTOR: float = 2.0

func _ready():
	zoom = Vector2(ZOOM_FACTOR, ZOOM_FACTOR)
