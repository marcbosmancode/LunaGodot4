extends NinePatchRect

@onready var action_texture = $MarginContainer/ActionTexture
@onready var key_label = $KeyLabel
@onready var quantity_label = $QuantityLabel

var key_action: HotkeyAction = null

func _ready():
	Globals.hotkey_action_changed.connect(_on_hotkey_action_changed)
	Inventory.items_changed.connect(_on_items_changed)
	
	var key_index = get_index()
	key_label.text = Globals.hotbar_keys.get(key_index + 1)
	
	update_action(key_index + 1)


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
		
		if key_action.action_type == Enums.HotkeyType.SKILL:
			# TODO load the skill texture
			action_texture.texture = null
			quantity_label.hide()
	else:
		# Clear the slot
		action_texture.texture = null
		quantity_label.hide()


func _on_hotkey_action_changed(hotkey_id: int, _new_action: HotkeyAction):
	var this_hotkey_id = get_index() + 1
	if this_hotkey_id == hotkey_id:
		update_action(this_hotkey_id)


func _on_items_changed(_slots):
	if key_action is HotkeyAction:
		if key_action.action_type == Enums.HotkeyType.ITEM:
			# Clamp the maximum quantity shown on the label
			var item_quantity = clamp(Inventory.get_item_quantity(key_action.action_id), 0, 999)
			quantity_label.text = str(item_quantity)
