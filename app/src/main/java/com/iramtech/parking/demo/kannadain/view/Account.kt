package com.iramtech.parking.demo.kannadain.view


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.facebook.CallbackManager
import java.util.*
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import org.json.JSONException
import com.facebook.GraphResponse
import org.json.JSONObject
import com.facebook.GraphRequest
import com.facebook.AccessToken
import com.iramtech.parking.demo.kannadain.R


/**
 * A simple [Fragment] subclass.
 */
class Account : Fragment() {

    private val title by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString("title") ?: "" }
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       // accountFragmentLabel.text=title
        var shareapp = share
        shareapp.setOnClickListener {

            // share application using other Apps
            val appPackageName = context?.getPackageName()
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out the App at: https://play.google.com/store/apps/details?id=$appPackageName"
            )
            sendIntent.type = "text/plain"
            context?.startActivity(sendIntent)
        }

        //login_button.setPermissions(Arrays.asList("email", "public_profile"))

        login_button.setOnClickListener {

            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))

            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                       val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                            try {
                                //here is the data that you want
                                var name = `object`.getString("name")
                                var id = `object`.getString("id")
                                var imageurl = "http://graph.facebook.com/"+id+"/picture?type=large"

                                Glide.with(view).
                                    load(imageurl).
                                    into(user_image)

                                email.setText(`object`.getString("email"))

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        val parameters = Bundle()
                        parameters.putString("fields", "name,email,id,picture.type(large)")
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        Log.d("MainActivity", "Facebook onCancel.")

                    }

                    override fun onError(error: FacebookException) {
                        Log.d("MainActivity", "Facebook onError. "+error)

                    }
                })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }





}


