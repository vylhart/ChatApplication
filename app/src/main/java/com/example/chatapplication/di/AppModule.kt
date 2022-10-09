package com.example.chatapplication.di

import android.app.Application
import androidx.room.Room
import com.example.chatapplication.common.Constants
import com.example.chatapplication.data.local.MessageDatabase
import com.example.chatapplication.data.local.repository.MessageLocalRepositoryImpl
import com.example.chatapplication.data.remote.repository.MessageRepositoryImpl
import com.example.chatapplication.domain.repository.MessageRepository
import com.example.chatapplication.domain.use_cases.DeleteMessage
import com.example.chatapplication.domain.use_cases.GetMessages
import com.example.chatapplication.domain.use_cases.SendMessage
import com.example.chatapplication.domain.use_cases.UseCases
import com.google.firebase.firestore.CollectionReference
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
    fun providesCollection() = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_MESSAGE)

    @Singleton
    @Provides
    fun provideMessageRepo(collection: CollectionReference): MessageRepository {
        return MessageRepositoryImpl(collection)
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

    @Singleton
    @Provides
    fun provideMessageDatabase(app: Application): MessageDatabase{
        return Room.databaseBuilder(app, MessageDatabase::class.java, Constants.DATABASE_NAME).build()
    }
/*
    @Singleton
    @Provides
    fun provideMessageLocalRepository(db: MessageDatabase): MessageRepository {
        return MessageLocalRepositoryImpl(db.messageDao)
    }*/
}