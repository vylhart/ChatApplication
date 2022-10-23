package com.example.chatapplication.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.chatapplication.common.Constants
import com.example.chatapplication.data.local.MessageDatabase
import com.example.chatapplication.data.local.repository.MessageLocalRepositoryImpl
import com.example.chatapplication.data.remote.repository.MessageRemoteRepositoryImpl
import com.example.chatapplication.data.worker.WorkerUtils
import com.example.chatapplication.domain.repository.MessageRepository
import com.example.chatapplication.domain.use_cases.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providesUseCases(repository: MessageRepository, remoteRepository: MessageRemoteRepositoryImpl, workerUtils: WorkerUtils): UseCases {
        return UseCases(
            deleteMessage = DeleteMessage(repository, workerUtils),
            sendMessage = SendMessage(repository, workerUtils),
            getMessages = GetMessages(repository, workerUtils),
            fetchMessages = FetchMessages(repository, remoteRepository)
        )
    }

    @Singleton
    @Provides
    fun provideMessageDatabase(app: Application): MessageDatabase{
        return Room.databaseBuilder(app, MessageDatabase::class.java, Constants.DATABASE_NAME).build()
    }

    @Singleton
    @Provides
    fun provideLocalMessageRepository(db: MessageDatabase): MessageRepository {
        return MessageLocalRepositoryImpl(db.messageDao)
    }

    @Singleton
    @Provides
    fun provideRemoteMessageRepository(collection: CollectionReference): MessageRemoteRepositoryImpl {
        return MessageRemoteRepositoryImpl(collection)
    }

    @Singleton
    @Provides
    fun provideWorkerRepo(@ApplicationContext context: Context): WorkerUtils {
        return WorkerUtils(context)
    }
}