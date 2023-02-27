package com.maryseo.opgg_test.data

data class Summoner(
    val name: String = "",
    val level: Int = 0,
    val profileImageUrl: String? = null,
    val profileBorderImageUrl: String? = null,
    val url: String? = null,
    val leagues: List<League> = ArrayList(),
    val previousTiers: List<Tier> = ArrayList(),
    val ladderRank: LadderRank = LadderRank(),
    val profileBackgroundImageUrl: String? = null
)
