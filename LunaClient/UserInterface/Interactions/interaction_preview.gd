extends NinePatchRect
class_name InteractionPreview

@onready var interaction_texture_rect = $HBoxContainer/InteractionTexture
@onready var interaction_text_label = $HBoxContainer/InteractionText
@onready var highlight = $SelectionHighlight

var interactable: Interactable
var selected: bool = false:
	set(new_value):
		selected = new_value
		if selected:
			highlight.show()
		else:
			highlight.hide()


func show_interaction(new_interactable: Node, new_selected: bool) -> void:
	if new_interactable is Interactable:
		interactable = new_interactable
	
	selected = new_selected
	if interactable is Interactable:
		interaction_text_label.text = interactable.get_interaction_text()
		interaction_texture_rect.texture = interactable.get_interaction_image()
