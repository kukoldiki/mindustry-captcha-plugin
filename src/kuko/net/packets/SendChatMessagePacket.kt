package kuko.net.packets

import arc.util.Log
import kuko.PVars
import kuko.Utils
import mindustry.gen.SendChatMessageCallPacket
import mindustry.net.NetConnection

fun handleSendChatMessage(con: NetConnection, packet: SendChatMessageCallPacket) {
    val player = con.player
    val status = PVars.playerStatuses[player.uuid()]
    status ?: return
    status.code ?: return
    val message = Utils.stripFoo(packet.message)
    Log.debug("Message: ${message} Code ${status.code}")
    if(!message.equals(status.code, ignoreCase = true)) {
        player.sendMessage("Wrong code!")
    } else {
        player.sendMessage("Ok!")
        Utils.reloadWorld(player)
    }
}