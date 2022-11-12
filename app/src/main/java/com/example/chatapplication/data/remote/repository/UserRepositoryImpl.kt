package com.example.chatapplication.data.remote.repository

import android.util.Log
import com.example.chatapplication.common.Constants.COLLECTION_USER
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Constants.UNKNOWN_ERROR
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.User
import com.example.chatapplication.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth): UserRepository {
    private var user: User? = null

    override suspend fun getCurrentUser(): Flow<Resource<User>> = flow{
        try{
            emit(Resource.Loading)
            getUser()
            user?.let { emit(Resource.Success(it)) }
        }
        catch (e: Exception){
            Log.e(TAG, "getCurrentUser: ", e)
            emit(Resource.Error(e.localizedMessage?: UNKNOWN_ERROR))
        }
    }

    override suspend fun getUser(): User {
        if(user == null){
            auth.currentUser?.uid?.let {
                user = firestore.collection(COLLECTION_USER).document(it).get().await().toObject(User::class.java)
            }
        }
        return user!!
    }


}
