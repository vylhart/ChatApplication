package com.example.chatapplication.data.remote.repository

import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.repository.ChannelRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ChannelRepositoryImpl(private val firestore: FirebaseFirestore) : ChannelRepository {
    override suspend fun getChannels(userID: String) = callbackFlow {
        try{
            firestore.collection("users").document(userID).collection("channel").addSnapshotListener{ snapshot, _ ->
                snapshot?.let {
                    val list = mutableListOf<Channel>()
                    for(item in it){
                        val channel: Channel = item.toObject(Channel::class.java)
                        list.add(channel)
                    }
                    trySend(Resource.Success(data = list))
                }
            }
        }
        catch (e: Exception){
            trySend(Resource.Error(e.localizedMessage?: "Something went wrong"))
        }
        awaitClose {  }
    }
}