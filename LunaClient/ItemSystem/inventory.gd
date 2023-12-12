extends Node

signal items_changed(slots)

const SIZE: int = 80
const ITEM_PATH: String = "res://ItemSystem/Items/item_%s.tres"

var item_slots: Array[Item] = []

func _ready():
	item_slots.resize(SIZE)


func clear_items() -> void:
	item_slots.clear()
	item_slots.resize(SIZE)


func load_item(item_id: int) -> Item:
	var path: String = ITEM_PATH % item_id
	
	if ResourceLoader.exists(path):
		var target_item = load(path)
		if target_item is Item:
			return target_item
	
	return null


func set_item(slot: int, item: Item, quantity: int = 1) -> void:
	var new_item = null
	
	# Verify item is not null
	if item is Item:
		new_item = item.duplicate()
		new_item.quantity = quantity
	
	item_slots[slot] = new_item
	
	items_changed.emit([slot])


func set_item_with_id(slot: int, item_id: int, quantity: int = 1) -> void:
	var target_item: Item = load_item(item_id)
	set_item(slot, target_item, quantity)


func get_item(slot: int) -> Item:
	return item_slots[slot]


func get_item_id(slot: int) -> int:
	var item = item_slots[slot]
	if item is Item:
		return item.id
	else:
		return -1


func drop_item(slot: int) -> void:
	Client.send_data(PacketWriter.write_alter_inventory(slot, -1))


func swap_items(slot_1: int, slot_2: int) -> void:
	Client.send_data(PacketWriter.write_alter_inventory(slot_1, slot_2))


func consume_item(slot: int) -> void:
	# Make sure the item in the slot is a consumable first
	var item: Item = item_slots[slot]
	
	if item is Item:
		# Item type 1 is consumable
		if item.type == 1:
			Client.send_data(PacketWriter.consume_item(slot))


func consume_item_id(item_id: int) -> void:
	var current_slot = 0
	
	for slot in item_slots:
		if slot is Item:
			if slot.id == item_id:
				consume_item(current_slot)
				return
		
		current_slot += 1


func get_item_quantity(item_id: int) -> int:
	var quantity: int = 0
	
	for slot in item_slots:
		if slot is Item:
			if slot.id == item_id:
				quantity += slot.quantity
	
	return quantity
