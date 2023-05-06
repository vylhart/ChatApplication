package com.example.chatapplication.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.common.getDummyImageUrl
import com.example.chatapplication.common.getRelativeTime
import com.example.chatapplication.databinding.ChatTileViewBinding
import com.example.chatapplication.domain.model.Channel


class ChannelAdapter(val onClick: (Channel) -> Unit) : RecyclerView.Adapter<ChannelAdapter.ChatViewHolder>() {
    private var list = emptyList<Channel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ChatTileViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: List<Channel>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    inner class ChatViewHolder(private val binding: ChatTileViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Channel) {
            with(binding) {
                imageView.setImage(data.users[0].photoURL?: getDummyImageUrl())
                messageTextView.text = data.lastMessage.data
                timeTextView.text = getRelativeTime(data.lastMessage.date)
                nameTextView.text = data.users[0].name
                root.setOnClickListener { onClick(data) }
            }
        }
    }
}