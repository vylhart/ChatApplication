package com.example.chatapplication.data.repository

import android.util.Log
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import com.google.firebase.firestore.CollectionReference

import kotlinx.coroutines.tasks.await

class MessageRepositoryImpl (private val collection: CollectionReference): MessageRepository {

    override suspend fun getMessages(channel: String): List<Message> {
        val list = collection
            .document(channel)
            .collection(Constants.COLLECTION_CHANNEL)
            .get()
            .await()
            .map {
                it.toObject(Message::class.java)
        }
        return list
    }

    override suspend fun deleteMessage(channel: String, messageId: String){
        collection
            .document(channel)
            .collection(Constants.COLLECTION_CHANNEL)
            .document(messageId)
            .delete()
    }

    override suspend fun sendMessage(channel: String, message: Message){
        collection.document(channel).collection(Constants.COLLECTION_CHANNEL).document(message.messageId).set(message)
        .addOnFailureListener {
            Log.i(TAG, "sendMessage: "+it.localizedMessage)
        }.addOnSuccessListener {
            Log.i(TAG, "sendMessage: success")
        }
    }
}