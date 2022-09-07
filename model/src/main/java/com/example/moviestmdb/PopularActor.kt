package com.example.moviestmdb

import com.google.gson.annotations.SerializedName

data class PopularActor(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("known_for")
    val known_for: List<KnownFor>,
    @SerializedName("name")
    val name: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("profile_path")
    val profile_path: String
) {

    fun getTitles() : String {
        val titleList = mutableListOf<String>()
        known_for.forEach{
            titleList.add(it.name)
        }
        return titleList.joinToString(", ")
    }

    val knownForTitles: String
        get() = "${
            StringBuilder().apply {
                known_for.forEach {
                    append(it.name)
                    append(", ")
                }
            }
        }".dropLast(1)
}
