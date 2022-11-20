package com.example.chatapplication.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.model.Contact
import com.example.chatapplication.domain.model.ListConverter
import com.example.chatapplication.domain.model.Message

@Database(entities = [Message::class, Channel::class, Contact::class], version = 1)
@TypeConverters(ListConverter::class)
abstract class MessageDatabase: RoomDatabase() {
    abstract val messageDao: MessageDao
    abstract val channelDao: ChannelDao
}