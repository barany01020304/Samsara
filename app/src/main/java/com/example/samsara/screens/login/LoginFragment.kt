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
        googleSignInClient = googleSignInClient()
        binding.loginBtn.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
           // signInGoogle()
        }
        binding.backbutton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    //signup suspend fun
    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    fun Fragment.googleSignInClient( ):GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(requireActivity(), gso)
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
    private fun firebaseAuthWithGoogle(idToken:String, mAuth: FirebaseAuth){
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