class_name OutPacket
extends Node

var code := 0
var buffer := StreamPeerBuffer.new()

func _init(packet_code: int) -> void:
	code = packet_code
	buffer.put_16(code)


func write_byte(value: int) -> void:
	buffer.put_8(value)


func write_short(value: int) -> void:
	buffer.put_16(value)


func write_int(value: int) -> void:
	buffer.put_32(value)


func write_long(value: int) -> void:
	buffer.put_64(value)


func write_bool(value: bool) -> void:
	# Casting a bool to and int gives 1 for true and 0 for false
	var int_value := int(value)
	buffer.put_8(int_value)


func write_string(value: String) -> void:
	buffer.put_string(value)


func write_vec2(value: Vector2) -> void:
	buffer.put_32(int(value.x))
	buffer.put_32(int(value.y))
