package com.iramtech.parking.demo.kannadain.view


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.TargetApi
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.iramtech.parking.demo.kannadain.R
import com.iramtech.parking.demo.kannadain.Retrofit.ApiClient
import com.iramtech.parking.demo.kannadain.Retrofit.ApiInterface
import com.iramtech.parking.demo.kannadain.model.VideoData
import com.iramtech.parking.demo.kannadain.model.uploadVideoPOJO.uploadVideo
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class Upload : Fragment() {

    private val title by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString("title") ?: "" }
    var REQUEST_TAKE_GALLERY_VIDEO:Int = 100
    var RECORD_REQUEST_CODE:Int = 5
    private var mediaPath: String? = null
    private var postPath: String? = null
     lateinit var permissionsToRequest:ArrayList<String>
     lateinit var permissionsRejected:ArrayList<String>
     lateinit var permissions:ArrayList<String>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload, container, false)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       // jobFragmentLabel.text = title
        val permission = context?.applicationContext?.let {
            checkSelfPermission(
                it,
                READ_EXTERNAL_STORAGE)
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "Permission to record denied")
            makeRequest()
        }

       /* ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            RECORD_REQUEST_CODE)*/

        picker.setOnClickListener {
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Video"),
                REQUEST_TAKE_GALLERY_VIDEO
            )
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE)
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("", "Permission has been denied by user")
                } else {
                    Log.i("", "Permission has been granted by user")
                }
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO) {

            if (data != null)
            {
                val contentURI = data.data

                var id:String = DocumentsContract.getDocumentId(contentURI)
                if (Build.VERSION.SDK_INT >= 19) {
//
                  var url:String? = handleVideoOnKitkat(data)
                    Log.i("result","URI "+url)
                    var file = File(url)
                    sendvideo(file)
                }
                else{
//
                    handleVideoBeforeKitkat(data)
                }

                try
                {
                    //var mylist:List<Video> ?= null //getRealPathfromURI(contentURI)
                    /*Glide.with(this)
                        .load(mylist.get(0).uri)
                        .into(picker)*/

                    //var file = File(mylist.get(0).uri.path)

                }

                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                }




            }

        }
    }

    private fun sendvideo(file: File) {

        var apiInterface:ApiInterface =
            ApiClient.getClient()!!.create(ApiInterface::class.java)

        var p1:MultipartBody.Part =   MultipartBody.Part.createFormData("avatar",file.getName(),
            RequestBody.create(MediaType.parse("video/*"), file))

        val call = apiInterface.UploadVideo(p1)
       // var call: Single<uploadVideo> = apiInterface.UploadVideo(p1)
        val disposable= CompositeDisposable()


        disposable.add(
            call.toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeBy(onComplete = {
                   },
                    onNext = {
                        //it
                        Toast.makeText(context,"Completed ",Toast.LENGTH_LONG).show()
                    },

                    onError = {

                    })
        )






        /*disposable.add(
            call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<uploadVideo>(){
                    override fun onSuccess(t: uploadVideo) {
                       val upload = t
                        Log.i("Rest result"," result success")
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(context,""+e.printStackTrace(),Toast.LENGTH_LONG).show()
                    }


                })

        )*/
    }

    private fun handleVideoBeforeKitkat(data: Intent?) {

    }

    @TargetApi(19)
    private fun handleVideoOnKitkat(data: Intent?) :String?{
        var imagePath: String? = null
        val uri = data!!.data
        if (DocumentsContract.isDocumentUri(context, uri)){
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri.authority){
                val id = docId.split(":")[1]
                val selsetion = MediaStore.Video.Media._ID + "=" + id
                imagePath = imagePath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, selsetion)
            }
            else if ("com.android.providers.downloads.documents" == uri.authority){
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                imagePath = imagePath(contentUri, null)
            }
        }
        else if ("content".equals(uri.scheme, ignoreCase = true)){
//            content类型Uri 普通方式处理
            imagePath = imagePath(uri, null)
        }
        else if ("file".equals(uri.scheme, ignoreCase = true)){
            imagePath = uri.path
        }
        //displayVideo(imagePath)
        return  imagePath
    }

    private fun imagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = context?.contentResolver?.query(uri, null, selection, null, null )
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }


    data class Video(
        val uri: Uri,
        val name: String,
        val duration: Int,
        val size: Int
    )


    /*private fun getRealPathfromURI(selectedVideo: Uri): MutableList<Video> {

        Log.i("authority","authority res "+selectedVideo.authority)

        val videoList = mutableListOf<Video>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATA
        )
        val selection = "${MediaStore.Video.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(
            TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
        )
        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
        //val uri = FileProvider.getUriForFile(context, "com.package.name.fileprovider", file)

        context?.contentResolver?.query(
            selectedVideo,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)


            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)


                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                videoList += Video(contentUri, name, duration, size)
            }
            cursor.close()
        }

        return videoList

    }*/

}
