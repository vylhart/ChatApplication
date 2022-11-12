package com.example.chatapplication.data.local

import androidx.room.*
import com.example.chatapplication.domain.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao{
    @Query("SELECT * FROM message WHERE channelId = :id")
    fun getMessages(id: String): Flow<List<Message>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Delete
    suspend fun deleteMessage(message: Message)

}