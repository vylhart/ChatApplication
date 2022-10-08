package com.example.chatapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chatapplication.domain.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao{
    @Query("SELECT * FROM message WHERE channelId = :id")
    fun getMessages(id: String): Flow<List<Message>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(message: Message)

    @Delete
    suspend fun deleteMessage(message: Message)

}