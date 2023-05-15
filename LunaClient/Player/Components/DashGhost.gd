extends Sprite2D

func _ready():
	var tween = create_tween().set_trans(Tween.TRANS_CUBIC)
	tween.tween_property(self, "self_modulate:a", 0, 0.5)
	tween.finished.connect(_on_tween_finished)


func _on_tween_finished() -> void:
	queue_free()
