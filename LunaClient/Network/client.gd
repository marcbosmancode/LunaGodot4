extends Node

signal host_connected # Successfully connected to the host
signal host_disconnected
signal connection_error # Failed connecting to the host
signal latency_changed(value) # Latency updated from heartbeat packets

const HOST_IP := "127.0.0.1"
const HOST_PORT := 1515

var con_status := 0
var con_stream := StreamPeerTCP.new()
var peer_stream := PacketPeerStream.new()

var latency_ms := 0:
	set(new_value):
		latency_ms = new_value
		emit_signal("latency_changed", latency_ms)

func _ready() -> void:
	con_status = con_stream.get_status()
	peer_stream.stream_peer = con_stream


## Connect to the server and update the connection status
func connect_to_server(connection_ip := HOST_IP) -> void:
	print("Connecting to the server. ip=%s port=%d" % [connection_ip, HOST_PORT])
	
	con_status = con_stream.STATUS_NONE
	if con_stream.connect_to_host(HOST_IP, HOST_PORT) != OK:
		print("Error connecting to the server")
		emit_signal("connection_error")


## Disconnect from the server if the connection is still active
func disconnect_from_server() -> void:
	if con_status == con_stream.STATUS_CONNECTED:
		con_stream.disconnect_from_host()


## Sends a data packet to the server
func send_data(out_packet: OutPacket) -> void:
	if con_status != con_stream.STATUS_CONNECTED:
		print("Error sending data: No connection to the server")
		return
	
	print("Sending a packet to the server. code=%s" % out_packet.code)
	var packet_data := out_packet.buffer.data_array
	var error := peer_stream.put_packet(packet_data)
	if error != OK:
		print("Error sending data: Failed sending data to the server")


func _process(_delta) -> void:
	# Poll the socket first to update the state
	var _error := con_stream.poll()
	var updated_status := con_stream.get_status()
	if updated_status != con_status:
		con_status = updated_status
		
		# Emit signals based on connection status changes
		match con_status:
			con_stream.STATUS_NONE:
				print("Disconnected from the server")
				emit_signal("host_disconnected")
				HeartbeatSystem.stop_heartbeat()
			con_stream.STATUS_CONNECTED:
				print("Successfully connected to the server")
				emit_signal("host_connected")
			con_stream.STATUS_ERROR:
				print("Error with the connection to the server")
				emit_signal("connection_error")
	
	if con_status == con_stream.STATUS_CONNECTED:
		# Handle incoming data
		if peer_stream.get_available_packet_count() > 0:
			var in_packet := InPacket.new(peer_stream.get_packet())
			PacketHandler.handle_packet(in_packet)
