extends Node

signal level_changed(value)
signal experience_changed(value)
signal max_health_changed(value)
signal health_changed(value)
signal max_mana_changed(value)
signal mana_changed(value)

var username: String = ""
var respawn_point: Vector2 = Vector2.ZERO

var level: int = 1:
	set(new_value):
		level = new_value
		level_changed.emit(new_value)
var experience: int = 0:
	set(new_value):
		experience = new_value
		experience_changed.emit(new_value)
var max_health: int = 1:
	set(new_value):
		max_health = new_value
		max_health_changed.emit(new_value)
var health: int = 1:
	set(new_value):
		health = new_value
		health_changed.emit(new_value)
var max_mana: int = 1:
	set(new_value):
		max_mana = new_value
		max_mana_changed.emit(new_value)
var mana: int = 1:
	set(new_value):
		mana = new_value
		mana_changed.emit(new_value)
