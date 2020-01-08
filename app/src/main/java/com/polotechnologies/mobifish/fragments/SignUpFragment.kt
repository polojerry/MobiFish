package com.polotechnologies.mobifish.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil

import com.polotechnologies.mobifish.R
import com.polotechnologies.mobifish.databinding.FragmentSignUpBinding

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

    private lateinit var mBinding: FragmentSignUpBinding
    private lateinit var mContext:Context
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up, container, false)
        mContext = this.context!!

        setFilledDropDownMenu()
        
        return mBinding.root
    }

    private fun setFilledDropDownMenu() {
        val accountsCategory = arrayOf("Customer", "FisherMan")
        val categoryAdapter  = ArrayAdapter<String>(
                mContext,
                R.layout.dropdown_menu_popup_item,
                accountsCategory
        )

        mBinding.filledExposedDropdownUsers.setAdapter(categoryAdapter)
    }


}
