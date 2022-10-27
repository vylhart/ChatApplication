package com.example.chatapplication.domain.use_cases.chat_use_cases

data class ChatUseCases(
    val deleteMessage: DeleteMessage,
    val sendMessage: SendMessage,
    val getMessages: GetMessages,
    val fetchMessages: FetchMessages
)
