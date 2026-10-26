package com.mjapa21.popcorn.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SearchResponseDto(
    @SerializedName("Search") val search: List<MovieDto>?,
    @SerializedName("totalResults") val totalResults: String?,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String?,
)

@Keep
data class MovieDto(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("imdbID") val imdbId: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster: String,
)