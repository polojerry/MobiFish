package com.polotechnologies.mobifish.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.PhoneBuilder
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.polotechnologies.mobifish.R
import com.polotechnologies.mobifish.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var mBinding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth

    private val RC_PHONE_SIGNUP = 101
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        mAuth = FirebaseAuth.getInstance()

        mBinding.btnLogin.setOnClickListener {
            loginUser()
        }
        mBinding.btnSignUp.setOnClickListener {
            signUpFlow(it)
        }
        return mBinding.root
    }

    private fun loginUser() {

    }

    private fun signUpFlow(view: View) {
        phoneAuth()
    }

    private fun phoneAuth() {
        // Choose authentication providers
        val providers = listOf(
                PhoneBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_PHONE_SIGNUP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_PHONE_SIGNUP -> signUpUser(resultCode, data)
        }

    }

    private fun signUpUser(resultCode: Int, data: Intent?) {
        val response = IdpResponse.fromResultIntent(data)

        if (resultCode == Activity.RESULT_OK) {
            val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment(firebaseUser.phoneNumber.toString())
            findNavController().navigate(action)

        } else {
            // Sign in failed. If response is null the user canceled the
            if ((response?.error?.errorCode ?: 0) == ErrorCodes.NO_NETWORK) {
                printMessage("No Network")
            } else {
                printMessage("Cancelled by User")
            }

        }

    }

    private fun printMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }
}
