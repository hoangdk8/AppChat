package com.example.testfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testfirebase.databinding.ActivityHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        // Lấy danh sách người dùng từ Firebase Realtime Database
        val database = Firebase.database
        val myRef = database.getReference("user")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList: ArrayList<UserDatabase.User> = ArrayList()
                for (postSnapshot in snapshot.children) {
                    val user = postSnapshot.getValue(UserDatabase.User::class.java)
                    if (user != null) {
                        userList.add(user)
                        Log.d("Hoangtv", "onData: $userList")
                    }
                    userList.sortByDescending { it.status }
                }
                val userAdapter =
                    UserOnlAdapter(userList, object : UserOnlAdapter.OnItemClickListener {
                        override fun onItemClick(email: String?) {
                            val email_gui = binding.textViewUser.text
                            val intent = Intent(this@HomeActivity, ChatActivity::class.java)
                            intent.putExtra("EMAIL_NHAN", email)
                            intent.putExtra("EMAIL_GUI", email_gui)
                            startActivity(intent)

                        }
                    })
                binding.recyclerViewUserOnl.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần thiết
            }
        })
    }

    private fun setupViews() {
        binding.buttonDangXuat.setOnClickListener {
            val database = Firebase.database
            val myRef = database.getReference("user")
            val email = intent.getStringExtra("EMAIL")
            myRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (postSnapshot in snapshot.children) {
                            val user: UserDatabase.User? =
                                postSnapshot.getValue(UserDatabase.User::class.java)
                            if (user != null) {
                                // Cập nhật trạng thái của người dùng thành true
                                postSnapshot.ref.child("status").setValue(false)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Xử lý lỗi nếu cần thiết
                    }
                })
            finish()
            startActivity(Intent(this, UserActivity::class.java))
        }
        val email = intent.getStringExtra("EMAIL")
        binding.textViewUser.text = email

        Log.d("Hoangtv", "setupViews: $email")
    }

//    override fun onPause() {
//        super.onPause()
//        val database = Firebase.database
//        val myRef = database.getReference("user")
//        val email = intent.getStringExtra("EMAIL")
//        myRef.orderByChild("email").equalTo(email)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (postSnapshot in snapshot.children) {
//                        val user: UserDatabase.User? =
//                            postSnapshot.getValue(UserDatabase.User::class.java)
//                        if (user != null) {
//                            // Cập nhật trạng thái của người dùng thành true
//                            postSnapshot.ref.child("status").setValue(false)
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Xử lý lỗi nếu cần thiết
//                }
//            })
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val database = Firebase.database
//        val myRef = database.getReference("user")
//        val email = intent.getStringExtra("EMAIL")
//        myRef.orderByChild("email").equalTo(email)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (postSnapshot in snapshot.children) {
//                        val user: UserDatabase.User? =
//                            postSnapshot.getValue(UserDatabase.User::class.java)
//                        if (user != null) {
//                            // Cập nhật trạng thái của người dùng thành true
//                            postSnapshot.ref.child("status").setValue(true)
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Xử lý lỗi nếu cần thiết
//                }
//            })
//    }
}