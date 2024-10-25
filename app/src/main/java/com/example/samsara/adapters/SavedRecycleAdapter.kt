package com.example.samsara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.samsara.MainActivity
import com.example.samsara.R
import com.example.samsara.databinding.SavedRowBinding
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.screens.main.MainFragmentDirections
import com.example.samsara.screens.saved.SavedFragmentDirections

class SavedRecycleAdapter(private val activity: MainActivity, private val onHoldClick:(List<String>) ->Unit) : RecyclerView.Adapter<SavedRecycleAdapter.SavedRecycleVH>() {
    inner class SavedRecycleVH(val binding: SavedRowBinding) : RecyclerView.ViewHolder(binding.root)
    val currentDestination = activity.navController.currentDestination?.id
    var data: List<ApartmentDataModel> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRecycleVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<SavedRowBinding>(
            layoutInflater, R.layout.saved_row, parent, false
        )
        return SavedRecycleVH(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SavedRecycleVH, position: Int) {
        val item = data[position]
        holder.binding.imageInRowImageView.load(item.photos?.get(0)){

            error(R.drawable.pic_test_drawable)
        }
        holder.binding.costTextView.text="${item.price} $"
        holder.binding.locationTextView.text=item.location
        holder.binding.detailsTextView.text=item.description
       // holder.binding.roomNum.text=item.rooms.toString()
        holder.binding.ratingTextView.text=item.rating.toString()
        holder.binding.roomNum.text="${item.rooms} rooms"
        holder.binding.homeArea.text ="${item.homeArea} m²"
      //  holder.binding.homeArea.text =item.homeArea.toString()
        holder.binding.root.setOnLongClickListener {
        onHoldClick(item.photos)
            true
        }
        holder.binding.root.setOnClickListener {

            if(item.type =="Rent"){
                activity.navController.navigate(SavedFragmentDirections.actionSavedFragmentToRentDetailsFragment(item.id))
            }
            else {
                activity.navController.navigate(SavedFragmentDirections.actionSavedFragmentToRentDetailsFragment(item.id))
            }
        }

    }
}