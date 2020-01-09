package com.polotechnologies.mobifish.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.polotechnologies.mobifish.R
import com.polotechnologies.mobifish.databinding.FragmentFisherManBinding

/**
 * A simple [Fragment] subclass.
 */
class FisherManFragment : Fragment() {

    lateinit var mBinding: FragmentFisherManBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_fisher_man, container, false)





        return mBinding.root
    }


}
