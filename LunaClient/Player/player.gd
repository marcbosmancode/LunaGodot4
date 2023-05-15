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
# Movement engine constants
const SPEED: int = 90
const JUMP_FORCE: int = 240
const COYOTE_TIME: float = 0.1
const JUMP_BUFFER_TIME: float = 0.1
const DASH_SPEED: int = 220

enum States {
	FREE,
	DASH
}

var gravity: int = ProjectSettings.get_setting("physics/2d/default_gravity")
var state := States.FREE
var grounded: bool = false
var jump_allowed: bool = false
var last_direction: float = 1.0

@onready var hairstyle_back = $CanvasGroup/HairstyleBack
@onready var body = $CanvasGroup/Body
@onready var outfit = $CanvasGroup/Outfit
@onready var hairstyle_front = $CanvasGroup/HairstyleFront
@onready var eyes = $CanvasGroup/Eyes
@onready var sprite_group = $CanvasGroup
@onready var animation = $AnimationPlayer
@onready var coyote_timer = $CoyoteTimer
@onready var jump_buffer = $JumpBuffer
@onready var dash: DashComponent = $DashComponent

func _ready() -> void:
	body.frame_changed.connect(_on_body_frame_changed)
	coyote_timer.timeout.connect(_on_coyote_timer_timeout)


func _physics_process(delta):
	grounded = is_on_floor()
	if grounded:
		jump_allowed = true
		coyote_timer.start(COYOTE_TIME)
	
	match state:
		States.FREE:
			state_free(delta)
		States.DASH:
			state_dash(delta)


func state_free(delta) -> void:
	if not grounded:
		velocity.y += gravity * delta
		
		if Input.is_action_just_pressed("jump"):
			jump_buffer.start(JUMP_BUFFER_TIME)
	
	if Input.is_action_just_pressed("jump") or not jump_buffer.is_stopped():
		jump()
	
	# Handle directional movement
	var direction: float = Input.get_action_strength("move_right") - Input.get_action_strength("move_left")
	if direction:
		velocity.x = direction * SPEED
		last_direction = direction
	else:
		velocity.x = move_toward(velocity.x, 0, SPEED)
	
	# Handle dashing
	if Input.is_action_pressed("dash") and dash.can_dash:
		dash.start(last_direction)
		velocity.x = last_direction * DASH_SPEED
		state = States.DASH
	
	update_animation(direction)
	move_and_slide()


func state_dash(delta) -> void:
	animation.play("dash")
	
	if not grounded:
		velocity.y += gravity * delta
	
	if not dash.is_dashing():
		state = States.FREE
	
	if Input.is_action_just_pressed("jump"):
		jump_buffer.start(JUMP_BUFFER_TIME)
	
	move_and_slide()


func jump() -> void:
	if jump_allowed:
		velocity.y = -JUMP_FORCE
		jump_allowed = false


func update_animation(direction: float) -> void:
	if grounded:
		if direction:
			animation.play("run")
		else:
			animation.play("idle")
	else:
		if velocity.y < 0:
			animation.play("jump")
		else:
			animation.play("fall")
	
	if direction:
		sprite_group.scale.x = direction


func sync_animations(new_frame: int) -> void:
	var sprite_offset: Vector2 = ANIMATION_OFFSET[new_frame]
	
	hairstyle_back.offset = sprite_offset
	eyes.offset = sprite_offset
	hairstyle_front.offset = sprite_offset
	
	outfit.frame = new_frame


func _on_body_frame_changed() -> void:
	var new_frame: int = body.frame
	sync_animations(new_frame)


func _on_coyote_timer_timeout() -> void:
	jump_allowed = false
