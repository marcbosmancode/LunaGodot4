extends Node2D

func _ready():
	Client.connect_to_server()
