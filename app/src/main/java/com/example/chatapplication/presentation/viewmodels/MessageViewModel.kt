package com.example.chatapplication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.chatapplication.domain.use_cases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MessageViewModel(private val useCases: UseCases): ViewModel() {

}