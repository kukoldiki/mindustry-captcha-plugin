package kuko

import kuko.events.PEvents
import mindustry.mod.Plugin

class CaptchaPlugin : Plugin() {
    override fun init() {
        val events = PEvents()
        events.load()
        PVars.rules.canGameOver = false
    }
}