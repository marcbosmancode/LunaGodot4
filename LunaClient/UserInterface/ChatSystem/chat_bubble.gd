extends Node2D

var text: String = ""
var owner_name: String = ""

@onready var message_label = $NinePatchRect/MarginContainer/MessageLabel
@onready var life_timer = $LifeTimer

func _ready():
	life_timer.timeout.connect(_on_life_timer_timeout)
	
	message_label.text = "%s: %s" % [owner_name, text]


func _on_life_timer_timeout() -> void:
	queue_free()
