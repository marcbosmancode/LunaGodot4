extends Node
class_name Interactable

@export var text: String = "Interact"
@export var image: Texture

func can_interact(interacting_node: Node) -> bool:
	return interacting_node is Player


## Override this function with interactable specific logic
func interact(_interacting_node: Node) -> void:
	print("WARNING uncoded interactable triggered")


func get_interaction_text() -> String:
	return text


func get_interaction_image() -> Texture:
	return image
