package edu.gvsu.cis.notifications.Items

import java.util.*

object TimeContent {

    val ITEMS: MutableList<TimeItem> = ArrayList()

    fun addItem(item: TimeItem) {
        ITEMS.add(item)
    }

    fun deleteItem(item: TimeItem) {
        ITEMS.remove(item)
    }

    data class TimeItem(val Hour: Int, val Minute: Int, val calendar: Calendar)
    {

        override fun toString(): String {
            val format: String
            var hourTemp: Int = Hour
            if (Hour == 0) {
                hourTemp += 12;
                format = "AM";
            } else if (Hour == 12) {
                format = "PM";
            } else if (Hour > 12) {
                hourTemp -= 12;
                format = "PM";
            } else {
                format = "AM";
            }
            return if (Minute < 10) {
                "$hourTemp:0$Minute $format"
            } else {
                "$hourTemp:$Minute $format"
            }
        }
    }
}


