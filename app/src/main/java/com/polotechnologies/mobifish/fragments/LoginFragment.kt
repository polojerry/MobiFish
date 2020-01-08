package com.polotechnologies.mobifish.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

import com.polotechnologies.mobifish.R
import com.polotechnologies.mobifish.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var mBinding : FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        mAuth = FirebaseAuth.getInstance()

        mBinding.btnLogin.setOnClickListener {
            loginUser()
        }
        mBinding.btnSignUp.setOnClickListener {
            signUpUser()
        }

        return mBinding.root
    }

    private fun loginUser() {

    }

    private fun signUpUser() {
        findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
    }

}
