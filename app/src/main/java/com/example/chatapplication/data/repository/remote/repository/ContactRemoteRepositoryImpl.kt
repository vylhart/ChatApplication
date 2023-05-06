package com.example.chatapplication.data.repository.remote.repository

import android.app.Application
import android.util.Log
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Constants.PHONE_NUMBER
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.getPhoneContacts
import com.example.chatapplication.data.model.User
import com.example.chatapplication.data.model.toContact
import com.example.chatapplication.domain.model.Contact
import com.example.chatapplication.domain.repository.ContactRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.tasks.await

class ContactRemoteRepositoryImpl(
    private val app: Application,
    private val firestore: FirebaseFirestore,
) : ContactRepository {

    override suspend fun getContacts() = flow {
        emit(
            supervisorScope {
                val deferred = getPhoneContacts(app).map { contact ->
                    async {
                        return@async try {
                            val users: MutableList<User> = firestore
                                .collection(Constants.COLLECTION_USER)
                                .whereEqualTo(PHONE_NUMBER, contact.number).limit(1)
                                .get().await().toObjects(User::class.java)
                            if (users.isNotEmpty()) users[0].toContact() else null
                        } catch (e: Exception) {
                            Log.e(TAG, "getContacts: ", e)
                            null
                        }
                    }
                }
                deferred.awaitAll().filterNotNull()
            }
        )
    }

    override fun getContact(userID: String): Contact {
        TODO("Not yet implemented")
    }

    override suspend fun addContact(contact: Contact) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteContact(contact: Contact) {
        TODO("Not yet implemented")
    }

}