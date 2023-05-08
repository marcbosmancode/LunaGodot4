extends TabBar

const LINE_LIMIT := 100

@export var groups: Array[int]

var chat_colors := []

@onready var text_box = $RichTextLabel

func _ready() -> void:
	chat_colors.append(Color.from_string("#995f59", Color.WHITE))
	chat_colors.append(Color.from_string("#f0f5fa", Color.WHITE))
	chat_colors.append(Color.from_string("#509cff", Color.WHITE))
	chat_colors.append(Color.from_string("#76ff50", Color.WHITE))
	chat_colors.append(Color.from_string("#ff9350", Color.WHITE))
	chat_colors.append(Color.from_string("#ff5076", Color.WHITE))


func show_message(group: int, message: String, sender: String="") -> void:
	# Only add messages meant for this tab
	if groups.has(group) == false:
		return
	
	text_box.push_color(chat_colors[group])
	
	if text_box.get_paragraph_count() > LINE_LIMIT:
		text_box.remove_paragraph(0)
	
	var output_message := ""
	if sender != "":
		output_message += "[%s] " % sender
	output_message += message
	output_message += "\n"
	
	text_box.add_text(output_message)
