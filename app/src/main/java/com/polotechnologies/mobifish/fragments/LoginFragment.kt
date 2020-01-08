package com.polotechnologies.mobifish.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.polotechnologies.mobifish.R
import com.polotechnologies.mobifish.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    lateinit var mBinding : FragmentLoginBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)

        mBinding.btnLogin.setOnClickListener {
            loginUser()
        }
        mBinding.btnSignUp.setOnClickListener {
            signUpUser()
        }

        return mBinding.root
    }

    private fun loginUser() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    private fun signUpUser() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}
