package com.iramtech.parking.demo.kannadain.viewmodel

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iramtech.parking.demo.kannadain.model.VideoData

class VideosListViewmodel:ViewModel(){

    val videos = MutableLiveData<List<VideoData>>()
    val videosLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()


    fun refresh(){


        val data1 = VideoData("01","","IT","Easy way to install android studio",
            "Install Android Studio",5)

        val data2 = VideoData("02","","IT","Hello world program",
            "Demo App Android Studio",5)

        val videolist = arrayListOf<VideoData>(data1,data2)

        videos.value = videolist
        videosLoadError.value=false
        loading.value=false
    }

    fun restcall(){

    }

}