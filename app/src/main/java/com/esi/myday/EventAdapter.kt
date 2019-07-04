package com.esi.myday

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class EventAdapter(val list: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder.creat(parent)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = list[position]
        holder.bind(event)
    }

    class EventViewHolder private constructor(parent: View) : RecyclerView.ViewHolder(parent) {
        var title: TextView = parent.findViewById(R.id.title)
        var location: TextView = parent.findViewById(R.id.location)
        var desc: TextView = parent.findViewById(R.id.desc)
        var start: TextView = parent.findViewById(R.id.start)
        var end: TextView = parent.findViewById(R.id.end)
        var organizer: TextView = parent.findViewById(R.id.organizer)

        fun bind(event: Event) {
            title.text = event.title
            location.text = "Location: ${event.location}"
            //desc.text = "Description: \n"+event.desc
            if (event.t_start != null && event.t_end != null) {
                start.text = "At " + convertLongToDateString(event.t_start.toLong())
                end.text = "To " + convertLongToDateString(event.t_end.toLong())
            }
            organizer.text = "Organized by " + event.organizer

        }


        companion object {
            fun creat(parent: ViewGroup): EventViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.event_list_item, parent, false)
                return EventViewHolder(itemView)
            }
        }

    }
}

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}