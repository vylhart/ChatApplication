package com.example.chatapplication.di

import android.app.Application
import com.example.chatapplication.common.Constants
import com.example.chatapplication.data.repository.local.database.MessageDatabase
import com.example.chatapplication.data.repository.local.repository.ContactLocalRepositoryImpl
import com.example.chatapplication.data.repository.remote.repository.ContactRemoteRepositoryImpl
import com.example.chatapplication.data.worker.WorkerUtils
import com.example.chatapplication.domain.repository.ContactRepository
import com.example.chatapplication.domain.use_cases.contact_use_cases.ContactUseCases
import com.example.chatapplication.domain.use_cases.contact_use_cases.GetContacts
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ContactModule {

    @Singleton
    @Provides
    @Named(Constants.LOCAL)
    fun provideLocalContactRepository(db: MessageDatabase): ContactRepository {
        return ContactLocalRepositoryImpl(db.contactDao)
    }

    @Singleton
    @Provides
    @Named(Constants.REMOTE)
    fun provideRemoteContactRepository(
        application: Application,
        firestore: FirebaseFirestore,
    ): ContactRepository {
        return ContactRemoteRepositoryImpl(application, firestore)
    }

    @Singleton
    @Provides
    fun provideContactUseCases(
        @Named(Constants.LOCAL) repository: ContactRepository,
        workerUtils: WorkerUtils,
    ): ContactUseCases {
        return ContactUseCases(
            getContacts = GetContacts(repository = repository, workerUtils = workerUtils)
        )
    }
}