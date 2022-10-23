package com.example.chatapplication.data.worker

import android.content.Context
import androidx.work.*
import com.example.chatapplication.domain.model.Message

import javax.inject.Inject


class WorkerUtils @Inject constructor(private val context: Context){
    companion object{
        const val ACTION_SEND = "send"
        const val ACTION_FETCH = "fetch"
        const val ACTION_DELETE = "delete"
        const val KEY_ACTION = "action"
        const val KEY_CHANNELID = "channelId"
        const val KEY_SENDERID = "senderId"
        const val KEY_MESSAGEID = "messageId"
        const val KEY_DATA = "data"
        const val KEY_DATE = "date"
    }
    private val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    private val workManager = WorkManager.getInstance(context)

    fun getMessages(channelId: String) {
        val data = workDataOf(KEY_ACTION to ACTION_FETCH, KEY_CHANNELID to channelId)
        val fetchWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()
        workManager.enqueue(fetchWorkRequest)
    }

    fun deleteMessage(message: Message) {
        val data = workDataOf(
            KEY_ACTION to ACTION_SEND,
            KEY_MESSAGEID to message.messageId,
            KEY_CHANNELID to message.channelId,
            KEY_SENDERID to message.senderId,
            KEY_DATA to message.data,
            KEY_DATE to message.date
        )
        val deleteWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()
        workManager.enqueue(deleteWorkRequest)
    }

    fun sendMessage(message: Message) {
        val data = workDataOf(
            KEY_ACTION to ACTION_SEND,
            KEY_MESSAGEID to message.messageId,
            KEY_CHANNELID to message.channelId,
            KEY_SENDERID to message.senderId,
            KEY_DATA to message.data,
            KEY_DATE to message.date
        )
        val sendWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()
        workManager.enqueue(sendWorkRequest)
    }
}