package com.example.chatapplication.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity
class Channel(
    @PrimaryKey
    val channelID: String = "",
    val users: MutableList<User> = mutableListOf(),
    //val lastMessage: Message = Message(),
    val dateCreated: Long = 0L,
    val dateModified: Long = 0L
)

class ListConverter{

    @TypeConverter
        fun listToString(list: MutableList<User>?): String {
            return Json.encodeToString(list)
        }

        @TypeConverter
        fun stringToList(string: String?): MutableList<User>? {
            return string?.let { Json.decodeFromString<List<User>>(it).toMutableList() }
        }

}