package com.iramtech.parking.demo.kannadain.view


import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.iramtech.parking.demo.kannadain.R
import kotlinx.android.synthetic.main.fragment_upload.*




/**
 * A simple [Fragment] subclass.
 */
class Upload : Fragment() {

    private val title by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString("title") ?: "" }
    var REQUEST_TAKE_GALLERY_VIDEO:Int = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       // jobFragmentLabel.text = title
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, Videodata: Intent?) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
           var videores:Uri? = Videodata?.data

            //var pathToStoredVideo:String? = getRealPathFromURIPath(videores, context)
           // Toast.makeText(context,""+pathToStoredVideo,Toast.LENGTH_LONG).show()
        }
    }

    /*private fun getRealPathFromURIPath(videores: Uri?, upload: Context?): String? {
        //var cursor:Cursor? = upload?.contentResolver?.query(videores,null,null,null,null)
        var cursor:Cursor? = upload?.contentResolver?.query(videores,null,null,null,null)
        *//*Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);*//*
        if (cursor == null) {
            return videores?.path
        } else {
            cursor.moveToFirst()
            var idx:Int = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
            //var idx:Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(idx)
        }
    }*/

}
