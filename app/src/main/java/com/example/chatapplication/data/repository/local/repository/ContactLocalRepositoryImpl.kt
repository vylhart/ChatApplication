package com.example.chatapplication.data.repository.local.repository


import com.example.chatapplication.data.repository.local.dao.ContactDao
import com.example.chatapplication.domain.model.Contact
import com.example.chatapplication.domain.repository.ContactRepository


class ContactLocalRepositoryImpl(private val contactDao: ContactDao): ContactRepository {
    override suspend fun getContacts() = contactDao.getContacts()
    override fun getContact(userID: String) = contactDao.getContact(userID)
    override suspend fun addContact(contact: Contact) = contactDao.addContact(contact)
    override suspend fun deleteContact(contact: Contact) = contactDao.deleteContact(contact)
}