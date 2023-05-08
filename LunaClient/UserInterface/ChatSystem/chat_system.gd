class_name ChatSystem
extends Control

enum GROUPS {
	SYSTEM,
	NEARBY,
	WORLD,
	PRIVATE,
	PARTY,
	GUILD,
}

const COMMAND_CHAR := "/"

const CHAT_NAMES := [
	"System",
	"Nearby",
	"World",
	"Private",
	"Party",
	"Guild"
]

var chat_target := "":
	set(new_value):
		chat_target = new_value
		if current_group == GROUPS.PRIVATE:
			line_edit.placeholder_text = "to %s" % new_value

var current_group := 1:
	set(new_value):
		current_group = clamp(new_value, 1, CHAT_NAMES.size()-1)
		
		if current_group == GROUPS.PRIVATE:
			line_edit.placeholder_text = "to %s" % chat_target
		else:
			line_edit.placeholder_text = "to %s" % CHAT_NAMES[new_value]

@onready var tabs = $TabContainer
@onready var line_edit = $ChatInputRect/LineEdit

func _ready() -> void:
	line_edit.placeholder_text = "to %s" % CHAT_NAMES[current_group]
	show_message(0, "Welcome! For commands type /help")


func show_message(group: int, message: String, sender: String = "") -> void:
	for tab in tabs.get_children():
		if tab.has_method("show_message"):
			tab.show_message(group, message, sender)


func send_message_to_server(group: int, message: String, target: String = "") -> void:
	if not Globals.logged_in:
		return
	
	if target == "":
		Client.send_data(PacketWriter.write_message(group, message))
	else:
		Client.send_data(PacketWriter.write_private_message(group, message, target))


## Handles a message. Check if it is a command, else send it to the server
func process_message(message: String) -> void:
	if message.is_empty():
		return
	
	if message.begins_with(COMMAND_CHAR):
		var string_array := message.split(" ")
		var given_command = string_array[0].trim_prefix(COMMAND_CHAR)
		# Commands not implemented here are handled by the server
		match given_command:
			"help":
				show_message(0, "Available commands: /help, /roll")
			_:
				send_message_to_server(current_group, message)
	else:
		if current_group == GROUPS.PRIVATE:
			if chat_target == "":
				show_message(0, "No private message target selected")
			else:
				if Globals.logged_in:
					show_message(current_group, message, "to %s" % chat_target)
					send_message_to_server(current_group, message, chat_target)
				else:
					show_message(0, "Log in to send private messages")
		else:
			if Globals.logged_in:
				show_message(current_group, message, PlayerStats.username)
				send_message_to_server(current_group, message)
			else:
				show_message(current_group, message)


func _input(event):
	if event.is_action_pressed("input_text"):
		if line_edit.has_focus():
			var input_text: String = line_edit.text
			if input_text.is_empty() == false:
				process_message(input_text)
				line_edit.clear()
			line_edit.release_focus()
		else:
			line_edit.grab_focus()
	
	elif event.is_action_pressed("ui_cancel"):
		line_edit.release_focus()


func _on_chat_selection_button_item_selected(index):
	# The selection excludes system. First selectable group is nearby (id = 1)
	current_group = index + 1
	
	if current_group == GROUPS.PRIVATE:
		UserInterface.select_message_target()
