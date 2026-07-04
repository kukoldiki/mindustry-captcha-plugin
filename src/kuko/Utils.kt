package kuko

import arc.struct.Seq
import kuko.world.TilePatched
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.core.World
import mindustry.game.Team
import mindustry.gen.Call
import mindustry.world.Tile
import mindustry.world.WorldContext
import mindustry.world.blocks.environment.Floor
import java.awt.Font
import java.awt.image.BufferedImage

object Utils {
    private val maxWidth = 800;
    private val maxHeight = 50;

    const val characters = "qwertyuiopasdfghjklzxcvbnm2345689"

    private val fontCache = mutableMapOf<Char, Array<String>>()

    fun getWorld(): World {
        val world = World()
        val ctx: WorldContext = world.context

        // world.beginMapLoad()
        world.resize(maxWidth, maxHeight)

        // val floorId = Blocks.darksand.asFloor().id.toInt()
        val overlayId = Blocks.air.id.toInt()
        val blockId = Blocks.air.id.toInt()

        for (y in 0 until maxHeight) {
            for (x in 0 until maxWidth) {
                val tile = TilePatched(x, y, Blocks.darksand.asFloor().id.toInt(), overlayId, blockId)
                world.tiles.set(x, y, tile)
            }
        }

        // world.endMapLoad()

        world.tile(3, 3).setBlock(Blocks.coreShard, Team.sharded)
        val code = getRandomString(30)
        Call.sendMessage(code)

        drawLetters(world, code, 10, 10, getRandomFloorsByColor(), 0)

        return world
    }

    private fun renderChar(c: Char, size: Int = 16): Array<String> {
        val img = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
        val g = img.createGraphics()

        g.font = Font("Arial", Font.BOLD, size)
        g.drawString(c.toString(), 0, size - 1)
        g.dispose()

        val result = Array(size) { y ->
            buildString {
                for (x in 0 until size) {
                    val rgb = img.getRGB(x, y)
                    append(if (rgb and 0xFFFFFF != 0) '1' else '0')
                }
            }
        }

        return result
    }

    private fun getGlyph(c: Char): Array<String> {
        return fontCache.getOrPut(c) {
            renderChar(c)
        }
    }

    fun drawLetters(
        world: World,
        text: String,
        startX: Int,
        startY: Int,
        floors: Seq<Floor>,
        blockId: Int
    ) {
        var offsetX = 0

        for (ch in text.uppercase()) {
            val glyph = getGlyph(ch)

            for (y in glyph.indices) {
                for (x in glyph[y].indices) {
                    if (glyph[y][x] == '1') {
                        val tx = startX + offsetX + x
                        val ty = startY + (glyph.size - 1 - y)

                        if (tx in 0 until maxWidth && ty in 0 until maxHeight) {
                            val tile = TilePatched(
                                tx,
                                ty,
                                floors.shuffle().first().id.toInt(),
                                0,
                                blockId
                            )
                            world.tiles.set(tx, ty, tile)
                        }
                    }
                }
            }

            offsetX += glyph[0].length + 1
        }
    }

    fun getRandomString(len: Int): String {
        val sb = StringBuilder()

        for (i in 0..<len) {
            sb.append(characters[PVars.random.nextInt(characters.length)])
        }

        return sb.toString()
    }

    fun getRandomFloor(): Floor {
        return PVars.floors.shuffle().first()
    }

    fun getRandomFloorsByColor(): Seq<Floor> {
        return PVars.floorsByColor.shuffle().first()
    }
}