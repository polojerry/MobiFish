package com.polotechnologies.mobifish.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.polotechnologies.mobifish.R
import com.polotechnologies.mobifish.dataModels.User
import com.polotechnologies.mobifish.databinding.FragmentSignUpBinding

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

    private lateinit var mBinding: FragmentSignUpBinding
    private lateinit var mContext:Context

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabaseReference: DatabaseReference
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up, container, false)
        mContext = this.context!!
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference

        val phoneNumber = SignUpFragmentArgs.fromBundle(arguments!!).userPhoneNumber

        setFilledDropDownMenu()

        mBinding.btnSignUp.setOnClickListener {
            signUpUser(phoneNumber)
        }
        return mBinding.root
    }

    private fun signUpUser(phoneNumber: String) {
        val fullName = mBinding.etSignUpUserName.text.toString()
        val userCategory = mBinding.filledExposedDropdownUsers.text.toString()
        val idNumber = mBinding.etIdNumber.text.toString()
        val uid = mAuth.currentUser!!.uid


        val createdUser = User(
                fullName,
                idNumber,
                userCategory,
                phoneNumber,
                uid
        )

        mDatabaseReference.child(getString(R.string.db_node_users)).child(uid).setValue(createdUser)
                .addOnSuccessListener {
                    Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {
                    Toast.makeText(context,"Failure",Toast.LENGTH_SHORT).show()
                }
    }

    private fun setFilledDropDownMenu() {
        val accountsCategory = arrayOf(getString(R.string.user_category_customer), getString(R.string.user_category_fisher_man))
        val categoryAdapter  = ArrayAdapter<String>(
                mContext,
                R.layout.dropdown_menu_popup_item,
                accountsCategory
        )

        mBinding.filledExposedDropdownUsers.setAdapter(categoryAdapter)
    }


}
