package com.example.chatapplication.data.repository.local

import androidx.room.*
import com.example.chatapplication.domain.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact")
    fun getContacts(): Flow<List<Contact>>

    @Query("SELECT * FROM contact where uid = :userID")
    fun getContact(userID: String): Contact

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)
}