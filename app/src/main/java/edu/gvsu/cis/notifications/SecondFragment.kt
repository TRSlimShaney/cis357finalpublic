package edu.gvsu.cis.notifications

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import edu.gvsu.cis.notifications.Items.TimeContent
import edu.gvsu.cis.notifications.Items.TimeContent.addItem
import edu.gvsu.cis.notifications.Items.TimeContent.deleteItem
import kotlinx.android.synthetic.main.fragment_second.*
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    lateinit var viewModel: SchedulerDataViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SchedulerDataViewModel::class.java)
        viewModel.selected.observe(this.viewLifecycleOwner, Observer { z ->

            datePicker1.hour = z.Hour
            datePicker1.minute = z.Minute
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            if (viewModel.selected.value != null) {
                deleteItem(viewModel.selected.value!!)
            }
            val time = Calendar.getInstance()
            val item = TimeContent.TimeItem(datePicker1.hour, datePicker1.minute, time)
            addItem(item)

            FirebaseMessaging.getInstance().send(
                    RemoteMessage.Builder("1@gmc.googleapis.com")
                            .setMessageId("1")
                            .addData(java.util.Calendar.getInstance().toString(), "${item.Hour}:${item.Minute}")
                            .build()
            )

            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }
}