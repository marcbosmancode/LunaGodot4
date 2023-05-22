extends Node
class_name DashComponent

@export var duration: float = 0.3
@export var cooldown: float = 0.5

var can_dash: bool = true
var ghost: Resource = preload("res://Particles/DashGhost.tscn")
var direction: float = 1.0

@onready var duration_timer = $DurationTimer
@onready var cooldown_timer = $CooldownTimer
@onready var ghost_timer = $GhostTimer
@onready var dust_particles = $DustParticles

func start(target_direction: float) -> void:
	duration_timer.start(duration)
	
	direction = target_direction
	ghost_timer.start()
	
	# Make the dust particles move opposite of the dash direction
	dust_particles.rotation = Vector2(target_direction * -1, 0).angle()
	dust_particles.restart()
	dust_particles.emitting = true


func is_dashing() -> bool:
	return not duration_timer.is_stopped()


func instantiate_ghost() -> void:
	var ghost_instance: Sprite2D = ghost.instantiate()
	get_parent().add_child(ghost_instance)
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
