extends Node

const DELAY_SEC := 10
const TIMEOUT_SEC := 20

enum Status {
	RUNNING,
	STOPPED,
}

var timer := Timer.new()
var write_time := 0.0
var last_response := 0.0

var status := Status.STOPPED

func _ready() -> void:
	timer.one_shot = true
	timer.timeout.connect(_on_timer_timeout)
	# Add the timer to the scene tree for it to function
	add_child(timer)


func send_heartbeat() -> void:
	write_time = Time.get_unix_time_from_system()
	
	var out_packet := PacketWriter.write_heartbeat()
	Client.send_data(out_packet)


func start_heartbeat() -> void:
	send_heartbeat()
	timer.start(DELAY_SEC)
	status = Status.RUNNING


func stop_heartbeat() -> void:
	timer.stop()
	status = Status.STOPPED


func _on_timer_timeout() -> void:
	# Disconnect from the server upon timeout
	var current_time := Time.get_unix_time_from_system()
	if current_time - last_response > TIMEOUT_SEC:
		Client.disconnect_from_server()
	else:
		send_heartbeat()
		timer.start(DELAY_SEC)
	
