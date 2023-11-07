extends Resource
class_name Item

@export var id: int = 0
@export var name: String = ""
@export var description: String = ""
@export var texture: Texture
@export var type: int = 0
@export var max_quantity: int = 1
@export var trade_locked: bool = false

const TYPE_NAMES: Array[String] = ["Etc", "Consumable", "Currency", "Weapon", "Armor", "Accessory"]

var quantity: int = 1

func get_type_name() -> String:
	return TYPE_NAMES[type]
