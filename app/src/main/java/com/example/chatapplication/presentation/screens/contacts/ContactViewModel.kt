package com.example.chatapplication.presentation.screens.contacts

import android.annotation.SuppressLint
import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.common.formatNumber
import com.example.chatapplication.domain.model.Contact
import com.example.chatapplication.domain.use_cases.channel_use_cases.ChannelUseCases
import com.example.chatapplication.domain.use_cases.user_use_cases.UserUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactViewModel @Inject constructor(
    private val app: Application,
    private val auth: FirebaseAuth
    ): ViewModel() {
    var contactList = MutableStateFlow(listOf<Contact>())
    private set

    
    @SuppressLint("Range")
    fun getContacts(){

    }


    fun joinChannel(contact: Contact, callback: (String) -> Unit) {
        val number = auth.currentUser!!.phoneNumber!!
        val channelID = if(number < contact.number) number+contact.number else contact.number+number
        callback(channelID)
    }
}