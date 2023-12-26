extends Control

# Allow items to be dragged and dropped
func _can_drop_data(_at_position, data):
	# Check if the drag data contains an item
	if data is Dictionary:
		if data.has("slot"):
			return true
	
	return false


func _drop_data(_at_position, data):
	# If an item is dragged outside of the inventory grid the item should be dropped
	Inventory.drop_item(data.slot)
