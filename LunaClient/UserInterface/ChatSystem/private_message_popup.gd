extends OkPopup

@onready var name_input = $LineEdit

func _on_ok_button_pressed():
	if name_input.text != "":
		var message_target: String = name_input.text
		
		if message_target.contains(" "):
			queue_free()
			return
		
		UserInterface.set_private_message_target(message_target)
		UserInterface.show_message(0, "Private messages will be sent to " + message_target, "System")
	
	queue_free()
