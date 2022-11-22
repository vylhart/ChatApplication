package com.example.chatapplication.domain.use_cases.contact_use_cases

import android.util.Log
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.data.worker.WorkerUtils
import com.example.chatapplication.domain.model.Contact
import com.example.chatapplication.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow

class GetContacts(private val repository: ContactRepository, private val workerUtils: WorkerUtils) {

    suspend operator fun invoke(): Flow<List<Contact>> {
        Log.d(TAG, "invoke: getcontacts")
        workerUtils.syncContacts()
        return repository.getContacts()
    }

}

