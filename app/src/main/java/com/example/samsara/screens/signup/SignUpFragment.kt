package com.example.samsara.screens.signup


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.samsara.R
import com.example.samsara.databinding.FragmentSignUpBinding
import com.example.samsara.datasource.local.UserDataSharedPref
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    private val userData=UserDataSharedPref(requireContext())
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var oneTapClient:SignInClient?=null
    private val RC_SIGN_IN = 9001
    private var currentuser=FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_sign_up,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = Firebase.auth
        mAuth = FirebaseAuth.getInstance()
//        val uid = currentuser!!.uid
        oneTapClient = Identity.getSignInClient(requireContext())
        //traditional signup
        binding.signupBtn.setOnClickListener {
            binding.phoneedit.doOnTextChanged { text, start, before, count ->
                userData.setPhoneNumber(text.toString())
            }

            val userEmail = binding.emailET.text.toString().trim()
            val userpass = binding.passwordET.text.toString().trim()
            val userpassconf = binding.passwordconfET.text.toString().trim()
            if (userEmail.isEmpty()) {
                binding.emailET.error = "email is empty"
            }
            if (userpass.isEmpty()) {
                binding.passwordET.error = "password is empty"
            }
            if (userpassconf.isEmpty()) {
                binding.passwordconfET.error = "password is empty"
            }
            if (userpass != userpassconf) {
                Toast.makeText(context, "passwords are not matching !", Toast.LENGTH_LONG).show()
            }
            if (userEmail.isNotEmpty() && userpass.isNotEmpty() && userpassconf == userpass) {
                mAuth.createUserWithEmailAndPassword(userEmail, userpass).addOnSuccessListener {
                    it.let {
                        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToMainFragment())
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        // Configure Google Sign In
       // googleSignInClient = GoogleSignInClient()
        //back button
        binding.backbutton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.loginGoogle.setOnClickListener {
            signInGoogle()

        }
    }
    //signup suspend fun
    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    //onActivityResult
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                //Toast.makeText(context,"firebaseAuthWithGoogle:" + account.id,Toast.LENGTH_LONG).show()
                firebaseAuthWithGoogle(account.idToken!!,mAuth)
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToMainFragment())
            }catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(context,"google signup failed",Toast.LENGTH_LONG).show()
            }
        }

    }

//    private fun updateUI(user: FirebaseUser?) {
//        if (user != null) {
//            Toast.makeText(context,"hello ",Toast.LENGTH_SHORT).show()
//        } else {
//            // User is signed out
//            // ...
//        }
//    }
//    private fun signOut() {
//        // Firebase sign out
//        mAuth.signOut()
//
//        // Google sign out
//        googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
//            updateUI(null)
//        }
//    }

    //when user signup it will return data again
//    override fun onStart() {
//        super.onStart()
//        if (currentuser != null){
//        }
//    }
    private fun showuser(){
        currentuser?.let {
            val name=it.displayName
            userData.setUserName(name?:"Unknown")
            val email=it.email
            val photoUrl=it.photoUrl
            val phoneNumber:String=it.phoneNumber?:"0100000000000"
            userData.setPhoneNumber(phoneNumber)
            userData.setPhoneNumber(name?:"Unknown")
            //val emailVerified=it.isEmailVerified
            var image:Bitmap?=null
            val imageURL=photoUrl.toString()
            val executorService=Executors.newSingleThreadExecutor()
            executorService.execute {
                try {
                    val inm =java.net.URL(imageURL).openStream()
                    image=BitmapFactory.decodeStream(inm)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        }
    }
    fun firebaseAuthWithGoogle(idToken:String,mAuth: FirebaseAuth){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(context,"signInWithCredential:success", Toast.LENGTH_LONG).show()
                    //updateUI(currentuser)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context,"signInWithCredential:failure"+task.exception, Toast.LENGTH_LONG).show()
                    //updateUI(null)
                }
            }
    }


}