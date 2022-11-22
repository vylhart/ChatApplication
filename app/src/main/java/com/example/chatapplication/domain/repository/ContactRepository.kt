package com.example.chatapplication.domain.repository

import com.example.chatapplication.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun getContacts(): Flow<List<Contact>>
    fun getContact(userID: String): Contact
    suspend fun addContact(contact: Contact)
    suspend fun deleteContact(contact: Contact)
}