package net.superricky.tpaplusplus.config.config

import com.uchuhimo.konf.ConfigSpec

object CommonSpec : ConfigSpec("common") {
    val showBlockedMessage by required<Boolean>()
    val toggledPlayerCommand by required<Boolean>()
    val tpaTimeout by required<Double>()
    val waitTimeBeforeTp by required<Double>()
    val language by required<String>()
}
