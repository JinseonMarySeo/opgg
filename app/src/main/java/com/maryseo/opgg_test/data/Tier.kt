package com.maryseo.opgg_test.data

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
