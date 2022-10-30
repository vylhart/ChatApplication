package com.example.chatapplication.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.data.worker.WorkerUtils.Companion.ACTION_DELETE
import com.example.chatapplication.data.worker.WorkerUtils.Companion.ACTION_FETCH
import com.example.chatapplication.data.worker.WorkerUtils.Companion.ACTION_SEND
import com.example.chatapplication.data.worker.WorkerUtils.Companion.KEY_ACTION
import com.example.chatapplication.data.worker.WorkerUtils.Companion.KEY_CHANNELID
import com.example.chatapplication.data.worker.WorkerUtils.Companion.KEY_DATA
import com.example.chatapplication.data.worker.WorkerUtils.Companion.KEY_DATE
import com.example.chatapplication.data.worker.WorkerUtils.Companion.KEY_MESSAGEID
import com.example.chatapplication.data.worker.WorkerUtils.Companion.KEY_SENDERID
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Named

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    @Named("Remote") private val remoteRepository: MessageRepository,
    @Named("Local")  private val localRepository:  MessageRepository
    ): CoroutineWorker(context, params) {


    override suspend fun doWork(): Result {
        try {
            val action = inputData.getString(KEY_ACTION)
            val messageId = inputData.getString(KEY_MESSAGEID)
            val channelId = inputData.getString(KEY_CHANNELID)
            val senderId = inputData.getString(KEY_SENDERID)
            val data = inputData.getString(KEY_DATA)
            val date = inputData.getLong(KEY_DATE, 0)
            Log.d(TAG, "doWork: $action")

            channelId?.let{
                return when (action) {
                    ACTION_SEND -> {
                        if (messageId != null && senderId != null && data != null) {
                            val message = Message(messageId = messageId, channelId = it, senderId= senderId, data = data, date = date)
                            remoteRepository.sendMessage(message)
                        }
                        Result.success()
                    }
                    ACTION_DELETE -> {
                        if (messageId != null && senderId != null && data != null) {
                            val message = Message(messageId = messageId, channelId = it, senderId= senderId, data = data, date = date)
                            remoteRepository.deleteMessage(message)
                        }
                        Result.success()
                    }
                    ACTION_FETCH -> {
                        remoteRepository.getMessages(it).collect{ list ->
                            list.forEach{ msg ->
                                Log.d(TAG, "doWork: fetching -> ${msg.data}")
                                localRepository.sendMessage(msg)
                            }
                        }
                        Result.success()
                    }
                    else -> {
                        Result.failure()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
        return Result.failure()
    }
}
