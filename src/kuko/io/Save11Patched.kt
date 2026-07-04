package kuko.io

import arc.util.io.Writes
import mindustry.Vars
import mindustry.core.World
import mindustry.game.Team
import mindustry.io.TypeIO
import mindustry.io.versions.Save11
import java.io.DataOutput
import java.io.IOException

class Save11Patched : Save11() {
    @Throws(IOException::class)
    fun writeMap(stream: DataOutput, world: World) {
        //write world size
        stream.writeShort(world.width())
        stream.writeShort(world.height())

        //floor + overlay
        run {
            var i = 0
            while (i < world.width() * world.height()) {
                val tile = world.tiles.geti(i)
                stream.writeShort(tile.floorID().toInt())
                stream.writeShort(tile.overlayID().toInt())
                var consecutives = 0

                var j = i + 1
                while (j < world.width() * world.height() && consecutives < 255) {
                    val nextTile = world.rawTile(j % world.width(), j / world.width())

                    if (nextTile.floorID() != tile.floorID() || nextTile.overlayID() != tile.overlayID()) {
                        break
                    }

                    consecutives++
                    j++
                }

                stream.writeByte(consecutives)
                i += consecutives
                i++
            }
        }

        //blocks
        var i = 0
        while (i < world.width() * world.height()) {
            val tile = world.tiles.geti(i)
            stream.writeShort(tile.blockID().toInt())

            val savedata = tile.shouldSaveData()

            //in the old version, the second bit was set to indicate presence of data, but that approach was flawed - it didn't allow buildings + data on the same tile
            //so now the third bit is used instead
            val packed = ((if (tile.build != null) 1 else 0) or (if (savedata) 4 else 0)).toByte()

            //make note of whether there was an entity or custom tile data here
            stream.writeByte(packed.toInt())

            if (savedata) {
                //the new 'extra data' format writes 7 bytes of data instead of 1
                stream.writeByte(tile.data.toInt())
                stream.writeByte(tile.floorData.toInt())
                stream.writeByte(tile.overlayData.toInt())
                stream.writeInt(tile.extraData)
            }

            //only write the entity for multiblocks once - in the center
            if (tile.build != null) {
                if (tile.isCenter) {
                    stream.writeBoolean(true)
                    writeChunk(stream) { out: Writes? ->
                        out!!.b(tile.build.version().toInt())
                        tile.build.writeAll(out)
                    }
                } else {
                    stream.writeBoolean(false)
                }
            } else if (!savedata) { //don't write consecutive blocks when there is custom data
                //write consecutive non-entity blocks
                var consecutives = 0

                var j = i + 1
                while (j < world.width() * world.height() && consecutives < 255) {
                    val nextTile = world.rawTile(j % world.width(), j / world.width())

                    if (nextTile.blockID() != tile.blockID() || savedata != nextTile.shouldSaveData()) {
                        break
                    }

                    consecutives++
                    j++
                }

                stream.writeByte(consecutives)
                i += consecutives
            }
            i++
        }
    }
}