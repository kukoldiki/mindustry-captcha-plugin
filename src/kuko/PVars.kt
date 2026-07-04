package kuko

import arc.math.Rand
import arc.struct.ObjectMap
import arc.struct.Seq
import arc.struct.Seq.with
import kuko.io.Save11Patched
import kuko.model.PlayerStatus
import mindustry.content.Blocks
import mindustry.core.World
import mindustry.game.Rules
import mindustry.io.SaveIO
import mindustry.net.Administration
import mindustry.world.blocks.environment.Floor

object PVars {
    val saveWriter = Save11Patched()
    val playerStatuses = ObjectMap<String, PlayerStatus>()

    val rules = Rules()
    val random = Rand()

    val floors = Seq<Floor>()
    val floorsByColor: Seq<Seq<Floor>> = with(
        with(Blocks.water.asFloor(), Blocks.cryofluid.asFloor()), // blue +-
        with(Blocks.slag.asFloor(), Blocks.denseRedStone.asFloor(), Blocks.redStone.asFloor(), Blocks.metalTiles12.asFloor(), Blocks.cruxRuneOverlay.asFloor()), // red
        /*with(Blocks.dirt.asFloor(), Blocks.mud.asFloor(), Blocks.rhyolite.asFloor(), Blocks.rhyoliteCrater.asFloor(), Blocks.roughRhyolite.asFloor(),
        Blocks.regolith.asFloor()),*/ // brown +-
        //with(Blocks.arkyciteFloor.asFloor(), Blocks.arkyicStone.asFloor(), Blocks.grass.asFloor(), Blocks.beryllicStone.asFloor()), // green
    )

    val randomizeTiles = Administration.Config("randomizeTiles", "Should tiles in the background of the letters be randomized", false)
}