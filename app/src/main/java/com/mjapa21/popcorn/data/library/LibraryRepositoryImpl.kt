package com.mjapa21.popcorn.data.library

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.mjapa21.popcorn.data.mapper.toDomain
import com.mjapa21.popcorn.data.model.SavedMovieDto
import com.mjapa21.popcorn.domain.model.Movie
import com.mjapa21.popcorn.domain.model.SavedMovie
import com.mjapa21.popcorn.domain.model.WatchStatus
import com.mjapa21.popcorn.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LibraryRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : LibraryRepository {

    private fun moviesRef(): CollectionReference {
        val uid = auth.currentUser?.uid ?: error("Not signed in")
        return firestore.collection("users").document(uid).collection("movies")
    }

    override fun observeLibrary(): Flow<List<SavedMovie>> =
        moviesRef().snapshots().map { snapshot ->
            snapshot.documents
                .mapNotNull { it.toObject<SavedMovieDto>()?.toDomain(it.id) }
                .sortedByDescending { it.addedAt }
        }

    override suspend fun save(movie: Movie, status: WatchStatus) {
        val dto = SavedMovieDto(
            title = movie.title,
            year = movie.year,
            posterUrl = movie.posterUrl,
            type = movie.type.name,
            status = status.name,
            addedAt = System.currentTimeMillis(),
        )
        moviesRef().document(movie.id).set(dto)
    }

    override suspend fun updateStatus(movieId: String, status: WatchStatus) {
        moviesRef().document(movieId).update("status", status.name)
    }

    override suspend fun remove(movieId: String) {
        moviesRef().document(movieId).delete()
    }
}