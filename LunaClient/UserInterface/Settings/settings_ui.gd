extends Control

@onready var input_button_scene = preload("res://UserInterface/Settings/input_button.tscn")
@onready var action_container = $SettingsBox/MarginContainer/VBoxContainer/InputSettingsContainer/MarginContainer/ActionContainer

var is_remapping: bool = false
var action_to_remap = null
var remapping_button = null

var input_actions: Dictionary = {
	"move_up": "Move up",
	"move_down": "Move down",
	"move_left": "Move left",
	"move_right": "Move right",
	"jump": "Jump",
	"dash": "Dash",
	"teleport": "Teleport",
	"basic_attack": "Basic attack",
	"interact": "Interact",
	"open_inventory": "Inventory",
	"open_settings": "Settings",
	"hotkey_1": "Hotkey 1",
	"hotkey_2": "Hotkey 2",
	"hotkey_3": "Hotkey 3",
	"hotkey_4": "Hotkey 4",
	"hotkey_5": "Hotkey 5",
	"hotkey_6": "Hotkey 6",
	"hotkey_7": "Hotkey 7",
	"hotkey_8": "Hotkey 8",
	"hotkey_9": "Hotkey 9",
}

func _ready():
	create_action_list()


func _input(event):
	if is_remapping:
		if (event is InputEventKey or (event is InputEventMouseButton and event.is_pressed())):
			if event is InputEventMouseButton and event.double_click:
				event.double_click = false
				
			InputMap.action_erase_events(action_to_remap)
			InputMap.action_add_event(action_to_remap, event)
			update_action_list(remapping_button, event)
			
			MessageBus.input_key_changed.emit(action_to_remap, event)
			
			is_remapping = false
			action_to_remap = null
			remapping_button = null
			
			Globals.can_move = true
			
			accept_event()


func create_action_list() -> void:
	# Clear the input buttons already loaded in the UI
	InputMap.load_from_project_settings()
	for action in action_container.get_children():
		action.queue_free()
	
	for action in input_actions:
		var button = input_button_scene.instantiate()
		
		# Change the button text
		button.action_text = input_actions[action]
		var events = InputMap.action_get_events(action)
		if events.size() > 0:
			MessageBus.input_key_changed.emit(action, events[0])
			button.key_text = events[0].as_text()
		else:
			MessageBus.input_key_changed.emit(action, null)
		
		action_container.add_child(button)
		button.pressed.connect(_on_input_button_pressed.bind(button, action))


func _on_input_button_pressed(button, action):
	if is_remapping == false:
		is_remapping = true
		action_to_remap = action
		remapping_button = button
		button.key_text = "Press key to bind"
		Globals.can_move = false


func update_action_list(button, event):
	button.key_text = event.as_text()


func _on_reset_button_pressed():
	create_action_list()
