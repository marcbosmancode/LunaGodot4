class_name OkPopup
extends BasePopup

@export var content := "":
	set(new_value):
		content = new_value
		if self.is_inside_tree():
			content_label.text = new_value

@onready var content_label = $Content

func _ready() -> void:
	super()
	
	content_label.text = content


func _on_ok_button_pressed():
	# Override this method to add functionality
	queue_free()
