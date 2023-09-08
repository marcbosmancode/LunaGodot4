extends Node

const SCENE_PATH := "res://Scenes/Ingame/scene_%s.tscn"


func change_scene(scene_id: int) -> void:
	var target_path = SCENE_PATH % str(scene_id)
	
	if ResourceLoader.exists(target_path):
		get_tree().change_scene_to_file(target_path)
	else:
		UserInterface.show_message(0, "Target scene is invalid!")
