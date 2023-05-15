extends Node
class_name DashComponent

@export var duration: float = 0.4
@export var cooldown: float = 0.4

var can_dash: bool = true
var ghost = preload("res://Player/Components/DashGhost.tscn")
var direction: float = 1.0

@onready var duration_timer = $DurationTimer
@onready var cooldown_timer = $CooldownTimer
@onready var ghost_timer = $GhostTimer

func start(target_direction: float) -> void:
	duration_timer.start(duration)
	
	direction = target_direction
	instantiate_ghost()
	ghost_timer.start()


func is_dashing() -> bool:
	return not duration_timer.is_stopped()


func instantiate_ghost() -> void:
	var ghost_instance: Sprite2D = ghost.instantiate()
	get_tree().current_scene.add_child(ghost_instance)
	
	ghost_instance.position = get_parent().position
	ghost_instance.scale.x = direction


func _on_duration_timer_timeout():
	can_dash = false
	ghost_timer.stop()
	cooldown_timer.start(cooldown)


func _on_cooldown_timer_timeout():
	can_dash = true


func _on_ghost_timer_timeout():
	instantiate_ghost()
