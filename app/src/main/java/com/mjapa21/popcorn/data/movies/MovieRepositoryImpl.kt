package com.mjapa21.popcorn.data.movies

import com.mjapa21.popcorn.data.mapper.toDomain
import com.mjapa21.popcorn.domain.model.MovieSearchResult
import com.mjapa21.popcorn.domain.repository.MovieRepository
import kotlinx.coroutines.CancellationException

class MovieRepositoryImpl(private val movieApi: MovieApi) : MovieRepository {
    override suspend fun searchMovies(title: String, page: Int): Result<MovieSearchResult> {
        return try {
            val response = movieApi.searchMovies(title, page)
            if (response.response == "True") {
                Result.success(response.toDomain())
            } else {
                Result.failure(Exception(response.error ?: "Unknown error"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            Result.failure(Exception("Couldn't fetch a movie"))
        }
    }
}
