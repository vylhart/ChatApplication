package com.example.chatapplication.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.chatapplication.common.Constants.LOCAL
import com.example.chatapplication.common.Constants.REMOTE
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.domain.repository.ContactRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Named

@HiltWorker
class ContactWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    @Named(REMOTE) private val remoteRepository: ContactRepository,
    @Named(LOCAL) private val localRepository: ContactRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "doWork: ")
            remoteRepository.getContacts().collect { list ->
                list.map {
                    Log.d(TAG, "doWork: ${it.name}")
                    localRepository.addContact(it)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}
