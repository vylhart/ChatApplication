package com.example.chatapplication.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.chatapplication.R
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Constants.SIGN_IN_REQUEST
import com.example.chatapplication.common.Constants.SIGN_UP_REQUEST
import com.example.chatapplication.data.local.MessageDatabase
import com.example.chatapplication.data.local.repository.MessageLocalRepositoryImpl
import com.example.chatapplication.data.remote.repository.AuthRepositoryImpl
import com.example.chatapplication.data.remote.repository.MessageRemoteRepositoryImpl
import com.example.chatapplication.data.worker.WorkerUtils
import com.example.chatapplication.domain.repository.AuthRepository
import com.example.chatapplication.domain.repository.MessageRepository
import com.example.chatapplication.domain.use_cases.auth_use_cases.*
import com.example.chatapplication.domain.use_cases.chat_use_cases.*
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(
        app: Application
    ): Context = app.applicationContext

    @Singleton
    @Provides
    fun providesCollection() = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_MESSAGE)

    @Singleton
    @Provides
    fun providesUseCases(repository: MessageRepository, remoteRepository: MessageRemoteRepositoryImpl, workerUtils: WorkerUtils): ChatUseCases {
        return ChatUseCases(
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

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }


    @Singleton
    @Provides
    fun provideAuthUseCases(repository: AuthRepository) = AuthUseCases(
        isUserAuthenticated = IsUserAuthenticated(repository),
        firebaseSignIn = FirebaseSignIn(repository),
        signOut = SignOut(repository),
        oneTapSignIn = OneTapSignIn(repository)
    )

    @Provides
    @Named(SIGN_IN_REQUEST)
    fun provideSignInRequest(app: Application) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build())
        .setAutoSelectEnabled(true)
        .build()

    @Provides
    @Named(SIGN_UP_REQUEST)
    fun provideSignUpRequest(app: Application) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build())
        .build()

    @Provides
    fun provideGoogleSignInOptions(
        app: Application
    ) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    @Provides
    fun provideGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)

    @Provides
    fun provideAuthRepository(auth: FirebaseAuth, oneTapClient: SignInClient,
        @Named(SIGN_IN_REQUEST)
        signInRequest: BeginSignInRequest,
        @Named(SIGN_UP_REQUEST)
        signUpRequest: BeginSignInRequest
    ): AuthRepository = AuthRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInRequest = signInRequest,
        signUpRequest = signUpRequest,
    )

    @Provides
    fun provideOneTapClient(
        @ApplicationContext context: Context
    ) = Identity.getSignInClient(context)

}