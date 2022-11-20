package com.example.chatapplication.data.repository.remote.repository

import android.util.Log
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Constants.COLLECTION_CHANNEL
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MessageRemoteRepositoryImpl (private val firestore: FirebaseFirestore): MessageRepository {

    override suspend fun getMessages(channel: String): Flow<List<Message>> = callbackFlow {
        firestore.collection(COLLECTION_CHANNEL)
            .document(channel)
            .collection(Constants.COLLECTION_MESSAGE)
            .orderBy("date")
            .addSnapshotListener{ snapshot, _ ->
                snapshot?.let {
                    val list = mutableListOf<Message>()
                    for(item in it){
                        val msg: Message = item.toObject(Message::class.java)
                        //Log.d(TAG, "fetchMessages: ${msg.data}")
                        list.add(msg)
                    }
                    trySend(list)
                }
            }
        awaitClose {  }
    }


    override suspend fun deleteMessage(message: Message){
        firestore.collection(COLLECTION_CHANNEL)
            .document(message.channelId)
            .collection(Constants.COLLECTION_MESSAGE)
            .document(message.messageId)
            .delete()
    }

    override suspend fun sendMessage(message: Message){
        firestore.collection(COLLECTION_CHANNEL)
            .document(message.channelId)
            .collection(Constants.COLLECTION_MESSAGE)
            .document(message.messageId)
            .set(message)
            .addOnFailureListener {
                Log.i(TAG, "sendMessage: "+it.localizedMessage)
            }.addOnSuccessListener {
                Log.i(TAG, "sendMessage: success")
            }
    }
}