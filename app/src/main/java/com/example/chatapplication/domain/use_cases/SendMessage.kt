package com.example.chatapplication.domain.use_cases

import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessage @Inject constructor(private val repository: MessageRepository) {
    operator fun invoke(message: Message) = repository.sendMessage(message)
}