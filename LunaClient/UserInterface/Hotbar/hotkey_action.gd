extends Resource
class_name HotkeyAction

var action_type: int = Enums.HotkeyType.ITEM
var action_id: int = 0

func _init(a_type: int, a_id: int):
	action_type = a_type
	action_id = a_id
	
