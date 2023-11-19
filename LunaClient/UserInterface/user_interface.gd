extends CanvasLayer

var ok_popup = preload("res://UserInterface/Popups/ok_popup.tscn")
var message_target_selection = preload("res://UserInterface/ChatSystem/private_message_popup.tscn")
var mts_instance: Node = null

@onready var inventory_ui: Control = $InventoryUI

func show_message(group: int, message: String, sender: String = "") -> void:
	MessageBus.show_message.emit(group, message, sender)


func select_message_target() -> void:
	if not is_instance_valid(mts_instance):
		mts_instance = message_target_selection.instantiate()
		add_child(mts_instance)


func show_ok_popup(message: String) -> void:
	var op_instance: OkPopup = ok_popup.instantiate()
	op_instance.content = message
	add_child(op_instance)


func toggle_ui_visibility(toggleable_ui: Control) -> void:
	if Globals.can_move == false:
		return
	if Globals.logged_in == false:
		return
	
	if toggleable_ui.is_visible_in_tree():
		toggleable_ui.hide()
	else:
		toggleable_ui.show()


func _input(event):
	if event.is_action_pressed("open_inventory"):
		toggle_ui_visibility(inventory_ui)
