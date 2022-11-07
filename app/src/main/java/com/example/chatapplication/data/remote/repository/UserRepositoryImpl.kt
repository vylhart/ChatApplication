package com.example.chatapplication.data.remote.repository

import android.util.Log
import com.example.chatapplication.common.Constants.COLLECTION_CHANNEL
import com.example.chatapplication.common.Constants.COLLECTION_USER
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Constants.UNKNOWN_ERROR
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.model.User
import com.example.chatapplication.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): UserRepository {

    private var user: User? = null

    private suspend fun setUser() {
        if(user == null){
            auth.currentUser?.uid?.let {
                user = firestore.collection(COLLECTION_USER).document(it).get().await().toObject(User::class.java)
            }
        }
    }

    override suspend fun getCurrentUser(): Flow<Resource<User>> = flow{
        try{
            emit(Resource.Loading)
            setUser()
            user?.let { emit(Resource.Success(it)) }
        }
        catch (e: Exception){
            Log.e(TAG, "getCurrentUser: ", e)
            emit(Resource.Error(e.localizedMessage?: UNKNOWN_ERROR))
        }
    }

    private suspend fun getChannel(channelID: String): Channel {
        var channel = firestore.collection(COLLECTION_CHANNEL).document(channelID).get().await().toObject<Channel>()
        if(channel == null){
            channel = Channel(
                channelID = channelID,
                dateCreated = System.currentTimeMillis(),
                dateModified = System.currentTimeMillis()
            )
        }
        firestore.collection(COLLECTION_CHANNEL).document(channelID).set(channel).await()
        return channel
    }

    override suspend fun joinChannel(channelID: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading)
            setUser()
            user?.let {
                it.channelIDs.add(channelID)
                val channel = getChannel(channelID)
                channel.users.add(user!!)
                firestore.collection(COLLECTION_CHANNEL).document(channelID).set(channel)
                firestore.collection(COLLECTION_USER).document(it.uid).set(it).await()
            }
            emit(Resource.Success(true))
        }
        catch (e: Exception){
            Log.e(TAG, "joinChannel: ", e)
            emit(Resource.Error(e.localizedMessage?: UNKNOWN_ERROR))
        }
    }

    override suspend fun getChannels() = callbackFlow {
        try{
            setUser()
            user?.let { it ->
                Log.d(TAG, "getChannels: here")
                val list = mutableListOf<Channel>()
                for (id in it.channelIDs){
                    Log.d(TAG, "getChannels: $id")
                    launch {
                        val channel: Channel? = firestore.collection(COLLECTION_CHANNEL).document(id).get().await().toObject<Channel>()
                        channel?.let { it1 -> list.add(it1) }
                        trySend(Resource.Success(data = list))
                    }
                }
            }
        }
        catch (e: Exception){
            trySend(Resource.Error(e.localizedMessage?: UNKNOWN_ERROR))
        }
        awaitClose {  }
    }
}