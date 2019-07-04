package com.esi.myday

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.provider.CalendarContract
import java.text.SimpleDateFormat

class CalendarContentResolver(ctx: Context) {

    private var contentResolver: ContentResolver = ctx.contentResolver
    private var calendars: MutableSet<Calendar> = HashSet()
    private var events: MutableList<Event> = mutableListOf()

    fun getCalendars(): Set<Calendar> {
        // Fetch a list of all calendars sync'd with the device and their display names
        val cursor = contentResolver.query(CALENDAR_URI, CALENDAR_FIELDS, null, null, null)

        try {
            if (cursor!!.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars._ID))
                    val name = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME))
                    // This is actually a better pattern:
                    val owner = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.OWNER_ACCOUNT))
                    val type = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE))
                    val selected = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.VISIBLE)) != "0"
                    if (selected && type == "com.google") {
                        calendars.add(Calendar(id, name, owner, type, selected))
                    }
                }
            }
        } catch (ex: AssertionError) { /*TODO: log exception and bail*/
        }

        return calendars
    }


    fun getEvents(): List<Event> {
        // Fetch a list of all calendars sync'd with the device and their display names
        val cursor = contentResolver.query(EVENT_URI, EVENT_FIELDS, null, null, null)

        try {
            if (cursor!!.count > 0) {
                while (cursor.moveToNext()) {
                    val t_start = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTSTART))
                    if (checkDate(t_start.toLong())) {
                        val id = cursor.getString(cursor.getColumnIndex(CalendarContract.Events._ID))
                        val title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE))
                        val location = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION))
                        val desc = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION))
                        val t_end = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTEND))
                        val organizer = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.ORGANIZER))
                        val type = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.ACCOUNT_TYPE))
                        if (title != null && id != null) {

                            val event = Event(id, title, location, desc, t_start, t_end, organizer)
                            events.add(event)
                        }
                    }
                }
            }
        } catch (ex: AssertionError) { /*TODO: log exception and bail*/
        }
        events.sortedByDescending { event ->
            event.t_start
        }

        return events
    }

    companion object {
        val CALENDAR_FIELDS = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.OWNER_ACCOUNT,
            CalendarContract.Calendars.ACCOUNT_TYPE,
            CalendarContract.Calendars.VISIBLE
        )

        val EVENT_FIELDS = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.ORGANIZER,
            CalendarContract.Events.ACCOUNT_TYPE
        )

        val CALENDAR_URI = CalendarContract.Calendars.CONTENT_URI
        val EVENT_URI = CalendarContract.Events.CONTENT_URI
    }
}


@SuppressLint("SimpleDateFormat")
fun checkDate(systemTime: Long): Boolean {
    val date = SimpleDateFormat("-MMM-yyyy").format(systemTime).toString()
    val today = SimpleDateFormat("dd").format(systemTime).toString()
    val tmr = (today.toInt() + 1).toString()
    val date_now = SimpleDateFormat("dd-MMM-yyyy").format(System.currentTimeMillis()).toString()
    //if (date_now == (today + date) || date_now == (tmr + date)) return true
    return systemTime > System.currentTimeMillis()
}