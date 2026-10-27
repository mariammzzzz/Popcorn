package com.mjapa21.popcorn.data.authentication

import com.google.firebase.auth.FirebaseAuth
import com.mjapa21.popcorn.domain.repository.AuthRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AuthRepositoryImpl(private val firebaseAuth: FirebaseAuth) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> = suspendCancellableCoroutine { continuation ->
        firebaseAuth.signInWithEmailAndPassword(email, password).apply {
            addOnSuccessListener {
                continuation.resume(Result.success(Unit))
            }
            addOnFailureListener {
                continuation.resume(Result.failure(it))
            }
        }

////        found out that this does nothing since the coroutine is already cancelled and we kinda try to cancel it again
////        we would free up the resources here for example we would stop the firebase request if it was cancellable but that task is not cancellable once it's fired
//        continuation.invokeOnCancellation {
//            continuation.cancel()
//        }
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): Result<Unit> = suspendCancellableCoroutine { continuation ->
        firebaseAuth.createUserWithEmailAndPassword(email, password).apply {
            addOnSuccessListener {
                continuation.resume(Result.success(Unit))
            }
            addOnFailureListener {
                continuation.resume(Result.failure(it))
            }
        }

//        continuation.invokeOnCancellation {
//            continuation.cancel()
//        }
    }
}