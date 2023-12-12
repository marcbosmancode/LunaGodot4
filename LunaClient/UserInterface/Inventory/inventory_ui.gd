extends Control

@onready var item_details_popup = $ItemDetailsPopup

func _on_close_button_pressed():
	hide()


func _can_drop_data(_at_position, data):
	# Check if the drag data contains an item
	if data is Dictionary:
		if data.has("slot"):
			return true
	
	return false


func _drop_data(_at_position, data):
	# If an item is dragged outside of the inventory grid the item should be dropped
	Inventory.drop_item(data.slot)


func _input(event):
	if event.is_action_pressed("open_inventory"):
		if Globals.can_move == false or Globals.logged_in == false:
			return
		
		if is_visible_in_tree():
			hide()
		else:
			show()
