package com.polotechnologies.mobifish.fragments


import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.polotechnologies.mobifish.R
import com.polotechnologies.mobifish.dataModels.Fish
import com.polotechnologies.mobifish.databinding.FragmentCustomerBinding
import com.polotechnologies.mobifish.databinding.FragmentNewFishBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class NewFishFragment : Fragment(), View.OnClickListener, Toolbar.OnMenuItemClickListener {

    var currentPhotoPath: String = ""

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mStorageReference: StorageReference

    lateinit var mBinding: FragmentNewFishBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_new_fish, container, false)
        mBinding.toolbarNewFish.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back)
        mBinding.toolbarNewFish.setNavigationOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        mStorageReference = FirebaseStorage.getInstance().reference

        mBinding.imgFishImage.setOnClickListener{
            selectImageLocation()
        }
        mBinding.toolbarNewFish.setOnMenuItemClickListener(this)

        return mBinding.root}

    private fun postNewFish() {
        if(currentPhotoPath == ""){
            Toast.makeText(context, "Image Required", Toast.LENGTH_SHORT).show()
            return
        }

        if(mBinding.etFishType.text.isNullOrEmpty()){
            mBinding.etFishType.error = "Fish Type Required"
            return

        }

        if(mBinding.etFishDescription.text.isNullOrEmpty()){
            mBinding.etFishDescription.error = "Fish Description Required"
            return

        }
        if(mBinding.etFishPrice.text.isNullOrEmpty()){
            mBinding.etFishPrice.error = "Fish Price is required"
            return

        }

        if(mBinding.etFishQuantity.text.isNullOrEmpty()){
            mBinding.etFishQuantity.error = "Fish Price is required"
            return

        }

        uploadFish()
    }

    private fun uploadFish() {
        val fishId  = mDatabaseReference.child("fish")
                .push()
                .key
                .toString()

        val imageUrl = uploadFishImage(fishId)
        val newFish = Fish(
                fishId,
                mBinding.etFishType.text.toString(),
                mBinding.etFishDescription.text.toString(),
                mBinding.etFishPrice.text.toString(),
                mBinding.etFishQuantity.text.toString(),
                imageUrl,
                mAuth.currentUser!!.uid

        )

        mDatabaseReference.child("fish").child(fishId).setValue(newFish).addOnSuccessListener {
            Toast.makeText(context,"Fish Uploaded Successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Log.d("New Fish Upload", it.localizedMessage.toString())
            Toast.makeText(context,"Failed to Upload Fish", Toast.LENGTH_SHORT).show()
        }

    }

    private fun uploadFishImage(fishId: String): String {
        var imageUrl = ""
        val imageRef = mStorageReference.child("fish/$fishId.jpg")

        val uploadTask = imageRef.putFile((Uri.parse(currentPhotoPath)))
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageUrl = task.result.toString()
            } else {
                Toast.makeText(context, "Failed to Upload Image", Toast.LENGTH_SHORT).show()
            }
        }



        return imageUrl

    }

    private fun selectImageLocation() {
        val imageDialog = AlertDialog.Builder(context!!)
        imageDialog.setTitle("Image Location")
        imageDialog.setItems(R.array.image_locations) { _, which ->
            when(which){
                0 -> captureImage()
                1 -> selectFromGallery()
            }
        }
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
        /*Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.d("@@IMAGE FILE CREATION@@", "Error Creating Image File")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            context!!,
                            "com.polotechnologies.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
*/
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onClick(v: View?) {
        findNavController().navigate(R.id.action_newFishFragment_to_fisherManFragment)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            if(data!!.hasExtra("data")){
                val thumbnail: Bitmap = data!!.getParcelableExtra("data")
            }

            val fullPhotoUri: Uri = data.data!!
            currentPhotoPath = fullPhotoUri.toString()
            // Do work with photo saved at fullPhotoUri

            Glide.with(this.context!!)
                    .load(fullPhotoUri)
                    .into(mBinding.imgFishImage)

        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            mBinding.imgFishImage.setImageBitmap(imageBitmap)
        }
    }

    companion object {
        const val REQUEST_IMAGE_GET = 10
        const val REQUEST_IMAGE_CAPTURE = 11
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.action_new_fish){
            postNewFish()
        }
        return true
    }

}
