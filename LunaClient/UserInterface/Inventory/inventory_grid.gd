extends GridContainer

func _ready():
	Inventory.items_changed.connect(_on_items_changed)
	
	update_grid()


func update_slot(id: int) -> void:
	var item: Item = Inventory.item_slots[id]
	var slot_node: Node = get_child(id)
	
	if slot_node.has_method("display_item"):
		slot_node.display_item(item)


func update_grid() -> void:
	for slot in Inventory.SIZE:
		update_slot(slot)


func _on_items_changed(slots: Array) -> void:
	for slot in slots:
		update_slot(slot)


# Dropping items is now handled from the inventory_ui
#func _input(event):
#	# Handle dropping an item
#	if event is InputEventMouseButton and Inventory.drag_data is Dictionary:
#		# Make sure the drag data contains an item
#		if not Inventory.drag_data.has("slot"):
#			return
#
#		# Make sure the event position is outside of this inventory grid
#		var grid_rect: Rect2 = get_global_rect()
#		if grid_rect.has_point(event.position):
#			return
#
#		if event.is_released() and event.button_index == MOUSE_BUTTON_LEFT:
#			Inventory.drop_item(Inventory.drag_data.slot)
#			Inventory.drag_data = null
