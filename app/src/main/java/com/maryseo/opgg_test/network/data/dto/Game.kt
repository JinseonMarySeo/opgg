package com.maryseo.opgg_test.network.data.dto

data class Game(
    val champion: GameChampion,
    val createDate: Long,
    val gameId: String,
    val gameLength: Int,
    val gameType: String,
    val isWin: Boolean,
    val items: List<Item>,
    val mapInfo: Any,
    val mmr: Int,
    val needRenew: Boolean,
    val peak: List<String>,
    val spells: List<Spell>,
    val stats: Stats,
    val summonerId: String,
    val summonerName: String,
    val tierRankShort: String
)

data class GameChampion(
    val imageUrl: String,
    val level: Int
)

data class Spell(
    val imageUrl: String
)

data class Stats(
    val general: General,
    val ward: Ward
)

data class Item(
    val imageUrl: String
)

data class General(
    val assist: Int,
    val contributionForKillRate: String,
    val cs: Int,
    val csPerMin: Double,
    val death: Int,
    val goldEarned: Int,
    val kdaString: String,
    val kill: Int,
    val largestMultiKillString: String,
    val opScoreBadge: String,
    val totalDamageDealtToChampions: Int
)

data class Ward(
    val sightWardsBought: Int,
    val visionWardsBought: Int
)