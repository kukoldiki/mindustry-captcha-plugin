package kuko

import arc.struct.Seq
import arc.util.Log
import kuko.PVars.randomizeTiles
import kuko.model.PlayerStatus
import kuko.world.TilePatched
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.core.World
import mindustry.game.Team
import mindustry.gen.Call
import mindustry.gen.Player
import mindustry.world.WorldContext
import mindustry.world.blocks.environment.Floor
import java.awt.Font
import java.awt.image.BufferedImage

object Utils {
    private val minWidth = 80;
    private val minHeight = 30;

    val charSize = 16
    val paddingRight = 5
    val baseId = Blocks.darksand.asFloor().id.toInt()

    const val characters = "qwertyupafhjlzxcvbnm23469"

    private val fontCache = mutableMapOf<Char, Array<String>>()

    fun reloadWorld(player: Player) {
        val status = player.getStatus()
        status.world = null
        status.code = null

        Call.worldDataBegin(player.con)
        Vars.netServer.sendWorldData(player)
    }

    fun getWorld(status: PlayerStatus): World {
        val world = World()
        val ctx: WorldContext = world.context

        val code = getRandomString(3)
        status.code = code
        Log.debug("Generated code: $code")

        // world.beginMapLoad()
        val width = maxOf(minWidth, code.length * charSize + paddingRight)
        val height = maxOf(minHeight, charSize)
        val floorsColor = getRandomFloorsByColor()
        val forbidden = floorsColor.map { it.id.toInt() }.toSet()

        world.resize(width, height)

        // val floorId = Blocks.darksand.asFloor().id.toInt()
        val overlayId = Blocks.air.id.toInt()
        val blockId = Blocks.air.id.toInt()

        for (y in 0 until height) {
            for (x in 0 until width) {
                val id = if (randomizeTiles.bool()) {
                    var result: Int
                    do {
                        result = getRandomFloor().id.toInt()
                    } while (result in forbidden)
                    result
                } else {
                    baseId
                }

                val tile = TilePatched(x, y, id, overlayId, blockId)
                world.tiles.set(x, y, tile)
            }
        }

        world.tile(3, 3).setBlock(Blocks.coreShard, Team.sharded)

        // world.endMapLoad()
        // Call.sendMessage(code)

        drawLetters(world, code, 10, 6, floorsColor, 0)

        return world
    }

    private fun renderChar(c: Char): Array<String> {
        val img = BufferedImage(charSize, charSize, BufferedImage.TYPE_INT_ARGB)
        val g = img.createGraphics()

        g.font = Font("Arial", Font.BOLD, charSize)
        g.drawString(c.toString(), 0, charSize - 1)
        g.dispose()

        val result = Array(charSize) { y ->
            buildString {
                for (x in 0 until charSize) {
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

                        if (tx in 0 until minWidth && ty in 0 until minHeight) {
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

    fun Player.getStatus(): PlayerStatus {
        return PVars.playerStatuses[this.uuid()] ?: PlayerStatus()
    }

    fun stripFoo(string: String): String {
        val var1 = StringBuilder(string)
        for (i in string.length - 1 downTo 0) {
            if (var1[i].code in 0xf80..0x107f) var1.deleteCharAt(i)
        }
        return var1.toString()
    }
}