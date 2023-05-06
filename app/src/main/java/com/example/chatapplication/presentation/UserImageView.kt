package com.example.chatapplication.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.chatapplication.databinding.UserImageViewBinding

class UserImageView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attr, defStyleAttr, defStyleRes) {
    private val binding = UserImageViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setImage(url: String) {
        Glide.with(context)
            .load(url)
            .circleCrop().into(binding.imageView)
    }
}