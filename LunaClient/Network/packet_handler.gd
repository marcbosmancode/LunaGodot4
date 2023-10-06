class_name PacketHandler
extends Node

enum InCodes {
	HEARTBEAT = 0,
	HANDSHAKE = 1,
	MESSAGE = 2,
	LOGIN = 3,
	PLAYER_POSITION_UPDATE = 4,
	PLAYER_STATE_UPDATE = 5,
	CHANGE_SCENE = 6,
	PLAYER_ENTERED_SCENE = 7,
	PLAYER_LEFT_SCENE = 8,
}

static func handle_packet(in_packet: InPacket) -> void:
	print("Received a packet from the server. code=%s" % in_packet.code)
	# Handle a packet based on the packet code
	match in_packet.code:
		InCodes.HEARTBEAT:
			var current_unix_time := Time.get_unix_time_from_system()
			HeartbeatSystem.last_response = current_unix_time
			var elapsed_time := current_unix_time - HeartbeatSystem.write_time
			# The elapsed time calculates the ping (round trip time)
			# To get the latency (one way delay) divide the elapsed time in half
			var latency := elapsed_time * 0.5
			Client.latency_ms = int(latency * 1000)
		
		InCodes.HANDSHAKE:
			var accepted_online := in_packet.get_byte()
			var server_version := in_packet.get_string()
			var server_unix_time := in_packet.get_long()
			
			# Accepted online acts as a bool with 1 = true
			if accepted_online == 1:
				var server_time := Time.get_time_string_from_unix_time(server_unix_time)
				UserInterface.show_message(0, "Connected to the server! Server version %s Server time %s (UTC)" % [server_version, server_time])
				HeartbeatSystem.start_heartbeat()
			else:
				print("Server refused the connection. Reached player capacity")
		
		InCodes.MESSAGE:
			var group := in_packet.get_short()
			var message := in_packet.get_string()
			var sender := in_packet.get_string()
			
			UserInterface.show_message(group, message, sender)
		
		InCodes.LOGIN:
			var result := in_packet.get_bool()
			
			if result == true:
				var username := in_packet.get_string()
				var map_id := in_packet.get_int()
				var respawn_point: Vector2 = Vector2(in_packet.get_int(), in_packet.get_int())
				Globals.logged_in = true
				PlayerStats.username = username
				PlayerStats.respawn_point = respawn_point
				UserInterface.show_message(0, "Successfully logged in as %s!" % username)
				SceneHandler.change_scene(map_id)
			else:
				var reason := in_packet.get_byte()
				var explanation: String = "Login failed: "
				match reason:
					0:
						explanation += "Invalid credentials"
					1:
						explanation += "Account already logged in"
					2:
						explanation += "Account is banned"
					3:
						var comment := in_packet.get_string()
						explanation += "Account is banned"
						explanation += comment
					_:
						explanation += "Error"
				UserInterface.show_ok_popup(explanation)
		
		InCodes.PLAYER_POSITION_UPDATE:
			var player_id := in_packet.get_int()
			var player_position: Vector2 = Vector2(in_packet.get_int(), in_packet.get_int())
			var player_teleported: bool = in_packet.get_bool()
			
			SceneHandler.update_other_player_position(player_id, player_position, player_teleported)
		
		InCodes.PLAYER_STATE_UPDATE:
			var player_id := in_packet.get_int()
			var player_animation := in_packet.get_string()
			var player_direction := in_packet.get_int()
			
			SceneHandler.update_other_player_state(player_id, player_animation, player_direction)
		
		InCodes.CHANGE_SCENE:
			var new_scene_id := in_packet.get_int()
			var new_position: Vector2 = Vector2(in_packet.get_int(), in_packet.get_int())
			
			SceneHandler.change_scene(new_scene_id, new_position)
		
		InCodes.PLAYER_ENTERED_SCENE:
			var player_id := in_packet.get_int()
			var player_username := in_packet.get_string()
			var player_position: Vector2 = Vector2(in_packet.get_int(), in_packet.get_int())
			# Vec2 was rounded while sending packet. To keep the character on the floor add 1
			player_position += Vector2(1, 1)
			
			SceneHandler.add_other_player(player_id, player_username, player_position)
		
		InCodes.PLAYER_LEFT_SCENE:
			var player_id := in_packet.get_int()
			
			SceneHandler.remove_other_player(player_id)
