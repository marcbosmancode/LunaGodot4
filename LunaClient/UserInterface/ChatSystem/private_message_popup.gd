extends OkPopup

@onready var name_input = $LineEdit

func _ready():
	super()
	
	name_input.grab_focus()
	Globals.can_move = false


func _on_ok_button_pressed():
	Globals.can_move = true
	
	if name_input.text != "":
		var message_target: String = name_input.text
		
		if message_target.contains(" "):
			queue_free()
			return
		
		UserInterface.set_private_message_target(message_target)
		UserInterface.show_message(0, "Private messages will be sent to " + message_target, "System")
	
	queue_free()


func _on_close_button_pressed() -> void:
	Globals.can_move = true
	queue_free()
