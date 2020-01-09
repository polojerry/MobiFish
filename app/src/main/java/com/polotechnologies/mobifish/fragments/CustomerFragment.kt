package com.polotechnologies.mobifish.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

import com.polotechnologies.mobifish.R
import com.polotechnologies.mobifish.databinding.FragmentCustomerBinding
import com.polotechnologies.mobifish.databinding.FragmentFisherManBinding

/**
 * A simple [Fragment] subclass.
 */
class CustomerFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    lateinit var mBinding: FragmentCustomerBinding
    lateinit var mAuth:FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_customer, container, false)
        mBinding.toolbarCustomer.inflateMenu(R.menu.menu_customer)
        mBinding.toolbarCustomer.setOnMenuItemClickListener(this)

        mAuth = FirebaseAuth.getInstance()




        return mBinding.root
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.actionSignOut->signOutUser()
        }
        return true
    }


    private fun signOutUser() {
        mAuth.signOut()
        findNavController().navigate(R.id.action_customerFragment_to_loginFragment)
    }

}
