package com.example.samsara.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.samsara.R
import com.example.samsara.databinding.SavedRowBinding
import com.example.samsara.datamodel.ApartmentDataModel

class SavedRecycleAdapter : RecyclerView.Adapter<SavedRecycleAdapter.SavedRecycleVH>() {
    inner class SavedRecycleVH(val binding: SavedRowBinding) : RecyclerView.ViewHolder(binding.root)
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
      //  holder.binding.homeArea.text =item.homeArea.toString()
        holder.binding.root.setOnLongClickListener {

            true
        }

    }
//    fun setDataSource(infoList: List<HomeData>) {
//        this.listOfApartment.clear()
//        this.listOfApartment.addAll(infoList)
//        notifyDataSetChanged()
//    }
}