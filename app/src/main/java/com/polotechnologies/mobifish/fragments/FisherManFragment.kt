package com.polotechnologies.mobifish.fragments


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

import com.polotechnologies.mobifish.R
import com.polotechnologies.mobifish.databinding.FragmentFisherManBinding

/**
 * A simple [Fragment] subclass.
 */
class FisherManFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    lateinit var mBinding: FragmentFisherManBinding
    lateinit var mAuth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_fisher_man, container, false)
        mBinding.toolbarFisherMan.inflateMenu(R.menu.menu_fisher_man)
        mBinding.toolbarFisherMan.setOnMenuItemClickListener(this)
        mAuth = FirebaseAuth.getInstance()



        return mBinding.root
    }


    private fun signOutUser() {
        mAuth.signOut()
        findNavController().navigate(R.id.action_fisherManFragment_to_loginFragment)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.actionSignOut->signOutUser()
        }
        return true
    }
}
