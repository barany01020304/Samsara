package com.example.samsara.screens.details

import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.samsara.R
import com.example.samsara.adapters.FacilitiesRecyclerView
import com.example.samsara.databinding.FragmentRentHotelDetailsBinding
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datamodel.PublicService
import com.example.samsara.datamodel.SpecialService
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class RentHotelDetails : Fragment() {
    private lateinit var binding: FragmentRentHotelDetailsBinding
    private lateinit var viewModel: DetailsViewModel
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var roomNum =0
    private var adultNum=0
    private var childNum=0
    private var infantNum =0
    private val dateFormat = SimpleDateFormat("dd MMMM ", Locale.getDefault())  // Date format

    private val adapter1: FacilitiesRecyclerView by lazy {
        FacilitiesRecyclerView()
    }
    private val adapter2: FacilitiesRecyclerView by lazy {
        FacilitiesRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_rent_hotel_details,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: RentHotelDetailsArgs by navArgs()
        val viewModelFactory = DetailsViewModelFactory(args.id, requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
        viewModel.apartment.observe(viewLifecycleOwner) {
            setForHotel(it.building!!)
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
        binding.startDayId.setOnClickListener {
            showStartDatePicker()
        }
        binding.endDayId.setOnClickListener {
            showEndDatePicker()
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
        binding.butShowResult.setOnClickListener {
            NavtoCheckOut(apartmentDataModel)
        }
        plusButtons(binding.plusRoomsBTN,apartmentDataModel.rooms)
        plusButtons(binding.plusAdultBTN)
        plusButtons(binding.plusChildBTN)
        plusButtons(binding.plusInfantBTN)

         minusButtons(binding.minusRoomsBTN)
        minusButtons(binding.minusAdultBTN)
        minusButtons(binding.minusChildBTN)
        minusButtons(binding.minusInfantsBTN)

    }
    fun plusButtons(view:View,rooms:Int=0){
        view.setOnClickListener {
            if (view.id ==binding.plusRoomsBTN.id){
                if (roomNum <rooms){
                    roomNum++
                    binding.roomNumTV.text=roomNum.toString()

                }else{
                    Toast.makeText(requireContext(),"we have only $rooms rooms",Toast.LENGTH_SHORT).show()
                }
            }else if (view.id ==binding.plusAdultBTN.id){
                adultNum++
                binding.adultNumTV.text=adultNum.toString()
            }else if (view.id ==binding.plusChildBTN.id){
                childNum++
                binding.childNumTV.text=childNum.toString()

            }else if (view.id ==binding.plusInfantBTN.id){
                infantNum++
                binding.infantNumTV.text=infantNum.toString()
            }
        }


    }
    fun minusButtons(view:View){

        view.setOnClickListener {
            if (view.id ==binding.minusRoomsBTN.id){
                if (roomNum >0){
                    roomNum--
                }
                binding.roomNumTV.text=roomNum.toString()
            }else if (view.id ==binding.minusAdultBTN.id){
                if (adultNum >0){
                    adultNum--
                }
                binding.adultNumTV.text=adultNum.toString()
            }else if (view.id ==binding.minusChildBTN.id){
                if (childNum>0){
                    childNum--
                }
                binding.childNumTV.text=childNum.toString()
            }else if (view.id ==binding.minusInfantsBTN.id){
                if (infantNum>0){
                    infantNum--
                }

                binding.infantNumTV.text=infantNum.toString()
            }
        }


    }
fun NavtoCheckOut(apartment:ApartmentDataModel){
    if (startDate !=null && endDate !=null){
        findNavController().navigate(RentHotelDetailsDirections.actionRentDetailsFragmentToCheckOutFragment
            (apartment.rating.toFloat(),apartment.description!!,apartment.location!!,apartment.photos[0],"${dateFormat.format(startDate?.time)} - ${dateFormat.format(endDate?.time)}",countGuest(),calculateDaysBetween(startDate!!,endDate!!).toInt().toString(),apartment.price))
    }else{
        Toast.makeText(requireContext(),"please put date or you didn't choose rooms",Toast.LENGTH_LONG).show()
    }
}
    fun countGuest():String{
        return " $adultNum adult \n $childNum childs \n $infantNum infants"

    }
    fun setForHotel(building: String) {
        if (building == "hotel") {
            binding.constraintForHotel.visibility = View.VISIBLE
        } else {
            binding.constraintForHotel.visibility = View.GONE
        }
    }

    fun calculateDaysBetween(startDate: Calendar, endDate: Calendar): Long {
        // Get the time in milliseconds for both dates
        val startTimeInMillis = startDate.timeInMillis
        val endTimeInMillis = endDate.timeInMillis

        // Calculate the difference in milliseconds
        val differenceInMillis = endTimeInMillis - startTimeInMillis

        // Convert milliseconds to days
        return TimeUnit.MILLISECONDS.toDays(differenceInMillis)
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
    // Function to show DatePicker dialog
    private fun showStartDatePicker() {
        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1) // Tomorrow's date
        }

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                startDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                // Update button text with formatted date
                binding.startDayId.text = "Start: ${dateFormat.format(startDate?.time)}"

                // Enable the end date button now that the start date is selected
                binding.endDayId.isEnabled = true
            },
            tomorrow.get(Calendar.YEAR),
            tomorrow.get(Calendar.MONTH),
            tomorrow.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = tomorrow.timeInMillis // Disable past dates
        }.show()
    }

    private fun showEndDatePicker() {
        val dayAfterTomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 2) // The day after tomorrow
        }

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                endDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                // Update button text with formatted date
                binding.endDayId.text = "End Date: ${dateFormat.format(endDate?.time)}"
            },
            dayAfterTomorrow.get(Calendar.YEAR),
            dayAfterTomorrow.get(Calendar.MONTH),
            dayAfterTomorrow.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = dayAfterTomorrow.timeInMillis // Ensure at least the day after tomorrow is selectable
        }.show()
    }
//    private fun getMonthString(month: Int): String {
//        return when (month) {
//            0 -> "Jan"
//            1 -> "Feb"
//            2 -> "Mar"
//            3 -> "Apr"
//            4 -> "May"
//            5 -> "Jun"
//            6 -> "Jul"
//            7 -> "Aug"
//            8 -> "Sep"
//            9 -> "Oct"
//            10 -> "Nov"
//            11 -> "Dec"
//            else -> "Invalid"
//        }
//    }

    private fun openLocationInMaps(latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude?z=19")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Google Maps not installed.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadImage(url: String) {
        binding.viewSwitcherId.load(url)
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