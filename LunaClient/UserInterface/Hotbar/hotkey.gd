extends NinePatchRect

@onready var action_texture = $MarginContainer/ActionTexture
@onready var key_label = $KeyLabel
@onready var quantity_label = $QuantityLabel

var key_index: int = 0
var key_action: HotkeyAction = null
var action_string: String = ""

func _ready():
	Globals.hotkey_action_changed.connect(_on_hotkey_action_changed)
	MessageBus.input_key_changed.connect(_on_input_key_changed)
	Inventory.items_changed.connect(_on_items_changed)
	
	key_index = get_index() + 1
	action_string = "hotkey_" + str(key_index)
	
	update_input_key()
	update_action(key_index)


func update_action(hotkey_id: int) -> void:
	key_action = Globals.hotkey_actions.get(hotkey_id)
	
	if key_action is HotkeyAction:
		if key_action.action_type == Enums.HotkeyType.ITEM:
			# Load the item and show it in the hotkey
			var item: Item = Inventory.load_item(key_action.action_id)
			if item is Item:
				action_texture.texture = item.texture
				quantity_label.text = str(Inventory.get_item_quantity(item.id))
				quantity_label.show()
		
		elif key_action.action_type == Enums.HotkeyType.SKILL:
			# TODO load the skill texture
			action_texture.texture = null
			quantity_label.hide()
	else:
		# Clear the slot
		action_texture.texture = null
		quantity_label.hide()


func update_input_key() -> void:
	# Get the input keys associated with the action
	var events = InputMap.action_get_events(action_string)
	
	# Display the first input key
	if events.size() > 0:
		key_label.text = events[0].as_text()
	else:
		key_label.text = ""


func _on_hotkey_action_changed(hotkey_id: int, _new_action: HotkeyAction):
	if key_index == hotkey_id:
		update_action(hotkey_id)


func _on_items_changed(_slots):
	if key_action is HotkeyAction:
		if key_action.action_type == Enums.HotkeyType.ITEM:
			# Clamp the maximum quantity shown on the label
			var item_quantity = clamp(Inventory.get_item_quantity(key_action.action_id), 0, 999)
			quantity_label.text = str(item_quantity)


func _on_input_key_changed(action, _event):
	if action == action_string:
		update_input_key()
