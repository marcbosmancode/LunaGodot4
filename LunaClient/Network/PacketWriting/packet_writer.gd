class_name PacketWriter
extends Node

enum OutCodes {
	HEARTBEAT = 0,
	CHAT_MESSAGE = 1,
	LOGIN = 2,
	PLAYER_POSITION_UPDATE = 3,
	PLAYER_STATE_UPDATE = 4,
	PORTAL_INTERACTION = 5,
	ALTER_INVENTORY = 6,
	CONSUME_ITEM = 7,
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


static func write_player_position_update(new_position: Vector2, teleport: bool) -> OutPacket:
	var packet := OutPacket.new(OutCodes.PLAYER_POSITION_UPDATE)
	packet.write_vec2(new_position)
	packet.write_bool(teleport)
	return packet


static func write_player_state_update(new_animation: String, new_direction: float) -> OutPacket:
	var packet := OutPacket.new(OutCodes.PLAYER_STATE_UPDATE)
	packet.write_string(new_animation)
	packet.write_int(int(new_direction))
	return packet


static func write_portal_interact(id: int) -> OutPacket:
	var packet := OutPacket.new(OutCodes.PORTAL_INTERACTION)
	packet.write_int(id)
	return packet


static func write_alter_inventory(slot_1: int, slot_2: int) -> OutPacket:
	var packet := OutPacket.new(OutCodes.ALTER_INVENTORY)
	packet.write_int(slot_1)
	packet.write_int(slot_2)
	return packet


static func consume_item(slot: int) -> OutPacket:
	var packet := OutPacket.new(OutCodes.CONSUME_ITEM)
	packet.write_int(slot)
	return packet
