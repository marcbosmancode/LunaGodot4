extends Node

signal just_logged_in

# User Interface
var ui_positions: Dictionary = {
	0: Vector2(250, 152),
	1: Vector2(250, 140),
}

# Networking
var logged_in := false:
	set(new_value):
		logged_in = new_value
		emit_signal("just_logged_in")
