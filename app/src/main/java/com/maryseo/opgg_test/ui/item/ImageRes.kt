package com.maryseo.opgg_test.ui.item

import com.maryseo.opgg_test.R

enum class Position(name: String) {
    TOP("TOP"),
    JNG("JNG"),
    ADC("ADC"),
    SUP("SUP"),
    MID("MID");

}

fun getPositionIcon(position: Position): Int {
    return when (position) {
        Position.TOP -> R.drawable.icon_lol_top
        Position.JNG -> R.drawable.icon_lol_jng
        Position.ADC -> R.drawable.icon_lol_bot
        Position.SUP -> R.drawable.icon_lol_sup
        Position.MID -> R.drawable.icon_lol_mid
    }
}