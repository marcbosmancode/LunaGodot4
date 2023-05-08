extends CharacterBody2D

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
	11: Vector2(1, 0)
}
const SPEED = 80.0
const JUMP_VELOCITY = -200.0

@onready var hairstyle_back = $CanvasGroup/HairstyleBack
@onready var body = $CanvasGroup/Body
@onready var outfit = $CanvasGroup/Outfit
@onready var hairstyle_front = $CanvasGroup/HairstyleFront
@onready var eyes = $CanvasGroup/Eyes
@onready var sprite_group = $CanvasGroup
@onready var animation = $AnimationPlayer

var gravity = 400.0

func _ready() -> void:
	body.frame_changed.connect(_on_body_frame_changed)


func _physics_process(delta):
	# Add the gravity.
	if not is_on_floor():
		velocity.y += gravity * delta

	# Handle Jump.
	if Input.is_action_just_pressed("ui_accept") and is_on_floor():
		velocity.y = JUMP_VELOCITY

	# Get the input direction and handle the movement/deceleration.
	# As good practice, you should replace UI actions with custom gameplay actions.
	var direction = Input.get_axis("ui_left", "ui_right")
	if direction:
		velocity.x = direction * SPEED
		animation.play("run")
	else:
		velocity.x = move_toward(velocity.x, 0, SPEED)
	
	if is_on_floor():
		if direction:
			animation.play("run")
		else:
			animation.play("idle")
	else:
		if velocity.y < 0:
			animation.play("jump")
		else:
			animation.play("fall")
	
	if direction != 0:
		sprite_group.scale.x = direction

	move_and_slide()


func sync_animations(new_frame: int) -> void:
	var sprite_offset: Vector2 = ANIMATION_OFFSET[new_frame]
	
	hairstyle_back.offset = sprite_offset
	eyes.offset = sprite_offset
	hairstyle_front.offset = sprite_offset
	
	outfit.frame = new_frame


func _on_body_frame_changed():
	var new_frame = body.frame
	sync_animations(new_frame)
