package com.example.samsara.screens.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.samsara.R
import com.example.samsara.adapters.FacilitiesRecyclerView
import com.example.samsara.databinding.FragmentDetailsBinding
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datamodel.PublicService
import com.example.samsara.datamodel.SpecialService


class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    lateinit var viewModel: DetailsViewModel
    private val adapter1: FacilitiesRecyclerView by lazy {
        FacilitiesRecyclerView()
    }
    private val adapter2: FacilitiesRecyclerView by lazy {
        FacilitiesRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: DetailsFragmentArgs by navArgs()
        val viewModelFactory = DetailsViewModelFactory(args.id, requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
        viewModel.apartment.observe(viewLifecycleOwner) {
            setViews(it)
            setOnClick(it)

            binding.loadingPB.visibility = View.GONE
            binding.insideCL.visibility = View.VISIBLE
        }
        viewModel.imageUrls.observe(viewLifecycleOwner) { urls ->
            loadImage(urls[viewModel.currentIndex.value ?: 0])
        }

        viewModel.currentIndex.observe(viewLifecycleOwner) { index ->
            viewModel.imageUrls.value?.let { loadImage(it[index]) }
        }


    }

    fun setOnClick(apartmentDataModel: ApartmentDataModel) {
        binding.arrowRightId.setOnClickListener {
            viewModel.nextImage()
        }

        binding.arrowLeftId.setOnClickListener {
            viewModel.previousImage()
        }
        binding.imgbackId.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.mapIV.setOnClickListener {
            openLocationInMaps(apartmentDataModel.latitude, apartmentDataModel.longitude)
        }
        binding.imgContact.setOnClickListener {
            callNumber(requireContext())
        }
        binding.imgChat.setOnClickListener {
            shareTextAndLink(
                requireContext(),
                "${apartmentDataModel.building} that have id=  ${apartmentDataModel.id} and location is in",
                getGoogleMapsLink(apartmentDataModel.latitude, apartmentDataModel.longitude)
            )
        }


    }
    private fun callNumber(context: Context) {
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:+201027453029")
        }
        context.startActivity(callIntent)
    }
    private fun getGoogleMapsLink(latitude: Double, longitude: Double): String {
        return "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
    }

    private fun setViews(apartmentDataModel: ApartmentDataModel) {
        binding.apply {
            mapIV.load(apartmentDataModel.locationInMap)
            detailsTextView.text = apartmentDataModel.description
            locationTextView.text = apartmentDataModel.location
            ratingTextView.text = apartmentDataModel.rating.toString()
            roomNum.text = apartmentDataModel.rooms.toString()
            homeSize.text = apartmentDataModel.homeArea.toString()
            aboutNeighborhoodDescriptioId.text = apartmentDataModel.locationsNeighborhood


        }
        setHomeFac(apartmentDataModel.specialServices)
        setpublicFac(apartmentDataModel.publicServices)
    }

    private fun openLocationInMaps(latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Google Maps not installed.", Toast.LENGTH_SHORT)
                .show()
        }
    }
   // val phoneNumber = "+201027453029"
   // I want to buy this
   private fun shareTextAndLink(context: Context, text: String, link: String) {
        // WhatsApp number with country code (replace with your static number)
        val phoneNumber = "201027453029" // Replace with actual number in international format, without the "+" sign

        // Text message to send
        val message = "$text\n$link"

        // WhatsApp API URL
        val whatsappUrl = "https://wa.me/$phoneNumber?text= I want to buy this ${Uri.encode(message)}"

        // Create intent for WhatsApp
        val whatsappIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(whatsappUrl)
            setPackage("com.whatsapp") // Target WhatsApp
        }

        // Create intent for WhatsApp Business
        val whatsappBusinessIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(whatsappUrl)
            setPackage("com.whatsapp.w4b") // Target WhatsApp Business
        }

        // Check if WhatsApp is installed
        if (whatsappIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(whatsappIntent)
        }
        // If WhatsApp is not installed, check for WhatsApp Business
        else if (whatsappBusinessIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(whatsappBusinessIntent)
        }
        // Neither WhatsApp nor WhatsApp Business is installed
        else {
            Toast.makeText(context, "Neither WhatsApp nor WhatsApp Business is installed on this device", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadImage(url: String) {
        binding.apartPhotosIV.load(url)
    }

    private fun setHomeFac(homFac: List<SpecialService>) {
        //remmber
        binding.apply {
            homeFacilityRecyclerView.adapter = adapter1
            //var emt
            val convertedSpecialServices = homFac.map { specialService ->
                PublicService(
                    img = specialService.img, service = specialService.service, howFar = null
                )
            }
            adapter1.data = convertedSpecialServices

        }

    }

    private fun setpublicFac(pupService: List<PublicService>) {
        binding.apply {
            nearestFacilityRecyclerView.adapter = adapter2
            adapter2.data = pupService

        }
    }
}