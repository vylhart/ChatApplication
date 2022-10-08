package com.example.chatapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatapplication.domain.model.Message

@Database(entities = [Message::class], version = 1)
abstract class MessageDatabase: RoomDatabase() {
    abstract val messageDao: MessageDao


}