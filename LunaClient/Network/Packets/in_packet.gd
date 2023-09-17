class_name InPacket
extends Node

var buffer := StreamPeerBuffer.new()
var code := 0

func _init(packet_bytes: PackedByteArray) -> void:
	buffer.data_array = packet_bytes
	# The packet starts with the code
	code = buffer.get_16()


func get_byte() -> int:
	return buffer.get_8()


func get_short() -> int:
	return buffer.get_16()


func get_int() -> int:
	return buffer.get_32()


func get_long() -> int:
	return buffer.get_64()


func get_bool() -> bool:
	return buffer.get_8() == 1


func get_string() -> String:
	var string_size := buffer.get_32()
	return buffer.get_string(string_size)


func get_vec2() -> Vector2:
	var x = buffer.get_32()
	var y = buffer.get_32()
	return Vector2(x, y)
