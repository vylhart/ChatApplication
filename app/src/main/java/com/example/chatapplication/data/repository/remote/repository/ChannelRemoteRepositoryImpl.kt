package com.example.chatapplication.data.repository.remote.repository

import android.util.Log
import com.example.chatapplication.common.Constants.COLLECTION_CHANNEL
import com.example.chatapplication.common.Constants.COLLECTION_USER
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.data.model.ChannelID
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.repository.ChannelRepository
import com.example.chatapplication.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class ChannelRemoteRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
) : ChannelRepository {

    private val idList = mutableListOf<ChannelID>()

    private fun updateChannelIDs(): Flow<Boolean> = callbackFlow {
        try{
            Log.d(TAG, "getChannels: enter ${auth.currentUser?.uid}" )
            firestore.collection(COLLECTION_USER)
                .document(auth.currentUser!!.uid)
                .collection(COLLECTION_CHANNEL).addSnapshotListener { s,e ->
                    idList.clear()
                    s?.map {
                        val channelID: ChannelID = it.toObject(ChannelID::class.java)
                        idList.add(channelID)
                    }
                    Log.d(TAG, "getChannelIDs: updated")
                    trySend(true)
                }
        }
        catch (e: Exception){
            Log.e(TAG, "updateChannelIDs: ", e)
        }
        awaitClose { close() }
    }

    private suspend fun getChannelsList() = coroutineScope {
        val deferred = idList.map { id ->
            async {
                val channel =  firestore
                    .collection(COLLECTION_CHANNEL)
                    .document(id.id)
                    .get().await()
                    .toObject(Channel::class.java)
                Log.d(TAG, "getChannels: ${channel?.channelID}")
                return@async channel
            }
        }
        deferred.awaitAll().filterNotNull()
    }

    override suspend fun getChannels(): Flow<List<Channel>> = flow {
        updateChannelIDs().collect{
            val list = getChannelsList()
            Log.d(TAG, "getChannels: done")
            emit(list)
        }
    }

    override suspend fun leaveChannel(channel: Channel) {
        try {
            firestore
                .collection(COLLECTION_USER)
                .document(auth.currentUser!!.uid)
                .collection(COLLECTION_CHANNEL)
                .document()
                .delete()

            firestore
                .collection(COLLECTION_CHANNEL)
                .document(channel.channelID)
                .collection(COLLECTION_USER)
                .document(auth.currentUser!!.uid)
                .delete()

        }
        catch (e: Exception){
            Log.e(TAG, "leaveChannel: ", e)
        }
    }

    override suspend fun getChannel(channelID: String): Channel {
        var channel = firestore.collection(COLLECTION_CHANNEL).document(channelID).get().await().toObject<Channel>()
        if(channel == null){
            Log.d(TAG, "getChannel: new")
            channel = Channel(
                channelID = channelID,
                dateCreated = System.currentTimeMillis(),
            )
            firestore.collection(COLLECTION_CHANNEL).document(channelID).set(channel).await()
        }
        return channel
    }

    override suspend fun joinChannel(channel: Channel) {
        try {
            Log.d(TAG, "joinChannel: ")
            firestore
                .collection(COLLECTION_USER)
                .document(auth.currentUser!!.uid)
                .collection(COLLECTION_CHANNEL)
                .document(channel.channelID)
                .set(ChannelID(id = channel.channelID))

            userRepository.getCurrentUser().collect{
                if(it is Resource.Success){
                    val user = it.data
                    if(user !in channel.users)
                        channel.users.add(user!!)
                }
            }
            firestore
                .collection(COLLECTION_CHANNEL)
                .document(channel.channelID)
                .set(channel)

        }
        catch (e: Exception){
            Log.e(TAG, "joinChannel: ", e)
        }
    }
}


