extends Area2D

signal interactables_changed(interactables_list, selected_index)
signal selected_interactable_changed(new_index)

var parent: Node
var target: int = 0
var interactables: Array[Node] = []

func _ready():
	parent = get_parent()


func _input(event) -> void:
	if event.is_action_pressed("interact"):
		# Validate the target interaction
		if target < 0 or target >= interactables.size():
			return
		
		var target_interactable: Node = interactables[target]
		
		if target_interactable.has_method("interact"):
			target_interactable.interact(parent)
		
		return
	
	# Change target interactable
	var previous_target: int = target
	if event.is_action_pressed("move_down"):
		target += 1
	elif event.is_action_pressed("move_up"):
		target -= 1
	
	if previous_target != target:
		target = clamp(target, 0, max(0, interactables.size()-1))
		selected_interactable_changed.emit(target)


func _on_body_entered(body: Node2D) -> void:
	print("interaction possible")
	var has_interaction = false
	
	if body.has_method("can_interact"):
		has_interaction = body.can_interact(parent)
	
	if has_interaction:
		interactables.append(body)
		interactables_changed.emit(interactables, target)


func _on_body_exited(body: Node2D) -> void:
	print("interaction poof")
	if interactables.has(body):
		interactables.erase(body)
		
		target = clamp(target, 0, max(0, interactables.size()-1))
		interactables_changed.emit(interactables, target)
