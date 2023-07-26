package com.example.testfirebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.testfirebase.databinding.ActivityMainBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isMessageSending = false
    private val messageList = mutableListOf<MessegesDataBase.Message>()
    private lateinit var messageAdapter: MessageAdapter
    private val isSentMessages = mutableListOf<Boolean>()


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        val emailSender = intent.getStringExtra("EMAIL_GUI") ?: ""
        val emailReceiver = intent.getStringExtra("EMAIL_NHAN") ?: ""

        listenForNewMessages(emailSender, emailReceiver)
        messageAdapter = MessageAdapter(messageList, emailSender)
        binding.recyclerViewsChat.adapter = messageAdapter
        binding.editText1.addTextChangedListener { text ->
            if (text?.isNotBlank() == true) {
                binding.buttonBam.visibility = View.VISIBLE
                binding.imageButton.visibility = View.GONE
            } else {
                binding.buttonBam.visibility = View.GONE
                binding.imageButton.visibility = View.VISIBLE
            }
        }
        binding.buttonBam.setOnClickListener {
            sendMessages()
            val message = MessegesDataBase.Message(
                emailSender,
                emailReceiver,
                binding.editText1.text.toString(),
                "",
                true
            )
            messageList.add(message)
            messageAdapter.notifyItemInserted(messageList.size - 1)
            binding.editText1.text.clear()
        }
    }

    private fun setupViews() {
        val email = intent.getStringExtra("EMAIL_NHAN")
        binding.textViewName.text = email
        binding.imageButtonBack.setOnClickListener {
            finish()
        }
        binding.imageButton.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivity(galleryIntent)
        }
    }

    private fun listenForNewMessages(emailSender: String, emailReceiver: String) {
        val database = Firebase.database
        val myRef = database.getReference("messages")

        val querySender = myRef.orderByChild("sender").equalTo(emailSender)
        querySender.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (!isMessageSending) {
                    val message = snapshot.getValue(MessegesDataBase.Message::class.java)
                    if (message != null) {
                        val isMessageFromSender =
                            message.sender == emailSender && message.receiver == emailReceiver
                        val isMessageFromReceiver =
                            message.sender == emailReceiver && message.receiver == emailSender

                        if (isMessageFromSender || isMessageFromReceiver) {
                            messageList.add(message)
                            isSentMessages.add(isMessageFromSender)
                            messageAdapter.notifyDataSetChanged()
                            binding.recyclerViewsChat.scrollToPosition(messageList.size - 1)
                        }
                    }
                } else {
                    isMessageSending = false
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Xử lý sự kiện khi có sự thay đổi tin nhắn (nếu cần)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Xử lý sự kiện khi tin nhắn bị xóa (nếu cần)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Xử lý sự kiện khi tin nhắn được di chuyển (nếu cần)
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })

        val queryReceiver = myRef.orderByChild("sender").equalTo(emailReceiver)
        queryReceiver.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (!isMessageSending) {
                    val message = snapshot.getValue(MessegesDataBase.Message::class.java)
                    if (message != null) {
                        val isMessageFromSender =
                            message.sender == emailSender && message.receiver == emailReceiver
                        val isMessageFromReceiver =
                            message.sender == emailReceiver && message.receiver == emailSender

                        if (isMessageFromSender || isMessageFromReceiver) {
                            messageList.add(message)
                            isSentMessages.add(isMessageFromSender)
                            messageAdapter.notifyDataSetChanged()
                            binding.recyclerViewsChat.scrollToPosition(messageList.size - 1)
                        }
                    }
                } else {
                    isMessageSending = false
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Xử lý sự kiện khi có sự thay đổi tin nhắn (nếu cần)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Xử lý sự kiện khi tin nhắn bị xóa (nếu cần)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Xử lý sự kiện khi tin nhắn được di chuyển (nếu cần)
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })
    }



    @SuppressLint("NotifyDataSetChanged")
    private fun sendMessages() {
        val database = Firebase.database
        val myRef = database.getReference("messages")
        val emailSender = intent.getStringExtra("EMAIL_GUI")
        val emailReceiver = intent.getStringExtra("EMAIL_NHAN")
        val userSender = MessegesDataBase.Message(
            emailSender,
            emailReceiver,
            binding.editText1.text.toString(),
            "abc",
            true
        )
        val key = myRef.push().key
        isMessageSending = true
        if (key != null) {
            myRef.child(key).setValue(userSender).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isSentMessages.add(true)
                    messageAdapter.notifyDataSetChanged()
                    binding.recyclerViewsChat.scrollToPosition(messageList.size - 1)

                } else {
                }
            }
        }
    }
}