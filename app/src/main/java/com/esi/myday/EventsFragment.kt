package com.esi.myday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.esi.myday.databinding.EventsFragmentBinding


class EventsFragment : Fragment() {

    companion object {
        fun newInstance() = EventsFragment()
    }

    private lateinit var viewModel: EventsViewModel
    private lateinit var binding: EventsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.events_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)


        var calendarContentResolver = CalendarContentResolver(context!!)
        var calendars = calendarContentResolver.getCalendars()
        var events = calendarContentResolver.getEvents()
        setupRecycleView(events)
    }


    private fun setupRecycleView(list: List<Event>) {
        var recycleView = binding.recycleView
        var adapter = EventAdapter(list)
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.setHasFixedSize(true)
    }

}
