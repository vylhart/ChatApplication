package com.example.chatapplication.di

import com.example.chatapplication.data.local.MessageDatabase
import com.example.chatapplication.data.local.repository.ChannelLocalRepositoryImpl
import com.example.chatapplication.data.remote.repository.ChannelRemoteRepositoryImpl
import com.example.chatapplication.domain.repository.ChannelRepository
import com.example.chatapplication.domain.repository.UserRepository
import com.example.chatapplication.domain.use_cases.channel_use_cases.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChannelModule {

    @Singleton
    @Provides
    @Named("remote")
    fun provideChannelRemoteRepository(auth: FirebaseAuth, firestore: FirebaseFirestore, userRepository: UserRepository): ChannelRepository {
        return ChannelRemoteRepositoryImpl(auth, firestore, userRepository)
    }

    @Singleton
    @Provides
    @Named("local")
    fun provideLocalMessageRepository(db: MessageDatabase): ChannelRepository {
        return ChannelLocalRepositoryImpl(db.channelDao)
    }

    @Singleton
    @Provides
    fun provideChannelUseCases(
        @Named("local") localRepository: ChannelRepository,
        @Named("remote") remoteRepository: ChannelRepository,
    ): ChannelUseCases {
        return ChannelUseCases(
            getChannelsFromNetwork = GetChannelsFromNetwork(localRepository = localRepository, remoteRepository = remoteRepository),
            getChannelsFromLocalDB = GetChannelsFromLocalDB(localRepository),
            joinChannel = JoinChannel(repository = remoteRepository),
            leaveChannel = LeaveChannel(localRepository = localRepository, remoteRepository = remoteRepository)
        )
    }
}