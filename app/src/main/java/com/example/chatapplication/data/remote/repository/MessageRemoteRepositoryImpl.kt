package com.example.chatapplication.data.remote.repository

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MessageRemoteRepositoryImpl (private val collection: CollectionReference, private val messageRepository: MessageRepository): MessageRepository {

    suspend fun fetchMessages(channel: String): Flow<Message> = callbackFlow {
        val ref = collection.document(channel).collection(Constants.COLLECTION_CHANNEL).orderBy("date")
        ref.addSnapshotListener{ snapshot, _ ->
            snapshot?.let {
                for(item in it){
                    val msg: Message = item.toObject(Message::class.java)
                    Log.d(TAG, "fetchMessages: ${msg.data}")
                    trySend(msg)
                    GlobalScope.launch {
                        messageRepository.sendMessage(msg)
                    }

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
        //trySend(Message()).isSuccess
        awaitClose {  }
    }

    override suspend fun getMessages(channel: String): Flow<List<Message>> = flow {
        val list = collection
            .document(channel)
            .collection(Constants.COLLECTION_CHANNEL)
            .orderBy("date")
            .get()
            .await()
            .map {
                it.toObject(Message::class.java)
        }
        emit(list)
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