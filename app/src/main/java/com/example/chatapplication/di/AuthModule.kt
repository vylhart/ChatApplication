package com.example.chatapplication.di

import com.example.chatapplication.data.repository.remote.repository.PhoneAuthRepositoryImpl
import com.example.chatapplication.domain.repository.AuthRepository
import com.example.chatapplication.domain.use_cases.auth_use_cases.*
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {
    @Provides
    @ViewModelScoped
    fun providePhoneAuthRepository(auth: FirebaseAuth
    ): AuthRepository = PhoneAuthRepositoryImpl(
        auth = auth
    )

    @Provides
    @ViewModelScoped
    fun provideAuthUseCases(repository: AuthRepository) = AuthUseCases(
        isUserAuthenticated = IsUserAuthenticated(repository),
        firebaseSignIn = FirebaseSignIn(repository),
        signOut = SignOut(repository),
        beginSignIn = BeginSignIn(repository)
    )
}
