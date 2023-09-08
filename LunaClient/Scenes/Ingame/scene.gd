extends Node2D

const PLAYER_SCENE = preload("res://Player/player.tscn")

func _ready():
	# Create the player at the desired location
	var player := PLAYER_SCENE.instantiate()
	player.position = PlayerStats.respawn_point
	get_tree().current_scene.add_child(player)
