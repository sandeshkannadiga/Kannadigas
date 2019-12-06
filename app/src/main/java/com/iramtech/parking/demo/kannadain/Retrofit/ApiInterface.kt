package com.iramtech.parking.demo.kannadain.Retrofit

import com.iramtech.parking.demo.kannadain.model.uploadVideoPOJO.uploadVideo
import com.iramtech.parking.demo.kannadain.view.Upload
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {

    @POST("kannada.in/test.php")
    @Multipart
    fun UploadVideo(@Part filePart: MultipartBody.Part):Single<uploadVideo>
    //fun UploadVideo(@Part filePart: MultipartBody.Part): Observable<uploadVideo>


    //fun UploadVideo(video: Upload.Video):Single<uploadVideo>



    /*@POST("feedback/upload/")
    @Multipart
    Call<UploadFeedback> sendfeedbackImage(@Header("Authorization") String auth, @Part MultipartBody.Part filePart, @Part("feedbackType") RequestBody feedbackType, @Part("timestamp") RequestBody timestamp
    , @Part("mobile") RequestBody mobile, @Part("description") RequestBody description, @Part("category") RequestBody category
    , @Part("rating") RequestBody rating, @Part("parkingSiteId") RequestBody parkingSiteId);

*/
}