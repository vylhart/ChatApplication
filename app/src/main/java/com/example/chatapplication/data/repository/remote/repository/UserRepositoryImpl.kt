package com.example.chatapplication.data.repository.remote.repository

import android.annotation.SuppressLint
import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import com.example.chatapplication.common.Constants.COLLECTION_USER
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Constants.UNKNOWN_ERROR
import com.example.chatapplication.common.Resource
import com.example.chatapplication.common.formatNumber
import com.example.chatapplication.domain.model.Contact
import com.example.chatapplication.data.model.User
import com.example.chatapplication.data.model.toContact
import com.example.chatapplication.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val app: Application,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth): UserRepository {
    private var user: User? = null

    override suspend fun getCurrentUser(): Flow<Resource<User?>> = flow{
        try{
            if(user!=null){
                emit(Resource.Success(user))
            }else{
                auth.currentUser?.uid?.let { getUser(it) }
                emit(Resource.Success(user))
            }
        }
        catch (e: Exception){
            Log.e(TAG, "getCurrentUser: ", e)
            emit(Resource.Error(e.localizedMessage?: UNKNOWN_ERROR))
        }
    }

    override suspend fun addNewUser(name: String): Flow<Resource<Boolean>> = flow {
        try{
            //emit(Resource.Loading)
            auth.currentUser?.apply {
                user = User(
                    uid = uid,
                    createdAt = System.currentTimeMillis(),
                    name = name,
                    phoneNumber = phoneNumber!!
                )
                firestore.collection(COLLECTION_USER).document(uid).set(user!!).await()
            }
            emit(Resource.Success(true))
        }
        catch (e: Exception){
            Log.e(TAG, "getCurrentUser: ", e)
            emit(Resource.Error(e.localizedMessage?: UNKNOWN_ERROR))
        }
    }

    override suspend fun getUser(uid: String): User? {
        user = firestore.collection(COLLECTION_USER).document(uid).get().await().toObject(User::class.java)
        return user
    }

    @SuppressLint("Range")
    override suspend fun getContacts(): Flow<Resource<List<Contact>>> = flow {
        try{
            val cursor = app.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC")
            val list = mutableSetOf<Contact>()
            while (cursor?.moveToNext() == true){
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = formatNumber( cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))) ?: continue
                list.add(Contact(name=name, number=number))
            }
            cursor?.close()
            emit(Resource.Success(getContactsList(list.toList())))
        } catch (e: Exception){
            Log.e(TAG, "getContacts: ", e)
        }
    }

    private suspend fun getContactsList(list: List<Contact>) = coroutineScope {
        val deferred = list.map { contact ->
            async {
                val users: MutableList<User> =  firestore
                    .collection(COLLECTION_USER)
                    .whereEqualTo("phoneNumber", contact.number).limit(1)
                    .get().await().toObjects(User::class.java)
                Log.d(TAG, "getContact: ${users[0].uid}")
                return@async users[0].toContact()
            }
        }
        deferred.awaitAll()
    }

}
