class_name BasePopup
extends Draggable

@export var show_close_button: bool = true
@export var header := "Notice":
	set(new_value):
		header = new_value
		if self.is_inside_tree():
			header_label.text = new_value

@onready var header_label = $Header
@onready var close_button = $CloseButton

func _ready() -> void:
	# Extend the ready function from Draggable
	super()
	
	header_label.text = header
	
	if show_close_button:
		close_button.show()

func _on_close_button_pressed() -> void:
	queue_free()
