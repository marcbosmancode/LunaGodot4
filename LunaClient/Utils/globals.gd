extends Node

signal just_logged_in

# User Interface
var ui_positions: Dictionary = {
	0: Vector2(275, 152),
	1: Vector2(275, 140),
}

# Networking
var logged_in := false:
	set(new_value):
		logged_in = new_value
		just_logged_in.emit()

# Gameplay
var can_move: bool = true
