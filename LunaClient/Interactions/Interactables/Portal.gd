extends Interactable

const DELAY_SEC: int = 2

@export var id: int = 1

@onready var delay_timer = $DelayTimer

var interactable: bool = false


func _ready():
	delay_timer.timeout.connect(_on_delay_timer_timeout)


func interact(_interacting_node: Node) -> void:
	if interactable == false:
		return
	
	# The server handles the interaction
	Client.send_data(PacketWriter.write_portal_interact(id))
	
	interactable = false
	delay_timer.start(DELAY_SEC)


func _on_delay_timer_timeout() -> void:
	interactable = true
