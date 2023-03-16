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
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        Glide.with(context)
            .load(currentItem.poster)
            .into(holder.eventPoster)
        holder.eventName.text = currentItem.name

        val venue = currentItem.venue!!.split(",")
        holder.eventLocation.text = venue[0]

        val inputString = currentItem.start_time
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())

        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // set input timezone to UTC
        val date = inputFormat.parse(inputString!!) // parse input string into date object
        outputFormat.timeZone = TimeZone.getDefault() // set output timezone to default timezone
        val outputString = outputFormat.format(date!!) // format date object into output string
        val separatedStrings = outputString.split(",").map { it.trim() }
        holder.eventDate.text = separatedStrings[0]
        holder.eventTime.text = separatedStrings[1]
        holder.itemView.startAnimation(animation)
        holder.itemView.setOnClickListener{
            listener.onItemClicked(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class MyViewHolder( binding: EventDesignBinding ):RecyclerView.ViewHolder(binding.root){
        val eventPoster : ImageView = binding.eventPoster
        val eventName : TextView = binding.eventName
        val eventLocation: TextView = binding.eventLocation
        val eventDate: TextView = binding.eventDate
        val eventTime: TextView = binding.eventTime
    }

}