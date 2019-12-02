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
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.facebook.*
import java.util.*
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import org.json.JSONException
import org.json.JSONObject
import com.iramtech.parking.demo.kannadain.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task



/**
 * A simple [Fragment] subclass.
 */
class Account : Fragment() {

    private val title by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString("title") ?: "" }
    lateinit var callbackManager: CallbackManager
    var logoutfb:Boolean = true
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var RC_SIGN_IN:Int=100



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        logoutfb = AccessToken.getCurrentAccessToken()==null
        if(!logoutfb){
            callbackManager = CallbackManager.Factory.create()
            getUserProfile(AccessToken.getCurrentAccessToken())
        }

        //gmail Sign in
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)

        return inflater.inflate(R.layout.fragment_account, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       // accountFragmentLabel.text=title
        fblogin_button.setFragment(this)
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

        var account  = GoogleSignIn.getLastSignedInAccount(context)
        if(account!=null){
            var  personPhoto: Uri? = account?.photoUrl
            Glide.with(this).
                load(personPhoto).
                into(user_image)
        }

        fblogin_button.setOnClickListener {
           // callbackManager = CallbackManager.Factory.create()
           // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))

            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))



            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        getUserProfile(loginResult.accessToken)
                        fblogin_button.setLogoutText("")
                    }

                    override fun onCancel() {
                        Log.d("MainActivity", "Facebook onCancel.")

                    }

                    override fun onError(error: FacebookException) {
                        Log.d("MainActivity", "Facebook onError. "+error)

                    }
                })
        }

        Gmaillogin_button.setOnClickListener {
            var signInIntent:Intent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


        logout.setOnClickListener {

            LoginManager.getInstance().logOut()

             mGoogleSignInClient.signOut().
                 addOnCompleteListener {
                     it.addOnCompleteListener {
                         email.setText("")
                         Glide.with(this).
                             load("").
                             into(user_image)
                     }
             }
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

         callbackManager?.onActivityResult(requestCode, resultCode, data)

         if (requestCode == RC_SIGN_IN) {
             var task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
             handleSignInResult(task)
        }

    }


    private fun handleSignInResult(completedTask:Task<GoogleSignInAccount>) {
    try
    {
        var  account: GoogleSignInAccount? = completedTask.getResult()
        var  personPhoto: Uri? = account?.photoUrl
        Glide.with(this).
            load(personPhoto).
            into(user_image)
        // Signed in successfully, show authenticated UI.

    } catch ( e:ApiException) {
        Log.w("Account ", "signInResult:failed code=" + e.getStatusCode())
    }

}





    private fun getUserProfile(currentAccessToken:AccessToken){
        val request = GraphRequest.newMeRequest(currentAccessToken) { `object`, response ->
            try {

                if (response.getError() != null) {

                }

                logoutfb = AccessToken.getCurrentAccessToken()==null
                //here is the data that you want
                var name = `object`.getString("name")
                var id = `object`.getString("id")
                var imageurl = "http://graph.facebook.com/"+id+"/picture?type=large"
                Glide.with(this).
                    load(imageurl).
                    into(user_image)
                email.setText(`object`.getString("email"))
                //LoginManager.getInstance().logOut()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("FB Error ",e.printStackTrace().toString())
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "name,email,id,picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }


}




