package com.iramtech.parking.demo.kannadain.model.uploadVideoPOJO

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class uploadVideo(
    @SerializedName("status")
    @Expose
    private var status:String?,
    @SerializedName("error")
    @Expose
    private var error:Boolean?,
    @SerializedName("message")
    @Expose
    private var message:String?,
    @SerializedName("url")
    @Expose
    private var url:String?
)