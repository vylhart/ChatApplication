package com.example.chatapplication.domain.use_cases

import com.example.chatapplication.domain.repository.MessageRepository
import javax.inject.Inject

class GetMessages @Inject constructor(private val repository: MessageRepository) {
    operator fun invoke(channel: String) = repository.getMessages(channel)
}