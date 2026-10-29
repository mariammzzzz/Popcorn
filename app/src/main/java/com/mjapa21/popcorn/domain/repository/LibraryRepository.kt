package com.mjapa21.popcorn.domain.repository

import com.mjapa21.popcorn.domain.model.Movie
import com.mjapa21.popcorn.domain.model.SavedMovie
import com.mjapa21.popcorn.domain.model.WatchStatus
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun observeLibrary(): Flow<List<SavedMovie>>
    suspend fun save(movie: Movie, status: WatchStatus)
    suspend fun updateStatus(movieId: String, status: WatchStatus)
    suspend fun remove(movieId: String)
}