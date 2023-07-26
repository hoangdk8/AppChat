package com.example.testfirebase


import com.google.gson.annotations.SerializedName

data class MessegesDataBase(
    @SerializedName("messages")
    var messages: List<Message?>? = null
) {

    data class Message(
        @SerializedName("sender")
        var sender: String? = null,
        @SerializedName("receiver")
        var receiver: String? = null,
        @SerializedName("text")
        var text: String? = null,
        @SerializedName("image")
        var image: String? = null,
        @SerializedName("type")
        var type: Boolean? = null
    )
}

