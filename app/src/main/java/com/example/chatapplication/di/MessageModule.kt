package com.example.chatapplication.di

import android.app.Application
import androidx.room.Room
import com.example.chatapplication.common.Constants
import com.example.chatapplication.data.local.MessageDatabase
import com.example.chatapplication.data.local.repository.MessageLocalRepositoryImpl
import com.example.chatapplication.data.remote.repository.MessageRemoteRepositoryImpl
import com.example.chatapplication.data.worker.WorkerUtils
import com.example.chatapplication.domain.repository.MessageRepository
import com.example.chatapplication.domain.use_cases.chat_use_cases.*
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MessageModule {

    @Singleton
    @Provides
    fun provideUseCases(@Named("Local") repository: MessageRepository, @Named("Remote") remoteRepository: MessageRepository, workerUtils: WorkerUtils): ChatUseCases {
        return ChatUseCases(
            deleteMessage = DeleteMessage(repository, workerUtils),
            sendMessage = SendMessage(repository, workerUtils),
            getMessagesFromLocalDB = GetMessagesFromLocalDB(repository),
            getMessagesFromNetwork = GetMessagesFromNetwork(repository, remoteRepository)
        )
    }

    @Singleton
    @Provides
    fun provideMessageDatabase(app: Application): MessageDatabase{
        return Room.databaseBuilder(app, MessageDatabase::class.java, Constants.DATABASE_NAME).build()
    }

    @Singleton
    @Provides
    @Named("Local")
    fun provideLocalMessageRepository(db: MessageDatabase): MessageRepository {
        return MessageLocalRepositoryImpl(db.messageDao)
    }

    @Singleton
    @Provides
    @Named("Remote")
    fun provideRemoteMessageRepository(collection: CollectionReference): MessageRepository {
        return MessageRemoteRepositoryImpl(collection)
    }


}