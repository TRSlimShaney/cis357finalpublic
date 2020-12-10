package edu.gvsu.cis.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.gvsu.cis.notifications.Items.TimeContent

class SchedulerDataViewModel : ViewModel(){

    private var _selected = MutableLiveData<TimeContent.TimeItem>()

    val selected get() = _selected
}