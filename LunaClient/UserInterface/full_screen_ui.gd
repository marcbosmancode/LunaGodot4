extends Control

@onready var tab_list = $TabList

var tabs: Dictionary = {
	"inventory": 0,
	"settings": 1,
}
var active_tab: int = -1

func show_tab(tab_id: int) -> void:
	for child in tab_list.get_children():
		if child.has_method("hide"):
			child.hide()
	
	var child := tab_list.get_child(tab_id)
	if child != null and child.has_method("show"):
		active_tab = tab_id
		child.show()


func close() -> void:
	Globals.can_move = true
	hide()


func _on_close_button_pressed():
	close()


func _input(event):
	var tab_id: int = -1
	if event.is_action_pressed("open_inventory"):
		tab_id = tabs.get("inventory")
	if event.is_action_pressed("open_settings"):
		tab_id = tabs.get("settings")
	
	if Globals.can_move and Globals.logged_in:
		if tab_id != -1:
			if is_visible_in_tree() and active_tab == tab_id:
				close()
				return
			else:
				show_tab(tab_id)
				show()


func _on_inventory_button_pressed():
	show_tab(tabs.get("inventory"))


func _on_settings_button_pressed():
	show_tab(tabs.get("settings"))
