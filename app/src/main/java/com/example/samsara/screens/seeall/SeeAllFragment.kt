package com.example.samsara.screens.seeall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.samsara.MainActivity
import com.example.samsara.R
import com.example.samsara.adapters.RecyclerViewAdapters
import com.example.samsara.databinding.FragmentSeeAllBinding
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.screens.home.HomeViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SeeAllFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SeeAllFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSeeAllBinding
    private lateinit var viewModel: SeeAllViewModel

    //   lateinit var  rentNearAdapter:RecyclerViewAdapters
//    lateinit var  rentTopAdapter:RecyclerViewAdapters
//    lateinit var  buyNearAdapter:RecyclerViewAdapters
//    lateinit var  buyTopadapter:RecyclerViewAdapters
    lateinit var adapter: RecyclerViewAdapters
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_see_all, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: SeeAllFragmentArgs by navArgs()
        val application = requireActivity().application
        val viewModelFactory = SeeAllViewModelFactory(
            application,
            args.rentOrBuy, args.topOrNear
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[SeeAllViewModel::class.java]
        val activity = activity as MainActivity
        setAdapters(activity)
        viewModel.listOfApartment.observe(viewLifecycleOwner) {
            setUpRecycle(it)
        }
    }

    private fun setAdapters(activity: MainActivity) {
        adapter = RecyclerViewAdapters(activity) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.saveItemToRoom(it)
            }
            Toast.makeText(requireContext(), "this Item saved", Toast.LENGTH_SHORT).show()
        }
//        rentNearAdapter = RecyclerViewAdapters(activity) {
//            CoroutineScope(Dispatchers.IO).launch {
//                viewModel.saveItemToRoom(it)
//            }
//            Toast.makeText(requireContext(), "this Item saved", Toast.LENGTH_SHORT).show()
//        }
//        rentTopAdapter = RecyclerViewAdapters(activity) {
//            CoroutineScope(Dispatchers.IO).launch {
//                viewModel.saveItemToRoom(it)
//            }
//            Toast.makeText(requireContext(), "this Item saved", Toast.LENGTH_SHORT).show()
//        }
//        buyNearAdapter = RecyclerViewAdapters(activity) {
//            CoroutineScope(Dispatchers.IO).launch {
//                viewModel.saveItemToRoom(it)
//            }
//            Toast.makeText(requireContext(), "this Item saved", Toast.LENGTH_SHORT).show()
//        }
//        buyTopadapter = RecyclerViewAdapters(activity) {
//            CoroutineScope(Dispatchers.IO).launch {
//                viewModel.saveItemToRoom(it)
//            }
//            Toast.makeText(requireContext(), "this Item saved", Toast.LENGTH_SHORT).show()
//        }

    }

    private fun setUpRecycle(list: List<ApartmentDataModel>) {
        binding.seeAllRecycleView.adapter = adapter
        adapter.data = list
        // delay(500L)
        binding.seeAllPB.visibility = View.GONE
        binding.seeAllRecycleView.visibility = View.VISIBLE

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SeeAllFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SeeAllFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}