package com.example.chatapplication.data.repository.local

import androidx.room.*
import com.example.chatapplication.domain.model.Channel
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {
    @Query("SELECT * FROM channel")
    fun getChannels(): Flow<List<Channel>>

    @Query("SELECT * FROM channel where channelID = :channelID")
    fun getChannel(channelID: String): Channel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun joinChannel(channel: Channel)

    @Delete
    suspend fun leaveChannel(channel: Channel)
}