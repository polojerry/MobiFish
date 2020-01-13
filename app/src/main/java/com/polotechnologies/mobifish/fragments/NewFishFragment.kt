package com.polotechnologies.mobifish.fragments


import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.polotechnologies.mobifish.R
import com.polotechnologies.mobifish.databinding.FragmentCustomerBinding
import com.polotechnologies.mobifish.databinding.FragmentNewFishBinding

/**
 * A simple [Fragment] subclass.
 */
class NewFishFragment : Fragment(), View.OnClickListener {

    val REQUEST_IMAGE_GET = 10
    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mBinding: FragmentNewFishBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_new_fish, container, false)
        mBinding.toolbarNewFish.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back)
        mBinding.toolbarNewFish.setNavigationOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference


        mBinding.imgFishImage.setOnClickListener{
            Toast.makeText(context,"Clicked", Toast.LENGTH_SHORT).show()
            selectImageLocation()
        }


        return mBinding.root}

    private fun selectImageLocation() {
        val imageDialog = AlertDialog.Builder(context!!)
        imageDialog.setTitle("Image Location")
        imageDialog.setItems(R.array.image_locations, DialogInterface.OnClickListener { _, which ->
            when(which){
                0 -> captureImage()
                1 -> selectFromGallery()
            }
        })
        imageDialog.show()
    }

    private fun selectFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    private fun captureImage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(v: View?) {
        findNavController().navigate(R.id.action_newFishFragment_to_fisherManFragment)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val thumbnail: Bitmap = data!!.getParcelableExtra("data")
            val fullPhotoUri: Uri = data.data!!
            // Do work with photo saved at fullPhotoUri

            Glide.with(this.context!!)
                    .load(fullPhotoUri)
                    .into(mBinding.imgFishImage)
        }
    }

}
