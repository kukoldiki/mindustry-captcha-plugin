package kuko

import arc.math.Rand
import arc.struct.ObjectMap
import arc.struct.Seq
import arc.struct.Seq.with
import kuko.io.Save11Patched
import mindustry.content.Blocks
import mindustry.core.World
import mindustry.game.Rules
import mindustry.io.SaveIO
import mindustry.world.blocks.environment.Floor

object PVars {
    val saveWriter = Save11Patched()
    val playerWorlds = ObjectMap<String, World>()

    val rules = Rules()
    val random = Rand()

    val floors = Seq<Floor>()
    val floorsByColor: Seq<Seq<Floor>> = with(
        with(Blocks.water.asFloor(), Blocks.cryofluid.asFloor()), // blue +-
        with(Blocks.slag.asFloor(), Blocks.denseRedStone.asFloor(), Blocks.redStone.asFloor(), Blocks.metalTiles12.asFloor(), Blocks.cruxRuneOverlay.asFloor()), // red
        /*with(Blocks.dirt.asFloor(), Blocks.mud.asFloor(), Blocks.rhyolite.asFloor(), Blocks.rhyoliteCrater.asFloor(), Blocks.roughRhyolite.asFloor(),
        Blocks.regolith.asFloor()),*/ // brown +-
        with(Blocks.arkyciteFloor.asFloor(), Blocks.arkyicStone.asFloor(), Blocks.grass.asFloor(), Blocks.beryllicStone.asFloor()), // green
    )
}