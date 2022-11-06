package com.example.chatapplication.di

import com.example.chatapplication.data.remote.repository.ChannelRepositoryImpl
import com.example.chatapplication.domain.repository.ChannelRepository
import com.example.chatapplication.domain.use_cases.channel_use_cases.ChannelUseCases
import com.example.chatapplication.domain.use_cases.channel_use_cases.GetChannelsFromNetwork
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChannelModule {

    @Singleton
    @Provides
    fun provideRemoteChannelRepository(firestore: FirebaseFirestore): ChannelRepository {
        return ChannelRepositoryImpl(firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideChannelUseCases(repository: ChannelRepository): ChannelUseCases {
        return ChannelUseCases(
            getChannelsFromNetwork = GetChannelsFromNetwork(repository = repository)
        )
    }
}