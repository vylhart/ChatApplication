package com.example.chatapplication.presentation.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.domain.model.Contact
import com.example.chatapplication.domain.use_cases.contact_use_cases.ContactUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val useCases: ContactUseCases,
) : ViewModel() {
    var contactList = MutableStateFlow(listOf<Contact>())
        private set

    fun getContacts() {
        viewModelScope.launch {
            useCases.getContacts().collect {
                contactList.value = it
            }
        }
    }

    fun joinChannel(contact: Contact, callback: (String) -> Unit) {
        val number = auth.currentUser!!.phoneNumber!!
        val channelID =
            if (number < contact.number) number + contact.number else contact.number + number
        callback(channelID)
    }
}