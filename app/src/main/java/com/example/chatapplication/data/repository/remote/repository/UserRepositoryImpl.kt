package com.example.chatapplication.data.repository.remote.repository

import android.util.Log
import com.example.chatapplication.common.Constants.COLLECTION_USER
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Constants.UNKNOWN_ERROR
import com.example.chatapplication.common.Resource
import com.example.chatapplication.data.model.User
import com.example.chatapplication.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : UserRepository {
    private var user: User? = null

    override suspend fun getCurrentUser(): Flow<Resource<User?>> = flow {
        try {
            if (user != null) {
                emit(Resource.Success(user))
            } else {
                auth.currentUser?.uid?.let { getUser(it) }
                emit(Resource.Success(user))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getCurrentUser: ", e)
            emit(Resource.Error(e.localizedMessage ?: UNKNOWN_ERROR))
        }
    }

    override suspend fun addNewUser(name: String): Flow<Resource<Boolean>> = flow {
        try {
            auth.currentUser?.apply {
                user = User(
                    uid = uid,
                    createdAt = System.currentTimeMillis(),
                    name = name,
                    phoneNumber = phoneNumber!!
                )
                firestore.collection(COLLECTION_USER).document(uid).set(user!!).await()
            }
            emit(Resource.Success(true))
        } catch (e: Exception) {
            Log.e(TAG, "getCurrentUser: ", e)
            emit(Resource.Error(e.localizedMessage ?: UNKNOWN_ERROR))
        }
    }

    override suspend fun getUser(uid: String): User? {
        user = firestore.collection(COLLECTION_USER).document(uid).get().await()
            .toObject(User::class.java)
        return user
    }


}
