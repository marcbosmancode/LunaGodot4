extends Control

@onready var level_label = $StatPreview/ExperiencePreview/HeaderTexture/LevelLabel
@onready var health_bar = $Vitals/HealthPreview/TextureProgressBar
@onready var health_details_label = $Vitals/HealthPreview/TextureProgressBar/DetailsLabel
@onready var mana_bar = $Vitals/ManaPreview/TextureProgressBar
@onready var mana_details_label = $Vitals/ManaPreview/TextureProgressBar/DetailsLabel
@onready var exp_bar = $StatPreview/ExperiencePreview/TextureProgressBar
@onready var exp_details_label = $StatPreview/ExperiencePreview/TextureProgressBar/ExpLabel

func _ready():
	MessageBus.player_logged_in.connect(_on_player_logged_in)
	PlayerStats.level_changed.connect(_on_level_changed)
	PlayerStats.experience_changed.connect(_on_experience_changed)
	PlayerStats.max_health_changed.connect(_on_max_health_changed)
	PlayerStats.health_changed.connect(_on_health_changed)
	PlayerStats.max_mana_changed.connect(_on_max_mana_changed)
	PlayerStats.mana_changed.connect(_on_mana_changed)


func _on_player_logged_in(_username: String) -> void:
	show()


func get_required_exp(level: int) -> int:
	return 10 + level * 2


func _on_level_changed(new_value: int) -> void:
	level_label.text = str(new_value)
	exp_bar.max_value = get_required_exp(new_value)


func _on_experience_changed(new_value: int) -> void:
	exp_bar.value = new_value
	
	# Make one of the numbers a float to avoid integer division
	var experience_percentage: float = new_value / float(exp_bar.max_value) * 100
	# 2 point decimal number. Also double % sign to make it appear wihout being a format placeholder
	exp_details_label.text = "%.2f%%" % experience_percentage


func _on_max_health_changed(new_value: int) -> void:
	health_bar.max_value = new_value
	health_details_label.text = "%s/%s" % [PlayerStats.health, new_value]


func _on_health_changed(new_value: int) -> void:
	health_bar.value = new_value
	health_details_label.text = "%s/%s" % [new_value, PlayerStats.max_health]


func _on_max_mana_changed(new_value: int) -> void:
	mana_bar.max_value = new_value
	mana_details_label.text = "%s/%s" % [PlayerStats.mana, new_value]


func _on_mana_changed(new_value: int) -> void:
	mana_bar.value = new_value
	mana_details_label.text = "%s/%s" % [new_value, PlayerStats.max_mana]

