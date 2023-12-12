extends CanvasLayer

var ok_popup = preload("res://UserInterface/Popups/ok_popup.tscn")

var message_target_selection = preload("res://UserInterface/ChatSystem/private_message_popup.tscn")
var mts_instance: Node = null

var assign_hotkey_popup = preload("res://UserInterface/Hotbar/assign_hotkey_popup.tscn")
var ahp_instance: Node = null

func show_message(group: int, message: String, sender: String = "") -> void:
	MessageBus.show_message.emit(group, message, sender)


func show_ok_popup(message: String) -> void:
	var op_instance: OkPopup = ok_popup.instantiate()
	op_instance.content = message
	add_child(op_instance)


func select_message_target() -> void:
	if not is_instance_valid(mts_instance):
		mts_instance = message_target_selection.instantiate()
		add_child(mts_instance)


func assign_hotkey(action_type: int, action_id: int) -> void:
	if not is_instance_valid(ahp_instance):
		ahp_instance = assign_hotkey_popup.instantiate()
		ahp_instance.action_type = action_type
		ahp_instance.action_id = action_id
		add_child(ahp_instance)
