extends CharacterBody2D
class_name Player

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
# Movement engine constants
const SPEED: int = 90
const ACCELERATION: int = 400
const FRICTION: int = 400
const AIR_ACCELERATION: int = 90
const AIR_RESISTANCE: int = 30
const JUMP_FORCE: int = 280
const DOUBLE_JUMPS: int = 1
const DASH_SPEED: int = 300
const COYOTE_TIME: float = 0.1
const JUMP_BUFFER_TIME: float = 0.1
const TELEPORT_DISTANCE: int = 80
const TELEPORT_COOLDOWN: float = 0.6
const AIR_TELEPORTS: int = 1
# Multiplayer constants
const NET_UPDATE_DELAY: float = 0.2

enum States {
	FREE,
	DASH,
	ATTACK,
}

# Movement variables
var gravity: int = ProjectSettings.get_setting("physics/2d/default_gravity")
var state: States = States.FREE
var grounded: bool = false
var jump_allowed: bool = false
var remaining_double_jumps: int = 1
var last_direction: float = 1.0
var teleport_allowed: bool = true
var remaining_teleports: int = 1
# Multiplayer Variables
var previous_position: Vector2 = Vector2.ZERO
var used_teleport: bool = false

var previous_direction: float = 1.0
var current_animation: StringName = ""
var previous_animation: StringName = ""
# Visual variables
var camera_bounds: Vector2 = Vector2(10000, 10000)
var chat_bubble_resource = preload("res://UserInterface/ChatSystem/chat_bubble.tscn")

@onready var hairstyle_back = $CanvasGroup/HairstyleBack
@onready var body = $CanvasGroup/Body
@onready var outfit = $CanvasGroup/Outfit
@onready var weapon = $CanvasGroup/Weapon
@onready var head = $CanvasGroup/Head
@onready var hairstyle_front = $CanvasGroup/HairstyleFront
@onready var eyes = $CanvasGroup/Eyes
@onready var sprite_group = $CanvasGroup
@onready var animation_player = $AnimationPlayer
@onready var net_update_timer = $NetUpdateTimer
@onready var coyote_timer = $CoyoteTimer
@onready var jump_buffer = $JumpBuffer
@onready var teleport_timer = $TeleportTimer
@onready var dash_component: DashComponent = $DashComponent
@onready var double_jump_particles = $DoubleJumpParticles
@onready var teleport_particles = $TeleportParticles
@onready var camera_2d = $Camera2D
@onready var username_label = $UsernameLabel

func _ready() -> void:
	body.frame_changed.connect(_on_body_frame_changed)
	animation_player.animation_finished.connect(_on_animation_player_animation_finished)
	animation_player.animation_started.connect(_on_animation_player_animation_started)
	net_update_timer.timeout.connect(_on_net_update_timer_timeout)
	net_update_timer.start(NET_UPDATE_DELAY)
	coyote_timer.timeout.connect(_on_coyote_timer_timeout)
	teleport_timer.timeout.connect(_on_teleport_timer_timeout)
	MessageBus.show_chat_bubble.connect(_on_show_chat_bubble)
	
	camera_2d.limit_right = camera_bounds.x
	camera_2d.limit_bottom = camera_bounds.y
	username_label.text = PlayerStats.username


func _physics_process(delta):
	grounded = is_on_floor()
	if grounded:
		jump_allowed = true
		remaining_double_jumps = DOUBLE_JUMPS
		remaining_teleports = AIR_TELEPORTS
		coyote_timer.start(COYOTE_TIME)
	
	match state:
		States.FREE:
			state_free(delta)
		States.DASH:
			state_dash(delta)
		States.ATTACK:
			state_attack(delta)


func state_free(delta) -> void:
	if not grounded:
		velocity.y += gravity * delta
		
		# Save jump input for a more responsive feel
		if Input.is_action_just_pressed("jump") and Globals.can_move:
			jump_buffer.start(JUMP_BUFFER_TIME)
	
	# Handle jumping
	if Input.is_action_just_pressed("jump") and Globals.can_move or not jump_buffer.is_stopped():
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
	var direction: float = 0.0
	if Globals.can_move:
		direction = Input.get_action_strength("move_right") - Input.get_action_strength("move_left")
	
	if direction:
		last_direction = direction
		
		if grounded:
			velocity.x += direction * ACCELERATION * delta
		else:
			velocity.x += direction * AIR_ACCELERATION * delta
	else:
		if grounded:
			velocity.x = move_toward(velocity.x, 0, FRICTION * delta)
		else:
			velocity.x = move_toward(velocity.x, 0, AIR_RESISTANCE * delta)
	
	# Cap movement speed for this state
	velocity.x = clamp(velocity.x, -SPEED, SPEED)
	
	update_animation(direction)
	
	# Special movement. Only allow one per physics frame
	if Globals.can_move:
		if Input.is_action_pressed("dash") and dash_component.can_dash:
			dash()
		elif Input.is_action_just_pressed("teleport"):
			teleport()
			used_teleport = true
		elif Input.is_action_pressed("basic_attack"):
			animation_player.play("slash")
			state = States.ATTACK
	
	move_and_slide()


