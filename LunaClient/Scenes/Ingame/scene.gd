extends Node2D

@export var camera_bounds: Vector2 = Vector2(10000, 10000)

const PLAYER_SCENE = preload("res://Player/player.tscn")

func _ready():
	# Create the player at the desired location
	var player := PLAYER_SCENE.instantiate()
	player.position = PlayerStats.respawn_point
	player.camera_bounds = camera_bounds
	get_tree().current_scene.add_child(player)
