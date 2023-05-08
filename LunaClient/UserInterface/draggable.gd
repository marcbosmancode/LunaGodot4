class_name Draggable
extends Control

var drag_pos = null

@export var ui_id: int
@export var is_draggable: bool = true

func _ready() -> void:
	if Globals.ui_positions.has(ui_id):
		global_position = Globals.ui_positions[ui_id]
	else:
		# Create a new entry to save the position
		Globals.ui_positions[ui_id] = Vector2(0, 0)


func _on_gui_input(event) -> void:
	if not is_draggable:
		return
	
	if event is InputEventMouseButton:
		
		# Start dragging the node
		if event.pressed:
			move_to_front()
			# global_position is the top left corner of the node
			drag_pos = get_global_mouse_position() - global_position
			
		# Stop dragging the node
		else:
			drag_pos = null
			Globals.ui_positions[ui_id] = global_position
			
	if event is InputEventMouseMotion and drag_pos != null:
		global_position = get_global_mouse_position() - drag_pos
