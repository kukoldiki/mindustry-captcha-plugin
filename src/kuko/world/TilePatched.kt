package kuko.world

import arc.func.Prov
import mindustry.game.Team
import mindustry.gen.Building
import mindustry.world.Block
import mindustry.world.Tile

class TilePatched : Tile {

    constructor(x: Int, y: Int) : super(x, y)

    constructor(x: Int, y: Int, floor: Int, overlay: Int, wall: Int)
            : super(x, y, floor, overlay, wall)

    constructor(x: Int, y: Int, floor: Block, overlay: Block, wall: Block)
            : super(x, y, floor, overlay, wall)

    override fun changeBuild(team: Team, entityprov: Prov<Building>, rotation: Int) {

    }

    override fun changed() {

    }
}