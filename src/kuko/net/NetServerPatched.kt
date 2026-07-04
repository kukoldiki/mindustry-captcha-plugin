package kuko.net

import arc.util.Log
import arc.util.io.FastDeflaterOutputStream
import mindustry.core.NetServer
import mindustry.gen.Player
import mindustry.net.NetworkIO
import mindustry.net.Packets.WorldStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.DeflaterOutputStream

class NetServerPatched : NetServer() {
    override fun sendWorldData(player: Player) {
        val stream = ByteArrayOutputStream()
        val def: DeflaterOutputStream = FastDeflaterOutputStream(stream)
        NetworkIOPatched.writeWorld(player, def)
        val data = WorldStream()
        data.stream = ByteArrayInputStream(stream.toByteArray())
        player.con.sendStream(data)

        Log.debug(
            "Packed @ bytes of world data to @ (@ / @)",
            stream.size(),
            player.name,
            player.con.address,
            player.uuid()
        )
    }
}