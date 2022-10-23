package com.example.chatapplication.domain.use_cases

data class UseCases(
    val deleteMessage: DeleteMessage,
    val sendMessage: SendMessage,
    val getMessages: GetMessages,
    val fetchMessages: FetchMessages
)
