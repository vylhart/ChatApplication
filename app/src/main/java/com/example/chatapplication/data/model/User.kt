package com.example.chatapplication.data.model

import androidx.room.Entity
import com.example.chatapplication.domain.model.Contact

@Entity
@kotlinx.serialization.Serializable
data class User(
    val uid: String = "",
    val createdAt: Long = 0,
    val name: String = "",
    val photoURL: String? = null,
    var phoneNumber: String = "",
    val channelIDs: HashMap<String, String> = HashMap(),
)

fun User.toContact(): Contact {
    return Contact(this.uid, this.phoneNumber, this.name, this.photoURL)
}
