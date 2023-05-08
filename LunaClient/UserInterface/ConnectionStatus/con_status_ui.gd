extends Control

@export var texture_connected: Texture
@export var texture_disconnected: Texture

@onready var status_texturerect = $StatusTextureRect
@onready var latency_label = $LatencyLabel

func _ready():
	Client.host_connected.connect(_on_host_connected)
	Client.host_disconnected.connect(_on_host_disconnected)
	Client.latency_changed.connect(_on_latency_changed)

func _on_host_connected() -> void:
	status_texturerect.texture = texture_connected

func _on_host_disconnected() -> void:
	status_texturerect.texture = texture_disconnected
	_on_latency_changed(999)

func _on_latency_changed(new_value: int) -> void:
	latency_label.text = "%s ms" % new_value
