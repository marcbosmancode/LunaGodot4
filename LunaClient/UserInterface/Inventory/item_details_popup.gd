extends NinePatchRect

var item_slot: int = -1

@onready var item_texture = $MarginContainer/VBoxContainer/MainInfo/ItemPreview/ItemTexture
@onready var item_name_label = $MarginContainer/VBoxContainer/MainInfo/ItemName
@onready var item_desc_label = $MarginContainer/VBoxContainer/ItemDescription
@onready var use_button = $MarginContainer/VBoxContainer/Buttons/UseButton

func _ready():
	MessageBus.show_item_details.connect(_on_show_item_details)
	Inventory.items_changed.connect(_on_items_changed)


func _on_show_item_details(item: Item, slot: int) -> void:
	# Make sure item isn't null
	if item is Item:
		item_slot = slot
		
		item_texture.texture = item.texture
		item_name_label.text = item.name
		item_desc_label.text = item.description
		
		# If the item is a consumable add use and keybind buttons
		if item.type == 1:
			use_button.show()
		else:
			use_button.hide()
		
		show()
	else:
		item_slot = -1
		hide()


func _on_items_changed(slots: Array) -> void:
	for slot in slots:
		if slot == item_slot:
			var item: Item = Inventory.get_item(slot)
			MessageBus.show_item_details.emit(item, slot)


func _on_use_button_pressed():
	if not item_slot == -1:
		Inventory.consume_item(item_slot)


func _on_drop_button_pressed():
	if not item_slot == -1:
		Inventory.drop_item(item_slot)
