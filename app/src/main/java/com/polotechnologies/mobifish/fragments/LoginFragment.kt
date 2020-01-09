package com.polotechnologies.mobifish.fragments


import android.app.Activity
import android.content.Context
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
import com.google.firebase.database.*
import com.polotechnologies.mobifish.R
import com.polotechnologies.mobifish.dataModels.User
import com.polotechnologies.mobifish.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var mBinding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference

    private val RC_PHONE_SIGNUP = 100
    private val RC_PHONE_SIGNIN = 101


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference

        if(mAuth.currentUser!=null){
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            val userCategory = sharedPref!!.getString("user_category","none")

            if(userCategory.equals(getString(R.string.user_category_fisher_man))){
                findNavController().navigate(R.id.action_loginFragment_to_fisherManFragment)
            }else if(userCategory.equals(getString(R.string.user_category_customer))){
                findNavController().navigate(R.id.action_loginFragment_to_customerFragment)
            }
        }
        mBinding.btnLogin.setOnClickListener {
            loginUser()
        }
        mBinding.btnSignUp.setOnClickListener {
            signUpFlow(it)
        }
        return mBinding.root
    }

    private fun loginUser() {
        phoneAuth(RC_PHONE_SIGNIN)
    }

    private fun signUpFlow(view: View) {
        phoneAuth(RC_PHONE_SIGNUP)
    }

    private fun phoneAuth(requestCode: Int) {
        // Choose authentication providers
        val providers = listOf(
                PhoneBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_PHONE_SIGNUP -> signUpUser(resultCode, data)
            RC_PHONE_SIGNIN -> signInUser(resultCode, data)
        }

    }

    private fun signInUser(resultCode: Int, data: Intent?) {
        val response = IdpResponse.fromResultIntent(data)

        if (resultCode == Activity.RESULT_OK) {
            val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return
            login(firebaseUser)

        } else {
            // Sign in failed. If response is null the user canceled the
            if ((response?.error?.errorCode ?: 0) == ErrorCodes.NO_NETWORK) {
                printMessage("No Network")
            } else {
                printMessage("Cancelled by User")
            }

        }
    }

    private fun login(firebaseUser: FirebaseUser) {
        val userReference = mDatabaseReference.child(getString(R.string.db_node_users)).child(firebaseUser.uid)
        val userValueListener = object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userDetail : User = dataSnapshot.getValue(User::class.java)!!

                saveDetailToSharedPreference(userDetail)
                if (userDetail.user_category == getString(R.string.user_category_fisher_man)){
                    findNavController().navigate(R.id.action_loginFragment_to_fisherManFragment)
                }else{
                    findNavController().navigate(R.id.action_loginFragment_to_customerFragment)
                }
            }

        }
        userReference.addListenerForSingleValueEvent(userValueListener)
    }

    private fun saveDetailToSharedPreference(userDetail: User) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("user_category", userDetail.user_category)
            commit()
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
