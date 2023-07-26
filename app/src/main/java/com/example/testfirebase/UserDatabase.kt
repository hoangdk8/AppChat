package com.example.testfirebase


import com.google.gson.annotations.SerializedName

data class UserDatabase(
    @SerializedName("user")
    var user: List<User?>? = null
) {
    data class User(
        @SerializedName("email")
        var email: String? = null,
        @SerializedName("status")
        var status: Boolean? = null
    )

}