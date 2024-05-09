package com.example.homemanagement.Room_fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TabHost
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.homemanagement.Components_fragments.ComponentFragment
import com.example.homemanagement.R
import com.example.homemanagement.ui.TasksFragment
import com.example.homemanagement.data.database.room.Camera
import com.example.homemanagement.Room_fragments.RoomAdapter
import com.example.homemanagement.data.database.AppDatabase
import kotlinx.coroutines.launch

class RoomShowFragment : Fragment() {
    private var position: Int = -1
    private lateinit var listener: RoomShowListener
    private lateinit var roomAdapter: RoomAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.room_show, container, false)
        val args = arguments
        if (args != null && args.containsKey("position")) {
            position = args.getInt("position")
        }
        val nameTextView: TextView = view.findViewById(R.id.rooms_text_view)

        // Retrieve the name from the adapter once it's updated with data
        (requireParentFragment() as? RoomsFragment)?.let { fragment ->
            val roomAdapter = fragment.roomAdapter
            if (position >= 0 && position < roomAdapter.count) {
                val roomName = roomAdapter.getItemName(position)
                nameTextView.text = roomName
            }
        }
        val tabHost: TabHost = view.findViewById(R.id.tab_host_room)
        tabHost.setup()

        // Create a tab for RoomsFragment
        var spec: TabHost.TabSpec = tabHost.newTabSpec("Components")
        spec.setContent(R.id.tab_one_room)
        spec.setIndicator("Components")
        tabHost.addTab(spec)

        // Create a tab for TasksFragment
        spec = tabHost.newTabSpec("Tasks")
        spec.setContent(R.id.tab_two_room)
        spec.setIndicator("Tasks")
        tabHost.addTab(spec)


        childFragmentManager.beginTransaction()
            .replace(R.id.tab_one_room, ComponentFragment())
            .commit()

        // Replace the fragment in the "Tasks" tab content area
        childFragmentManager.beginTransaction()
            .replace(R.id.tab_two_room, TasksFragment())
            .commit()
        return view;
    }
    interface RoomShowListener {

    }
    fun setRoomShowListener(listener: RoomShowListener) {
        this.listener = listener
    }
}
