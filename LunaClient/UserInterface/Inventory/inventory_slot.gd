extends CenterContainer

@onready var highlight = $Highlight
@onready var item_texture = $ItemTexture
@onready var quantity_label = $ItemTexture/QuantityLabel

var selected := false:
	set(new_value):
		selected = new_value
		
		if selected == true:
			highlight.show()
		else:
			highlight.hide()

func _ready():
	MessageBus.show_item_details.connect(_on_show_item_details)


func display_item(item: Item) -> void:
	if item is Item:
		item_texture.texture = item.texture
		quantity_label.text = str(item.quantity)
		quantity_label.show()
	else:
		item_texture.texture = null
		quantity_label.hide()


func _get_drag_data(_at_position):
	var slot = get_index()
	var item: Item = Inventory.get_item(slot)
	
	if item is Item:
		var data: Dictionary = {}
		data.item = item
		data.slot = slot
		
		Inventory.drag_data = data
		create_drag_preview(item)
		return data


func create_drag_preview(item: Item) -> void:
	var drag_preview: TextureRect = TextureRect.new()
	
	drag_preview.texture = item.texture
	drag_preview.modulate.a = 0.5
	
	set_drag_preview(drag_preview)


func _can_drop_data(_at_position, data):
	# Check if the drag data contains an item
	if data is Dictionary:
		if data.has("slot"):
			return true
	
	return false


func _drop_data(_at_position, data):
	Inventory.drag_data = null
	
	var target_slot: int = get_index()
	# Make sure the item isn't moved to the slot it's already in
	if data.slot == target_slot:
		return
	
	Inventory.swap_items(data.slot, target_slot)


func _on_gui_input(event):
	if event is InputEventMouseButton:
		if event.is_pressed() and event.button_index == MOUSE_BUTTON_LEFT:
			var slot = get_index()
			var item = Inventory.get_item(slot)
			
			MessageBus.show_item_details.emit(item, slot)


func _on_show_item_details(_item: Item, slot: int) -> void:
	# Highlight the slot that has their item details shown
	if get_index() == slot:
		selected = true
	else:
		selected = false
