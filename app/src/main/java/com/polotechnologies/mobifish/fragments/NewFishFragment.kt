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
import com.polotechnologies.mobifish.databinding.FragmentCustomerBinding
import com.polotechnologies.mobifish.databinding.FragmentNewFishBinding

/**
 * A simple [Fragment] subclass.
 */
class NewFishFragment : Fragment(), View.OnClickListener {

    lateinit var mAuth: FirebaseAuth
    lateinit var mBinding: FragmentNewFishBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_new_fish, container, false)
        mBinding.toolbarNewFish.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back)
        mBinding.toolbarNewFish.setNavigationOnClickListener(this)





        return mBinding.root}

    override fun onClick(v: View?) {
        findNavController().navigate(R.id.action_newFishFragment_to_fisherManFragment)
    }
}
