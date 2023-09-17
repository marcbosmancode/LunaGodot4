extends Node2D
class_name NetPlayer

const ANIMATION_OFFSET := {
	0: Vector2(0, 0),
	1: Vector2(0, 0),
	2: Vector2(0, 1),
	3: Vector2(0, 1),
	4: Vector2(0, 1),
	5: Vector2(0, 0),
	6: Vector2(1, 1),
	7: Vector2(1, 0),
	8: Vector2(1, 0),
	9: Vector2(1, 1),
	10: Vector2(1, 0),
	11: Vector2(1, 0),
	12: Vector2(0, 0),
	13: Vector2(0, 1),
	14: Vector2(0, 1)
}
const WEAPON_FRAME_OFFSET: int = 11
const WEAPON_FRAMES: int = 4

var id: int = -1
var username: String = ""
var target_position: Vector2 = Vector2.ZERO

@onready var hairstyle_back = $CanvasGroup/HairstyleBack
@onready var body = $CanvasGroup/Body
@onready var outfit = $CanvasGroup/Outfit
@onready var attack_effects = $CanvasGroup/AttackEffects
@onready var weapon = $CanvasGroup/Weapon
@onready var head = $CanvasGroup/Head
@onready var hairstyle_front = $CanvasGroup/HairstyleFront
@onready var eyes = $CanvasGroup/Eyes
@onready var sprite_group = $CanvasGroup
@onready var animation_player = $AnimationPlayer
@onready var double_jump_particles = $DoubleJumpParticles
@onready var teleport_particles = $TeleportParticles
@onready var dash_particles = $DashParticles
@onready var username_label = $UsernameLabel

func _ready() -> void:
	body.frame_changed.connect(_on_body_frame_changed)
	username_label.text = username


func sync_animations(new_frame: int) -> void:
	var sprite_offset: Vector2 = ANIMATION_OFFSET[new_frame]
	
	hairstyle_back.offset = sprite_offset
	head.offset = sprite_offset
	eyes.offset = sprite_offset
	hairstyle_front.offset = sprite_offset
	
	outfit.frame = new_frame
	weapon.frame = clamp(new_frame - WEAPON_FRAME_OFFSET, 0, WEAPON_FRAMES-1)


func _on_body_frame_changed() -> void:
	var new_frame: int = body.frame
	sync_animations(new_frame)


func update_position(new_position: Vector2) -> void:
	# Vec2 was rounded while sending packet. To keep the character on the floor add 1
	position = new_position + Vector2(1, 1)
