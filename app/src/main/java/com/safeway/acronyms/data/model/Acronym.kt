package com.safeway.acronyms.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AcronymResponse(
    @SerialName("sf") val sf: String,
    @SerialName("lfs") val lfs: List<Lfs>
)

@Serializable
data class Lfs(
    @SerialName("lf") val lf: String,
    @SerialName("freq") val freq: Int,
    @SerialName("since") val since: Int,
    @SerialName("vars") val vars: List<Vars>
)

@Serializable
data class Vars(
    @SerialName("lf") val lf: String,
    @SerialName("freq") val freq: Int,
    @SerialName("since") val since: Int
)
