package com.example.samsara.screens.searchscreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.samsara.MainActivity
import com.example.samsara.R
import com.example.samsara.adapters.RecyclerViewAdapters
import com.example.samsara.databinding.FragmentSearchScreenBinding
import com.example.samsara.datamodel.ApartmentDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class SearchScreenFragment : Fragment() {
    private lateinit var binding: FragmentSearchScreenBinding
    private lateinit var adapter: RecyclerViewAdapters
    private lateinit var viewModel: SearchScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.fragment_search_screen, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchViewId.requestFocus()
        showKeyboard(binding.searchViewId)
        val viewModelFactory = SearchScreenViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this,viewModelFactory)[SearchScreenViewModel::class.java]
        val activity = activity as MainActivity
        viewModel.allApartments.observe(viewLifecycleOwner) {
            setUpAutoComplete(it.mapNotNull {
                it.location
            })
        }
        observeSearch()

        viewModel.searchResult.observe(viewLifecycleOwner) {
            setUpSearchRecycle(it)
        }

        adapter = RecyclerViewAdapters(activity) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.saveItemToRoom(it)
            }
            Toast.makeText(requireContext(), "this Item saved", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showKeyboard(editText: EditText) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun setUpAutoComplete(list: List<String>) {
        val textAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                list.toSet().toList()
            )
        binding.searchViewId.setAdapter(textAdapter)

    }

    private fun setUpSearchRecycle(list: List<ApartmentDataModel>) {
        binding.searchRecyclerView.adapter = adapter
        adapter.data = list
    }

    private fun observeSearch() {
        val editText=binding.searchViewId
        var textToPass = editText.text.toString()
        editText.doAfterTextChanged { text ->
            // Handle the text change
            textToPass = text.toString()
            CoroutineScope(Dispatchers.IO).launch {

                //  repeat(999999){
               // delay(100L)
                viewModel.setSearchResult(textToPass)

                //  }

            }
        }



    }
}