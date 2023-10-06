extends Node

const SCENE_PATH := "res://Scenes/Ingame/scene_%s.tscn"
const NET_PLAYER_SCENE = preload("res://Player/net_player.tscn")

var current_players := []

func change_scene(scene_id: int, new_position: Vector2 = Vector2.ZERO) -> void:
	# Set the spawn location for the player in the new scene
	if new_position != Vector2.ZERO:
		PlayerStats.respawn_point = new_position
	
	current_players.clear()
	
	var target_path = SCENE_PATH % str(scene_id)
	
	if ResourceLoader.exists(target_path):
		get_tree().change_scene_to_file(target_path)
	else:
		UserInterface.show_message(0, "Target scene is invalid!")


func update_other_player_position(player_id: int, new_position: Vector2, used_teleport: bool) -> void:
	for net_player in current_players:
		if net_player is NetPlayer:
			if net_player.id == player_id:
				net_player.update_position(new_position, used_teleport)
				break


func update_other_player_state(player_id: int, new_animation: String, new_direction: int) -> void:
	for net_player in current_players:
		if net_player is NetPlayer:
			if net_player.id == player_id:
				net_player.update_state(new_animation, new_direction)
				break


func add_other_player(player_id: int, player_username: String, target_position: Vector2) -> void:
	var new_player = NET_PLAYER_SCENE.instantiate()
	new_player.id = player_id
	new_player.username = player_username
	
	current_players.append(new_player)
	
	# Add the player to the scene
	new_player.global_position = target_position
	get_tree().current_scene.add_child(new_player)


func remove_other_player(player_id) -> void:
	var target_player: NetPlayer = null
	
	for net_player in current_players:
		if net_player is NetPlayer:
			if net_player.id == player_id:
				target_player = net_player
				break
	
	if target_player != null:
		current_players.erase(target_player)
		target_player.queue_free()
