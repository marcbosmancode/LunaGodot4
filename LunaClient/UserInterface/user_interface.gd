extends CanvasLayer

var ok_popup = preload("res://UserInterface/Popups/ok_popup.tscn")
var message_target_selection = preload("res://UserInterface/ChatSystem/private_message_popup.tscn")
var mts_instance: Node = null

@onready var chat_system: ChatSystem = $ChatSystem

func show_message(group: int, message: String, sender: String = "") -> void:
	chat_system.show_message(group, message, sender)


func set_private_message_target(value: String) -> void:
	chat_system.chat_target = value


func select_message_target() -> void:
	if not is_instance_valid(mts_instance):
		mts_instance = message_target_selection.instantiate()
		add_child(mts_instance)


func show_ok_popup(message: String) -> void:
	var op_instance: OkPopup = ok_popup.instantiate()
	op_instance.content = message
	add_child(op_instance)
