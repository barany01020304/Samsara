package com.example.samsara.screens.buy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.samsara.MainActivity
import com.example.samsara.R
import com.example.samsara.adapters.RecyclerViewAdapters
import com.example.samsara.databinding.FragmentBuyBinding
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datasource.local.UserDataSharedPref
import com.example.samsara.screens.home.HomeViewModel
import com.example.samsara.screens.main.MainFragmentDirections
import com.example.samsara.screens.rent.RentVIewModelFactory
import com.example.samsara.screens.rent.RentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BuyFragment : Fragment() {
    private lateinit var binding: FragmentBuyBinding
    private lateinit var viewModel: BuyViewModel
    private lateinit var nearAdapter:RecyclerViewAdapters
    private lateinit var topAdapter:RecyclerViewAdapters
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater, R.layout.fragment_buy,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory= BuyVIewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this,viewModelFactory)[BuyViewModel::class.java]
        val activity =activity as MainActivity
        setAdapters(activity)
        setSeeAll(activity)
        viewModel.apartmentsTop.observe(viewLifecycleOwner){
            setUpTopRecycle(it)
            binding.buyTopPB.visibility =View.GONE
            binding.buyTopRatedRecyclerView.visibility=View.VISIBLE
        }
        viewModel.apartmentsNear.observe(viewLifecycleOwner){
            setUpNearRecycle(it)
            binding.buyNearPB.visibility =View.GONE
            binding.buyNearRecyclerView.visibility=View.VISIBLE
        }
    }
    private fun setSeeAll(activity: MainActivity){

        binding.nearSeeAllTv.setOnClickListener {
            activity.navController.navigate(MainFragmentDirections.actionMainFragmentToSeeAllFragment("buy","near"))
        }
        binding.topRatedSeeAll.setOnClickListener {
            activity.navController.navigate(MainFragmentDirections.actionMainFragmentToSeeAllFragment("buy","top"))
        }

    }
    private fun setAdapters(activity: MainActivity) {
        nearAdapter = RecyclerViewAdapters(activity) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.saveItemToRoom(it)
            }
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
        }
        topAdapter = RecyclerViewAdapters(activity) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.saveItemToRoom(it)
            }
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpTopRecycle(list: List<ApartmentDataModel>) {
        binding.buyTopRatedRecyclerView.adapter=topAdapter
        topAdapter.data=list


    }
    private fun setUpNearRecycle(list: List<ApartmentDataModel>) {
        binding.buyNearRecyclerView.adapter=nearAdapter
        nearAdapter.data=list

    }
}