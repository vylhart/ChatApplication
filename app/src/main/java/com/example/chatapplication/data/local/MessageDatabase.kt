package com.example.chatapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.model.ListConverter
import com.example.chatapplication.domain.model.Message

@Database(entities = [Message::class, Channel::class], version = 1)
@TypeConverters(ListConverter::class)
abstract class MessageDatabase: RoomDatabase() {
    abstract val messageDao: MessageDao
    abstract val channelDao: ChannelDao


}