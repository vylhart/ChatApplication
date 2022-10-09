package com.example.chatapplication.data.remote.repository

import android.util.Log
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.util.Listener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.tasks.await

class MessageRepositoryImpl (private val collection: CollectionReference): MessageRepository {

    override suspend fun getMessages(channel: String): Flow<List<Message>> = flow {

        val ref = collection.document(channel).collection(Constants.COLLECTION_CHANNEL).orderBy("date")
        val list = mutableListOf<Message>()
        ref.addSnapshotListener{ snapshot, e ->
            snapshot?.let {
                for(item in snapshot){
                    val msg = item.toObject(Message::class.java)
                    list.add(msg)
                }
            }

        }
        /*val list = collection
            .document(channel)
            .collection(Constants.COLLECTION_CHANNEL)
            .orderBy("date")
            .get()
            .await()
            .map {
                it.toObject(Message::class.java)
        }*/
        emit(list)
        list.clear()
    }

    override suspend fun deleteMessage(message: Message){
        collection
            .document(message.channelId)
            .collection(Constants.COLLECTION_CHANNEL)
            .document(message.messageId)
            .delete()
    }

    override suspend fun sendMessage(message: Message){
        collection.document(message.channelId).collection(Constants.COLLECTION_CHANNEL).document(message.messageId).set(message)
        .addOnFailureListener {
            Log.i(TAG, "sendMessage: "+it.localizedMessage)
        }.addOnSuccessListener {
            Log.i(TAG, "sendMessage: success")
        }
    }
}