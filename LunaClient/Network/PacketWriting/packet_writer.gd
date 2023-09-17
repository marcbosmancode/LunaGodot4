class_name PacketWriter
extends Node

enum OutCodes {
	HEARTBEAT = 0,
	CHAT_MESSAGE = 1,
	LOGIN = 2,
	PLAYER_POSITION_UPDATE = 3,
	PORTAL_INTERACTION = 4,
}

static func write_heartbeat() -> OutPacket:
	var packet := OutPacket.new(OutCodes.HEARTBEAT)
	return packet


static func write_message(group: int, message: String) -> OutPacket:
	var packet := OutPacket.new(OutCodes.CHAT_MESSAGE)
	packet.write_short(group)
	packet.write_string(message)
	return packet

static func write_private_message(group: int, message: String, target: String) -> OutPacket:
	var packet := OutPacket.new(OutCodes.CHAT_MESSAGE)
	packet.write_short(group)
	packet.write_string(message)
	packet.write_string(target)
	return packet


static func write_login(username: String, password: String) -> OutPacket:
	var packet := OutPacket.new(OutCodes.LOGIN)
	packet.write_string(username)
	packet.write_string(password)
	return packet


static func write_player_position_update(new_position: Vector2) -> OutPacket:
	var packet := OutPacket.new(OutCodes.PLAYER_POSITION_UPDATE)
	packet.write_vec2(new_position)
	return packet


static func write_portal_interaction(portal_id: int) -> OutPacket:
	var packet := OutPacket.new(OutCodes.PORTAL_INTERACTION)
	packet.write_int(portal_id)
	return packet
