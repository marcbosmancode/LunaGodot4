extends BasePopup

@onready var username_input = $VBoxContainer/UsernameInput
@onready var password_input = $VBoxContainer/PasswordInput
@onready var login_button = $LoginButton

func _ready() -> void:
	super()
	
	Globals.just_logged_in.connect(_on_just_logged_in)
	Client.host_connected.connect(_on_host_connected)


func _on_login_button_pressed() -> void:
	var username: String = username_input.text
	var password: String = password_input.text
	
	if username == "" or password == "":
		UserInterface.show_ok_popup("Please enter a valid username and password")
	else:
		Client.send_data(PacketWriter.write_login(username, password))


func _on_exit_button_pressed() -> void:
	get_tree().quit()


func _on_just_logged_in() -> void:
	login_button.disabled = true


func _on_host_connected() -> void:
	login_button.disabled = false
