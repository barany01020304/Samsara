package com.example.samsara.screens.rent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.samsara.MainActivity
import com.example.samsara.R
import com.example.samsara.adapters.RecyclerViewAdapters
import com.example.samsara.databinding.FragmentRentBinding
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datasource.local.UserDataSharedPref
import com.example.samsara.screens.main.MainFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RentFragment : Fragment() {
    private lateinit var binding: FragmentRentBinding
    private lateinit var viewModel:RentViewModel
    private lateinit var nearAdapter:RecyclerViewAdapters
    private lateinit var topAdapter:RecyclerViewAdapters
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_rent,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory=RentVIewModelFactory(requireActivity().application)
        viewModel =ViewModelProvider(this,viewModelFactory)[RentViewModel::class.java]
        val activity =activity as MainActivity

        viewModel.apartmentsTop.observe(viewLifecycleOwner){
            setUpTopRecycle(it)
            binding.rentTopPB.visibility =View.GONE
            binding.topRatedRecyclerView.visibility=View.VISIBLE
        }
        viewModel.apartmentsNear.observe(viewLifecycleOwner){
            setUpNearRecycle(it)
            binding.rentNearPB.visibility =View.GONE
            binding.nearRecyclerView.visibility=View.VISIBLE
        }
        setAdapters(activity)
        setSeeAll()
    }

    private fun setAdapters(activity: MainActivity) {
        topAdapter = RecyclerViewAdapters(activity) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.saveItemToRoom(it)
            }
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()

        }
        nearAdapter = RecyclerViewAdapters(activity) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.saveItemToRoom(it)
            }
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()

        }
    }

    private fun setSeeAll(){
        val activity =activity as MainActivity

        binding.nearSeeAllTv.setOnClickListener {
            activity.navController.navigate(MainFragmentDirections.actionMainFragmentToSeeAllFragment("rent","near"))
        }
        binding.topRatedSeeAll.setOnClickListener {
            activity.navController.navigate(MainFragmentDirections.actionMainFragmentToSeeAllFragment("rent","top"))
        }

    }

    private fun setUpTopRecycle(list: List<ApartmentDataModel>) {
        binding.topRatedRecyclerView.adapter=topAdapter
        topAdapter.data=list
    }
    private fun setUpNearRecycle(list: List<ApartmentDataModel>) {
        binding.nearRecyclerView.adapter=nearAdapter
        nearAdapter.data=list
    }
}