package com.example.samsara.screens.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.samsara.R
import com.example.samsara.databinding.FragmentCheckOutBinding

class CheckOutFragment : Fragment() {
    private lateinit var binding:FragmentCheckOutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_check_out,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args:CheckOutFragmentArgs by navArgs()
        setView(args)
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    fun setView(args:CheckOutFragmentArgs){
        binding.imageInRowImageView.load(args.img)
        binding.ratingTextView.text=args.rating.toString()
        binding.detailsTextView.text=args.name
        binding.locationTextView.text=args.location
        binding.costTextView.text=args.pricePerMonth.toString()
        binding.dateTv.text=args.date
        binding.guestTv.text=args.guests
        binding.durationTv.text="staying ${args.daysCount} days"
        binding.priceNumTv.text="${args.daysCount} * ${calculateTotalPrice(args.daysCount.toInt(),args.pricePerMonth).first} Pound"
        binding.feeNumTV.text="${calculateOurPercent(calculateTotalPrice(args.daysCount.toInt(),args.pricePerMonth).second.toDouble())} Pound"
        val totalPrice =calculateTotalPrice(args.daysCount.toInt(),args.pricePerMonth).second+calculateOurPercent(calculateTotalPrice(args.daysCount.toInt(),args.pricePerMonth).second.toDouble())
        binding.totalNumTV.text="$totalPrice Pound"


    }
    fun calculateOurPercent(price: Double): Double {
        val onePercent = price * 0.01
        return onePercent
    }
    fun calculateTotalPrice(daysBetween: Int, monthlyPrice: Int): Pair<Int,Int> {
        // Average number of days in a month
        val averageDaysInMonth = 30.44

        // Calculate the daily price based on the monthly price
        val dailyPrice = monthlyPrice / averageDaysInMonth
         val twoItem:Pair<Int,Int> = dailyPrice.toInt() to (daysBetween * dailyPrice).toInt()
        // Calculate the total price for the number of days
        return twoItem
    }
}