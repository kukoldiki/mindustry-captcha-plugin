package kuko.events

import arc.Core
import arc.Events
import kuko.PVars
import kuko.net.NetServerPatched
import kuko.net.packets.handleSendChatMessage
import mindustry.Vars
import mindustry.core.NetServer
import mindustry.game.EventType
import mindustry.gen.ClientSnapshotCallPacket
import mindustry.gen.SendChatMessageCallPacket
import mindustry.io.JsonIO

class PEvents {
    fun load() {
        Events.on(EventType.ServerLoadEvent::class.java) {
            val old = Vars.netServer
            Core.app.removeListener(old)
            Vars.netServer = NetServerPatched()
            JsonIO.json.copyFields(old, Vars.netServer)
            old.dispose()
            Core.app.addListener(Vars.netServer)

            Vars.content.blocks().each { bl ->
                PVars.rules.bannedBlocks.add(bl)

                if(bl.isFloor && !bl.isOverlay)
                    PVars.floors.add(bl.asFloor())
            }

            Vars.net.handleServer(SendChatMessageCallPacket::class.java, { con, packet ->
                handleSendChatMessage(con, packet)
            })
        }

        Events.on(EventType.PlayerLeave::class.java) { e ->
            PVars.playerStatuses.remove(e.player.uuid())
        }
    }
}