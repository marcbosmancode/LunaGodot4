extends VBoxContainer
class_name InteractionsUI

@onready var interaction_preview = preload("interaction_preview.tscn")

func _ready():
	MessageBus.interactables_changed.connect(_on_interactables_changed)
	MessageBus.selected_interactable_changed.connect(_on_selected_interactable_changed)


func _on_interactables_changed(new_interactables: Array[Node], target_index: int) -> void:
	# Clear current UI
	for c in get_children():
		c.queue_free()
	
	if new_interactables.is_empty():
		return
	
	var i: int = 0
	for interactable in new_interactables:
		var interaction_preview_instance = interaction_preview.instantiate()
		add_child(interaction_preview_instance)
		
		if interaction_preview_instance.has_method("show_interaction"):
			var selected: bool = i == target_index
			interaction_preview_instance.show_interaction(interactable, selected)
		
		i += 1


func _on_selected_interactable_changed(target_index: int) -> void:
	var i: int = 0
	for c in get_children():
		if c is InteractionPreview:
			# Let each interaction preview visually update
			if i == target_index:
				c.selected = true
			else:
				c.selected = false
		
		i += 1
