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
const FRICTION: int = 400
const AIR_ACCELERATION: int = 90
const AIR_RESISTANCE: int = 30
const JUMP_FORCE: int = 280
const DOUBLE_JUMPS: int = 1
const DASH_SPEED: int = 300
const COYOTE_TIME: float = 0.1
const JUMP_BUFFER_TIME: float = 0.1

enum States {
	FREE,
	DASH,
	ATTACK,
}

var gravity: int = ProjectSettings.get_setting("physics/2d/default_gravity")
var state: States = States.FREE
var grounded: bool = false
var jump_allowed: bool = false
var remaining_double_jumps: int = 1
var last_direction: float = 1.0

@onready var hairstyle_back = $CanvasGroup/HairstyleBack
@onready var body = $CanvasGroup/Body
@onready var outfit = $CanvasGroup/Outfit
@onready var hairstyle_front = $CanvasGroup/HairstyleFront
@onready var eyes = $CanvasGroup/Eyes
@onready var sprite_group = $CanvasGroup
@onready var animation_player = $AnimationPlayer
@onready var coyote_timer = $CoyoteTimer
@onready var jump_buffer = $JumpBuffer
@onready var dash: DashComponent = $DashComponent
@onready var double_jump_particles = $DoubleJumpParticles

func _ready() -> void:
	body.frame_changed.connect(_on_body_frame_changed)
	coyote_timer.timeout.connect(_on_coyote_timer_timeout)


func _physics_process(delta):
	grounded = is_on_floor()
	if grounded:
		jump_allowed = true
		remaining_double_jumps = DOUBLE_JUMPS
		coyote_timer.start(COYOTE_TIME)
	
	match state:
		States.FREE:
			state_free(delta)
		States.DASH:
			state_dash(delta)
		States.ATTACK:
			state_attack(delta)


func state_free(delta) -> void:
	# Cap movement speed for this state
	velocity.x = clamp(velocity.x, -SPEED, SPEED)
	
	if not grounded:
		velocity.y += gravity * delta
		
		# Save jump input for a more responsive feel
		if Input.is_action_just_pressed("jump"):
			jump_buffer.start(JUMP_BUFFER_TIME)
	
	# Handle jumping
	if Input.is_action_just_pressed("jump") or not jump_buffer.is_stopped():
		if jump_allowed:
			velocity.y = -JUMP_FORCE
			jump_allowed = false
			jump_buffer.stop()
		elif remaining_double_jumps > 0:
			# Double jumps
			velocity.y = -JUMP_FORCE
			remaining_double_jumps -= 1
			jump_buffer.stop()
			
			double_jump_particles.restart()
			double_jump_particles.emitting = true
	
	# Handle directional movement
	var direction: float = Input.get_action_strength("move_right") - Input.get_action_strength("move_left")
	if direction:
		last_direction = direction
		
		if grounded:
			velocity.x = direction * SPEED
		else:
			velocity.x += direction * AIR_ACCELERATION * delta
	else:
		if grounded:
			velocity.x = move_toward(velocity.x, 0, FRICTION * delta)
		else:
			velocity.x = move_toward(velocity.x, 0, AIR_RESISTANCE * delta)
	
	# Handle dashing
	if Input.is_action_pressed("dash") and dash.can_dash:
		velocity.x = last_direction * DASH_SPEED
		state = States.DASH
		dash.start(last_direction)
	
	update_animation(direction)
	move_and_slide()


func state_dash(delta) -> void:
	if not grounded:
		velocity.y += gravity * delta
	
	if Input.is_action_just_pressed("jump"):
		jump_buffer.start(JUMP_BUFFER_TIME)
	
	# Check if dash has ended
	if not dash.is_dashing():
		state = States.FREE
	
	animation_player.play("dash")
	move_and_slide()


func state_attack(delta) -> void:
	if not grounded:
		velocity.y += gravity * delta
	
	animation_player.play("slash_1")
	move_and_slide()


func update_animation(direction: float) -> void:
	if grounded:
		if direction:
			animation_player.play("run")
		else:
			animation_player.play("idle")
	else:
		if velocity.y < 0:
			animation_player.play("jump")
		else:
			animation_player.play("fall")
	
	# Change direction the player is facing
	if direction:
		sprite_group.scale.x = sign(direction)


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
