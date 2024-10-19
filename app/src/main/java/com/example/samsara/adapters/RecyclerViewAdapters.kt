package com.example.samsara.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.room.ForeignKey
import coil.load
import com.example.samsara.MainActivity
import com.example.samsara.R
import com.example.samsara.databinding.RecyclerviewRowBinding
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.screens.main.MainFragmentDirections

class RecyclerViewAdapters(private val activity: MainActivity,private val onItemClick:(ApartmentDataModel) ->Unit) :RecyclerView.Adapter<RecyclerViewAdapters.RecyclerViewHolder>() {
     var data: List<ApartmentDataModel> = emptyList()
    inner class RecyclerViewHolder( var binding:RecyclerviewRowBinding ):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding:RecyclerviewRowBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.recyclerview_row,parent,false)
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val item=data[position]
//        item.homeImag?.let {
//                holder.homeImag?.setImageResource(it)
//        }
        holder.binding.imageInRowImageView.load(item.photos?.get(0)){

            error(R.drawable.pic_test_drawable)
        }
        holder.binding.costTextView.text="${item.price} Pound"
        holder.binding.locationTextView.text=item.location
        holder.binding.detailsTextView.text=item.description
        holder.binding.roomNum.text=item.rooms.toString()
        holder.binding.ratingTextView.text=item.rating.toString()
        holder.binding.homeArea.text =item.homeArea.toString()
        holder.binding.heartId.setOnClickListener {
            onItemClick(item)
        }
        holder.binding.root.setOnClickListener {
            if(item.type =="Rent"){
                activity.navController.navigate(MainFragmentDirections.actionMainFragmentToRentDetailsFragment(item.id))
            }
            else{
                activity.navController.navigate(MainFragmentDirections.actionMainFragmentToBuyDetialsFragment(item.id))
            }
        }
//        holder.itemView.setOnClickListener {
//            when(fragment_position){
//                0->Navigation.findNavController(it).navigate(R.id.action_viewPagerFragment_to_rentDetailsFragment2)
//                else->Navigation.findNavController(it).navigate(R.id.action_viewPagerFragment_to_buyDetialsFragment)
//            }
//        }
    }
}