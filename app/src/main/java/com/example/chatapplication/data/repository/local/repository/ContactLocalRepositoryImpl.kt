package com.example.chatapplication.data.repository.local.repository


import com.example.chatapplication.data.repository.local.ContactDao
import com.example.chatapplication.domain.model.Contact


class ContactRepositoryImpl(private val contactDao: ContactDao) {

    fun getContacts() {
        contactDao.getContacts()
    }

    suspend fun addContact(contact: Contact) {
        contactDao.addContact(contact)
    }

}