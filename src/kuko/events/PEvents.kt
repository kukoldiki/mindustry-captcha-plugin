package kuko.events

import arc.Core
import arc.Events
import kuko.PVars
import kuko.net.NetServerPatched
import mindustry.Vars
import mindustry.core.NetServer
import mindustry.game.EventType
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
                Vars.state.rules.bannedBlocks.add(bl)

                if(bl.isFloor && !bl.isOverlay)
                    PVars.floors.add(bl.asFloor())
            }
        }

        Events.on(EventType.PlayerLeave::class.java) { e ->
            PVars.playerWorlds.remove(e.player.uuid())
        }
    }
}