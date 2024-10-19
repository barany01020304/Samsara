package com.example.samsara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.example.samsara.R
import com.example.samsara.databinding.FacilitiesRowBinding
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datamodel.PublicService
import kotlin.coroutines.coroutineContext

class FacilitiesRecyclerView() :RecyclerView.Adapter<FacilitiesRecyclerView.FacilitiesViewHolder>() {
    var data: List<PublicService> = emptyList()
    var context: Context? =null
    inner class FacilitiesViewHolder(val binding:FacilitiesRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilitiesViewHolder {
        context=parent.context
        val binding =DataBindingUtil.inflate<FacilitiesRowBinding>(LayoutInflater.from(parent.context),R.layout.facilities_row,parent,false)
        return FacilitiesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FacilitiesViewHolder, position: Int) {
        val item=data[position]
        if (item.howFar==null){
            holder.binding.imgRecyclerViewFacilitiesId.load(item.img){
                error(R.drawable.wifi_square)
            }
            holder.binding.txtRecyclerViewFacilitiesId.text=item.service
            holder.binding.txtDistanceRecyclerViewFacilitiesId.visibility=View.GONE
        }else{
            holder.binding.imgRecyclerViewFacilitiesId
            holder.binding.imgRecyclerViewFacilitiesId.load(item.img)
            holder.binding.txtRecyclerViewFacilitiesId.text=item.service
            holder.binding.txtDistanceRecyclerViewFacilitiesId.text=item.howFar
            val imgLayoutParams = holder.binding.imgRecyclerViewFacilitiesId.layoutParams as ViewGroup.MarginLayoutParams
            imgLayoutParams.setMargins(0, 10, 0, 0) // left, top, right, bottom
        }
//        item.image?.let {
//            holder.facilitiesImg?.setImageResource(it)
//        }
//        holder.facilitiestxt?.text=item.typefacility
//        holder.facilitiesDistance?.text=item.distance
    }
}