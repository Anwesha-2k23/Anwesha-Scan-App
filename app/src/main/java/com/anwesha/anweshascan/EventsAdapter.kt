package com.anwesha.anweshascan

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anwesha.anweshascan.databinding.EventDesignBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class EventsAdapter( private val context: Context): RecyclerView.Adapter<EventsAdapter.MyViewHolder>(){

    private lateinit var listener: OnItemClickListener
    private var eventList: kotlin.collections.ArrayList<EventsList> = ArrayList()

    fun setEvents(events: kotlin.collections.ArrayList<EventsList>){
        eventList = events
    }

    //Interface that will tell what happens when a event is clicked
    interface OnItemClickListener{
        fun onItemClicked(event: EventsList)
    }

    fun setOnItemClickListener(mListener: OnItemClickListener){
        listener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(EventDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = eventList[position]
        holder.eventName.text = currentItem.name

        holder.itemView.setOnClickListener{
            listener.onItemClicked(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class MyViewHolder( binding: EventDesignBinding ):RecyclerView.ViewHolder(binding.root){
        val eventName : TextView = binding.eventName
    }

}