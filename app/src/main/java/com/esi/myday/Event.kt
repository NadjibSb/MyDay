package com.esi.myday

data class Event(
    val id:String,
    val title: String,
    val location: String?,
    val desc: String?,
    val t_start: String?,
    val t_end: String?,
    val organizer: String?
) {
}