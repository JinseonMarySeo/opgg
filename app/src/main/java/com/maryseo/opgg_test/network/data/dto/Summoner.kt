package com.maryseo.opgg_test.network.data.dto

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

data class League(
    val hasResult: Boolean,
    val wins: Int,
    val losses: Int,
    val tierRank: Tier
) {
    val rate = (wins.toFloat() / (wins + losses) * 100).toInt()
}

data class Tier(
    val name: String,
    val tier: String,
    val tierDivision: String,
    val string: String,
    val shortString: String,
    val division: String,
    val imageUrl: String,
    val lp: Int,
    val tierRankPoint: Int,
    val season: Int? = null
) {
    override fun equals(other: Any?): Boolean {
        return if (other is Tier) {
            other.name == this.name && other.tier == this.tier
        } else {
            false
        }
    }
}

data class LadderRank(
    val rank: Int = 0,
    val rankPercentOfTop: Int = 0
)