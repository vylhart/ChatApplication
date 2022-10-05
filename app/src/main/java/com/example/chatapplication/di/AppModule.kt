package com.example.chatapplication.di

import com.example.chatapplication.data.repository.MessageRepositoryImpl
import com.example.chatapplication.domain.repository.MessageRepository
import com.example.chatapplication.domain.use_cases.DeleteMessage
import com.example.chatapplication.domain.use_cases.GetMessages
import com.example.chatapplication.domain.use_cases.SendMessage
import com.example.chatapplication.domain.use_cases.UseCases
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideMessageRepo(firestore: FirebaseFirestore): MessageRepository {
        return MessageRepositoryImpl(firestore)
    }

    @Singleton
    @Provides
    fun providesUseCases(repository: MessageRepository): UseCases {
        return UseCases(
            deleteMessage = DeleteMessage(repository),
            sendMessage = SendMessage(repository),
            getMessages = GetMessages(repository)
        )
    }

}