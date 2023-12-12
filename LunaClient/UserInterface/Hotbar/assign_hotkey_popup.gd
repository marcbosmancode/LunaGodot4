extends BasePopup

@onready var hotkey_buttons = $HBoxContainer

var action_type: int = 0
var action_id: int = 0

func _ready():
	super()
	
	for child in hotkey_buttons.get_children():
		if child.has_signal("hotkey_pressed"):
			child.hotkey_pressed.connect(_on_hotkey_pressed)


func _on_hotkey_pressed(hotkey_id: int) -> void:
	# Create a action and attach it to the hotkey
	Globals.change_hotkey_action(hotkey_id, HotkeyAction.new(action_type, action_id))
	
	queue_free()
