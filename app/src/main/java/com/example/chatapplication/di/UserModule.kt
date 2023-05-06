package com.example.chatapplication.di

import com.example.chatapplication.data.repository.remote.repository.UserRepositoryImpl
import com.example.chatapplication.domain.repository.UserRepository
import com.example.chatapplication.domain.use_cases.user_use_cases.AddNewUser
import com.example.chatapplication.domain.use_cases.user_use_cases.GetCurrentUser
import com.example.chatapplication.domain.use_cases.user_use_cases.UserUseCases
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Singleton
    @Provides
    fun provideRemoteUserRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
    ): UserRepository {
        return UserRepositoryImpl(firestore = firestore, auth = auth)
    }

    @Singleton
    @Provides
    fun provideUserUseCases(repository: UserRepository): UserUseCases {
        return UserUseCases(
            getCurrentUser = GetCurrentUser(repository = repository),
            addNewUser = AddNewUser(repository = repository)
        )
    }
}