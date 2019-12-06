package com.iramtech.parking.demo.kannadain.Retrofit

import android.content.Context
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {

        private val BASE_URL="http://www.marwadijodi.co.in/"

        private var retrofit:Retrofit? =null

        fun  getClient():Retrofit?{

            if(retrofit==null){
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                return retrofit
            }else{
                return retrofit
            }


        }
    }
}