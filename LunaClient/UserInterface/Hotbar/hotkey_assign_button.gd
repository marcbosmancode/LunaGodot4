extends Button

signal hotkey_pressed(index)

@onready var action_texture = $MarginContainer/ActionTexture
@onready var key_label = $KeyLabel

var key_index: int = 0
var action_string: String = ""

func _ready():
	Globals.hotkey_action_changed.connect(_on_hotkey_action_changed)
	
	key_index = get_index() + 1
	action_string = "hotkey_" + str(key_index)
	
	update_input_key()
	update_action(key_index)


func update_action(hotkey_id: int) -> void:
	var key_action = Globals.hotkey_actions.get(hotkey_id)
	if key_action is HotkeyAction:
		if key_action.action_type == Enums.HotkeyType.ITEM:
			# Load the item and show it in the hotkey
			var item: Item = Inventory.load_item(key_action.action_id)
			if item is Item:
				action_texture.texture = item.texture
		
		if key_action.action_type == Enums.HotkeyType.SKILL:
			# TODO load the skill texture
			action_texture.texture = null
	else:
		# Clear the slot
		action_texture.texture = null


func _on_pressed():
	hotkey_pressed.emit(get_index() + 1)


func _on_gui_input(event):
	if event is InputEventMouseButton:
		if event.button_index == MOUSE_BUTTON_RIGHT and event.is_pressed():
			var hotkey_id = get_index() + 1
			Globals.change_hotkey_action(hotkey_id, null)


func _on_hotkey_action_changed(hotkey_id: int, _new_action: HotkeyAction):
	var this_hotkey_id = get_index() + 1
	if this_hotkey_id == hotkey_id:
		update_action(this_hotkey_id)


func update_input_key() -> void:
	# Get the input keys associated with the action
	var events = InputMap.action_get_events(action_string)
	
	# Display the first input key
	if events.size() > 0:
		key_label.text = events[0].as_text()
	else:
		key_label.text = ""
