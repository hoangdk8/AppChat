package com.example.testfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.testfirebase.databinding.ActivityUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() {
        binding.buttonDangNhap.setOnClickListener {
            val email = binding.editTextUser.text.toString()
            checkEmailExists(email)
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("EMAIL", email)
            }


        }
        binding.buttonDanKy.setOnClickListener {
            val email = binding.editTextUser.text.toString()
            addUserToDatabase(email)
        }
    }

    private fun addUserToDatabase(email: String) {
        val database = Firebase.database
        val myRef = database.getReference("user")

        val user = UserDatabase.User(email, false)
        val key = myRef.push().key

        if (key != null) {
            myRef.child(key).setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this@UserActivity, "Đăng ký thành công.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        // Đăng ký thất bại, hiển thị thông báo
                        Toast.makeText(
                            this@UserActivity,
                            "Đăng ký thất bại. Vui lòng thử lại.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun checkEmailExists(email: String) {
        val database = Firebase.database
        val myRef = database.getReference("user")

        myRef.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Email đã tồn tại trong Realtime Database, chuyển sang HomeActivity
                        updateStatus(email)
                        startActivity(Intent(this@UserActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        // Email chưa tồn tại trong Realtime Database, hiển thị thông báo
                        Toast.makeText(
                            this@UserActivity,
                            "Chưa có tài khoản với email này.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi nếu cần thiết
                }
            })
    }

    private fun updateStatus(email: String) {
        val database = Firebase.database
        val myRef = database.getReference("user")
        myRef.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        val user: UserDatabase.User? =
                            postSnapshot.getValue(UserDatabase.User::class.java)
                        if (user != null) {
                            // Cập nhật trạng thái của người dùng thành true
                            postSnapshot.ref.child("status").setValue(true)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi nếu cần thiết
                }
            })
    }
//    private fun setDataBase(){
//        val database = Firebase.database
//        val myRef = database.getReference("user")
////        var user1 = UserDatabase.User("HoangTV1@gmail.com", false);
////        var user2 = UserDatabase.User("HoangTV2@gmail.com", false);
////        val listUser = ArrayList<UserDatabase.User>()
////        listUser.add(user1)
////        listUser.add(user2)
//        //myRef.setValue(listUser);
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val listUser : ArrayList<UserDatabase.User> = ArrayList()
//                for (postSnapshot in snapshot.children) {
//                    val user: UserDatabase.User? = postSnapshot.getValue(UserDatabase.User::class.java)
//                    if(user != null){
//                        listUser.add(user)
//                    }
//                    Log.d("hoangtv", "onDataChange: ${postSnapshot.toString()}")
//                    // here you can access to name property like university.name
//                }
//                Log.d("hoangtv", "onDataChange: ${listUser[0].email}")
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//    }
}