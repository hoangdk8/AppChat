package com.example.testfirebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testfirebase.databinding.ReceiveMessageBinding
import com.example.testfirebase.databinding.SendMessageBinding

class MessageAdapter(
    private val messages: MutableList<MessegesDataBase.Message>,
    private val currentEmail: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 0
        private const val VIEW_TYPE_RECEIVED = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding =
                SendMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentMessageViewHolder(binding)
        } else {
            val binding =
                ReceiveMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender == currentEmail) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    inner class SentMessageViewHolder(private val binding: SendMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessegesDataBase.Message) {
            binding.textViewMessage.text = message.text.toString()
        }
    }

    inner class ReceivedMessageViewHolder(private val binding: ReceiveMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessegesDataBase.Message) {
            binding.textViewMessage.text = message.text.toString()
        }
    }

}