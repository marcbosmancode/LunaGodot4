# Created on : Nov 19, 2023, 12:52:13 AM
# Author     : Marc
# Teleports the player to the nearest town

returnScene = sh.getReturnScene()

if returnScene != -1:
    sh.takeItem(4, 1)
    sh.changeScene(returnScene)
else:
    sh.sendMessage("Unable to teleport from here")
