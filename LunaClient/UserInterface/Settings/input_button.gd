extends Button

@onready var action_label = $MarginContainer/HBoxContainer/ActionLabel
@onready var key_label = $MarginContainer/HBoxContainer/KeyLabel

var action_text: String = "":
	set(new_value):
		action_text = new_value
		if is_visible_in_tree():
			action_label.text = new_value
var key_text: String = "":
	set(new_value):
		key_text = new_value
		if is_visible_in_tree():
			key_label.text = new_value

func _ready():
	action_label.text = action_text
	key_label.text = key_text
