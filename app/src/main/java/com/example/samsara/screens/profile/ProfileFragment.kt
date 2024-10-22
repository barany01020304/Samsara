package com.example.samsara.screens.profile

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.bumptech.glide.Glide
import com.example.samsara.MainActivity
import com.example.samsara.R
import com.example.samsara.databinding.FragmentProfileBinding
import com.example.samsara.screens.main.MainFragmentDirections

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =DataBindingUtil.inflate(layoutInflater,R.layout.fragment_profile,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = ProfileViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Handle the image Uri here
                result.data?.data?.let { imageUri ->
                    viewModel.setImage(imageUri)

                    //   binding.profileIV.load(imageUri)  // Load the image into an ImageView
                }
            }
        }
        val activity=activity as MainActivity
        binding.aboutUsView.setOnClickListener {
            activity.navController.navigate(MainFragmentDirections.actionMainFragmentToAboutUsFragment())
        }
        viewModel.profileImage.observe(viewLifecycleOwner){
            Glide.with(this)
                .load(it)
                .circleCrop()  // Circular crop
                .into(binding.profileIV).onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.profile, null))
        }

        viewModel.textData.observe(viewLifecycleOwner){
            binding.nameTV.text=it.first
            binding.phoneET.text=it.second
            binding.locationET.text=it.third
        }

        binding.profileIV.setOnLongClickListener {
            openGallery()
            true
        }
    }

    private fun openGallery() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"  // Filter to only show images
            }
        // Launch the intent to open the gallery
        pickImageLauncher.launch(intent)
    }

}