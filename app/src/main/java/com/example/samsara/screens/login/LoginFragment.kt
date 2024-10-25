package com.example.samsara.screens.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.samsara.R
import com.example.samsara.databinding.FragmentLoginBinding
import com.example.samsara.screens.signup.SignUpFragmentDirections
import com.example.samsara.utils.firebaseAuthWithGoogle
import com.example.samsara.utils.googleSignInClient

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

class LoginFragment : Fragment() {
    private lateinit var mAuth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var oneTapClient: SignInClient?=null
    private val RC_SIGN_IN = 9001
    private var currentuser=FirebaseAuth.getInstance().currentUser
    private lateinit var binding:FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = Firebase.auth
        mAuth = FirebaseAuth.getInstance()
        oneTapClient = Identity.getSignInClient(requireContext())
      //  val uid=currentuser!!.uid
        binding.loginBtn.setOnClickListener {
            val userEmail=binding.emailET.text.toString().trim()
            val userpass=binding.passwordbut.text.toString().trim()
            if (userEmail.isEmpty()){
                binding.emailET.error="email is empty"
            }
            if (userpass.isEmpty()){
                binding.passwordbut.error="password is empty"
            }
            if (userEmail.isNotEmpty()&&userpass.isNotEmpty()) {
            mAuth.signInWithEmailAndPassword(userEmail,userpass).addOnSuccessListener {
                it.let {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                }
            }.addOnFailureListener {
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
            }

            }
        }
        // Configure Google Sign In
        googleSignInClient =googleSignInClient()

        binding.backbutton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.googelogin.setOnClickListener {
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
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
            }catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(context,"google signup failed${e.message}",Toast.LENGTH_LONG).show()
            }
        }

    }


}