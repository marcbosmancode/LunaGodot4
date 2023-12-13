extends Node

signal just_logged_in
signal hotkey_action_changed(hotkey_id, new_action)

# User Interface
var ui_positions: Dictionary = {
	0: Vector2(275, 152),
	1: Vector2(275, 140),
	2: Vector2(230, 152),
}

# Networking
var logged_in := false:
	set(new_value):
		logged_in = new_value
		just_logged_in.emit()

# Gameplay
var can_move: bool = true
var hotkey_actions: Dictionary = {
	1: null,
	2: null,
	3: null,
	4: null,
	5: null,
	6: null,
	7: null,
	8: null,
	9: null,
}

# Controls
var hotbar_keys: Dictionary = {
	1: "1",
	2: "2",
	3: "3",
	4: "4",
	5: "5",
	6: "6",
	7: "Q",
	8: "W",
	9: "E",
}

func change_hotkey_action(hotkey_id: int, new_action: HotkeyAction, update_server: bool = true) -> void:
	# Check if hotkey is the same and do nothing if true
	var existing_action = hotkey_actions[hotkey_id]
	if existing_action is HotkeyAction and new_action is HotkeyAction:
		if existing_action.action_type == new_action.action_type and existing_action.action_id == new_action.action_id:
			return
	
	# Remove the action from other hotkeys
	for hotkey in hotkey_actions:
		var action = hotkey_actions[hotkey]
		if action is HotkeyAction and new_action is HotkeyAction:
			if action.action_type == new_action.action_type and action.action_id == new_action.action_id:
				if update_server:
					Client.send_data(PacketWriter.update_keybind(hotkey, Enums.HotkeyType.EMPTY, 0))
				
				hotkey_actions[hotkey] = null
				hotkey_action_changed.emit(hotkey, null)
	
	if update_server:
		if new_action is HotkeyAction:
			Client.send_data(PacketWriter.update_keybind(hotkey_id, new_action.action_type, new_action.action_id))
		else:
			Client.send_data(PacketWriter.update_keybind(hotkey_id, Enums.HotkeyType.EMPTY, 0))
	
	hotkey_actions[hotkey_id] = new_action
	hotkey_action_changed.emit(hotkey_id, new_action)