func dash() -> void:
	velocity.x = last_direction * DASH_SPEED
	state = States.DASH
	dash_component.start(last_direction)


func teleport() -> void:
	if not teleport_allowed:
		return
	if remaining_teleports <= 0:
		return
	
	# Check for the intended direction
	var target_direction: Vector2 = get_input_direction()
	if target_direction == Vector2.ZERO:
		return
	
	# Prevent clipping into the terrain
	var distance_reduction: float = 0.0
	var reduction_step: float = TELEPORT_DISTANCE * 0.1
	while test_move(transform, target_direction * (TELEPORT_DISTANCE - distance_reduction)):
		distance_reduction += reduction_step
		if distance_reduction >= TELEPORT_DISTANCE:
			return
	
	position += target_direction * (TELEPORT_DISTANCE - distance_reduction)
	velocity = Vector2.ZERO
	remaining_teleports -= 1
	teleport_allowed = false
	# Prevent jumping on nothing far from a platform
	jump_allowed = false
	teleport_timer.start(TELEPORT_COOLDOWN)
	
	teleport_particles.restart()
	teleport_particles.emitting = true


func get_input_direction() -> Vector2:
	var input_direction: Vector2 = Vector2.ZERO
	
	# Prioritize up and down movement
	if Input.is_action_pressed("move_up"):
		input_direction = Vector2.UP
	elif Input.is_action_pressed("move_down"):
		input_direction = Vector2.DOWN
	else:
		var direction: float = Input.get_action_strength("move_right") - Input.get_action_strength("move_left")
		input_direction = Vector2(sign(direction), 0)
	
	return input_direction


func state_dash(delta) -> void:
	if not grounded:
		velocity.y += gravity * delta
	
	if Input.is_action_just_pressed("jump") and Globals.can_move:
		jump_buffer.start(JUMP_BUFFER_TIME)
	
	# Check if dash has ended
	if not dash_component.is_dashing():
		state = States.FREE
	
	animation_player.play("dash")
	move_and_slide()


func state_attack(delta) -> void:
	if not grounded:
		velocity.y += gravity * delta
	else:
		velocity.x = move_toward(velocity.x, 0, FRICTION * delta)
	
	# Special movement. Only allow one per physics frame
	if Globals.can_move:
		if Input.is_action_just_pressed("teleport"):
			teleport()
	
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
	head.offset = sprite_offset
	eyes.offset = sprite_offset
	hairstyle_front.offset = sprite_offset
	
	outfit.frame = new_frame
	weapon.frame = clamp(new_frame - WEAPON_FRAME_OFFSET, 0, WEAPON_FRAMES-1)


func _on_body_frame_changed() -> void:
	var new_frame: int = body.frame
	sync_animations(new_frame)


func _on_animation_player_animation_finished(_anim_name: StringName) -> void:
	if state == States.ATTACK:
		state = States.FREE


func _on_animation_player_animation_started(anim_name: StringName) -> void:
	current_animation = anim_name


func _on_coyote_timer_timeout() -> void:
	jump_allowed = false


func _on_teleport_timer_timeout() -> void:
	teleport_allowed = true


func _on_net_update_timer_timeout() -> void:
	if Globals.logged_in:
		if global_position.floor() != previous_position:
			net_update_position()
		if current_animation != previous_animation or last_direction != previous_direction:
			net_update_animation()


func net_update_position() -> void:
	previous_position = global_position.floor()
	Client.send_data(PacketWriter.write_player_position_update(global_position, used_teleport))
	# Reset teleport tracking variable again
	used_teleport = false


func net_update_animation() -> void:
	previous_animation = current_animation
	previous_direction = last_direction
	Client.send_data(PacketWriter.write_player_state_update(previous_animation, last_direction))


func _on_show_chat_bubble(message: String, sender: String) -> void:
	# Only create bubble if we are the sender
	if sender != PlayerStats.username:
		return
	
	# Delete existing chat bubbles
	var old_chat_bubble: Node = get_node_or_null("ChatBubble")
	if old_chat_bubble != null:
		old_chat_bubble.queue_free()
	
	var chat_bubble = chat_bubble_resource.instantiate()
	chat_bubble.text = message
	chat_bubble.owner_name = PlayerStats.username
	
	add_child(chat_bubble)
