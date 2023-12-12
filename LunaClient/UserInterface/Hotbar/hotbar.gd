extends Control

const HOTKEY_COUNT: int = 9

func _ready():
	MessageBus.player_logged_in.connect(_on_player_logged_in)


func _on_player_logged_in(_username: String) -> void:
	show()


func hotkey_action(hotkey_id: int) -> void:
	var key_action = Globals.hotkey_actions.get(hotkey_id)
	
	if key_action is HotkeyAction:
		if key_action.action_type == Enums.HotkeyType.ITEM:
			Inventory.consume_item_id(key_action.action_id)
		elif key_action.action_type == Enums.HotkeyType.SKILL:
			# TODO implement skills
			UserInterface.show_message(0, "Unable to use the skill")


func _input(event):
	if Globals.logged_in and Globals.can_move:
		var current_key: int = 1
		
		while current_key <= HOTKEY_COUNT:
			if event.is_action_pressed("hotkey_%s" % current_key):
				hotkey_action(current_key)
			current_key += 1
