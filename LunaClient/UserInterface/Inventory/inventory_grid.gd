extends GridContainer

func _ready():
	Inventory.items_changed.connect(_on_items_changed)
	
	update_grid()


func update_slot(id: int) -> void:
	var item: Item = Inventory.item_slots[id]
	var slot_node := get_child(id)
	
	if slot_node.has_method("display_item"):
		slot_node.display_item(item)


func update_grid() -> void:
	for slot in Inventory.SIZE:
		update_slot(slot)


func _on_items_changed(slots: Array) -> void:
	for slot in slots:
		update_slot(slot)
