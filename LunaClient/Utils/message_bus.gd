extends Node

# User interface signals
signal interactables_changed(interactables_list, selected_index)
signal selected_interactable_changed(new_index)
signal show_item_details(item, slot)

signal show_chat_bubble(message, sender)

signal show_message(group, message, sender)
signal private_message_target_changed(new_target)
