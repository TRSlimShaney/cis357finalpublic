package edu.gvsu.cis.notifications

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.annotation.RequiresApi

import edu.gvsu.cis.notifications.Items.TimeContent.TimeItem
import edu.gvsu.cis.notifications.Items.TimeContent.deleteItem

/**
 * [RecyclerView.Adapter] that can display a [HistoryItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ScheduleRecyclerViewAdapter(
    private val values: MutableList<TimeItem>,
    val listener: ((TimeItem) -> Unit)? = null)
    : RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_schedules, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(values[position],listener)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)
    {
        val mP1: TextView
      //  val picker: TimePicker
        var tItem: TimeItem? = null
        val parentView:View

        @RequiresApi(Build.VERSION_CODES.M)
        override fun toString(): String {
            return super.toString() + " '"  +"'"
        }

        init {
          //  picker = mView.findViewById<TimePicker>(R.id.datePicker1);
            mP1 = mView.findViewById<View>(R.id.p1) as TextView
            parentView = mView
            mView.findViewById<ImageButton>(R.id.deleteButton).setOnClickListener {
                values.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        public fun bindTo(d: TimeItem, listener: ((TimeItem) -> Unit)?) {
            tItem = d
        //    picker.hour =  d.Hour
          //  picker.minute = d.Minute
            mP1.text = d.toString()

            if (listener != null) {
                parentView.setOnClickListener {
                    listener(d)
                }
            }
        }
    }



}