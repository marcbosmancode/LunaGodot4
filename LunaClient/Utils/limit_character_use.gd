extends LineEdit

@export var allowed_characters: String = "[a-zA-Z0-9`~!@#$%^&*()\\-_=+\\[\\]{}\\\\|:;'\",.<>\\/?\\s]"

var regex := RegEx.new()

func _ready() -> void:
	regex.compile(allowed_characters)

func _on_text_changed(new_text: String) -> void:
	var old_caret_position = caret_column - 1
	var updated_text: String = ""
	
	for valid_character in regex.search_all(new_text):
		updated_text += valid_character.get_string()
	text = updated_text
	
	if new_text.length() > updated_text.length():
		caret_column = old_caret_position
	else:
		caret_column = old_caret_position + 1
