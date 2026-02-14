package com.codepath.nationalparks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NationalParksRecyclerViewAdapter(
    private val parks: List<NationalPark>
) : RecyclerView.Adapter<NationalParksRecyclerViewAdapter.ParkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_national_park, parent, false)
        return ParkViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParkViewHolder, position: Int) {
        val park = parks[position]

        holder.parkName.text = park.name ?: ""
        holder.parkLocation.text = park.location ?: ""
        holder.parkDescription.text = park.description ?: ""

        Glide.with(holder.itemView)
            .load(park.imageUrl)
            .centerInside()
            .into(holder.parkImage)
    }

    override fun getItemCount(): Int = parks.size

    class ParkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parkName: TextView = itemView.findViewById(R.id.park_name)
        val parkLocation: TextView = itemView.findViewById(R.id.park_location)
        val parkDescription: TextView = itemView.findViewById(R.id.park_description)
        val parkImage: ImageView = itemView.findViewById(R.id.park_image)
    }
}
