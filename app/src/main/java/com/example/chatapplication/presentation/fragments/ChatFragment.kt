package com.example.chatapplication.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.databinding.FragmentChatBinding
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.presentation.PagerViewModel
import com.example.chatapplication.presentation.adapters.ChannelAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: PagerViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        setupAdapter()
        return binding.root
    }

    private fun setupAdapter() {
        val adapter = ChannelAdapter { onClick(it) }
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        viewModel.state.observe(requireActivity()) {
            adapter.updateData(it.channels)
        }
    }

    private fun onClick(data: Channel) {
    }
}
