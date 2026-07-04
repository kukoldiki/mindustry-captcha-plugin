package kuko.net

import arc.util.io.Writes
import kuko.PVars
import kuko.Utils
import mindustry.Vars
import mindustry.ctype.Content
import mindustry.ctype.ContentType
import mindustry.ctype.UnlockableContent
import mindustry.gen.Player
import mindustry.io.JsonIO
import mindustry.io.SaveIO
import mindustry.logic.GlobalVars
import java.io.DataOutputStream
import java.io.IOException
import java.io.OutputStream

object NetworkIOPatched {
    fun writeWorld(player: Player, os: OutputStream?) {
        try {
            DataOutputStream(os).use { stream ->
                //write all researched content to rules if hosting
                /* Nope
                if (Vars.state.isCampaign) {
                    Vars.state.rules.researched.clear()
                    for (type in ContentType.all) {
                        for (c in Vars.content.getBy<Content>(type)) {
                            if (c is UnlockableContent && c.unlocked() && c.techNode != null) {
                                Vars.state.rules.researched.add(c)
                            }
                        }
                    }
                }
                */

                stream.writeUTF(JsonIO.write(PVars.rules))
                stream.writeUTF(JsonIO.write(Vars.state.mapLocales))
                PVars.saveWriter.writeStringMap(stream, Vars.state.map.tags)

                stream.writeInt(Vars.state.wave)
                stream.writeFloat(Vars.state.wavetime)
                stream.writeDouble(Vars.state.tick)
                stream.writeLong(GlobalVars.rand.seed0)
                stream.writeLong(GlobalVars.rand.seed1)

                stream.writeInt(player.id)
                player.write(Writes(stream))

                PVars.saveWriter.writeContentHeader(stream)
                PVars.saveWriter.writeContentPatches(stream)

                val world = Utils.getWorld()
                PVars.playerWorlds.put(player.uuid(), world)

                PVars.saveWriter.writeMap(stream, world)
                PVars.saveWriter.writeTeamBlocks(stream)
                PVars.saveWriter.writeMarkers(stream)
                PVars.saveWriter.writeCustomChunks(stream, true)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}