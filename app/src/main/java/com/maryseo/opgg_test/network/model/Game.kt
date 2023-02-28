package com.maryseo.opgg_test.network.model

data class Game(
    val champion: ChampionX,
    val createDate: Int,
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

data class ChampionX(
    val imageUrl: String,
    val level: Int
)