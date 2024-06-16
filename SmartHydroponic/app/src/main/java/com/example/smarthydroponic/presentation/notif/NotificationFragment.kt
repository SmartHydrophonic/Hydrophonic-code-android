package com.example.smarthydroponic.presentation.notif

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthydroponic.adapter.NotificationAdapter
import com.example.smarthydroponic.databinding.FragmentNotificationBinding
import com.example.smarthydroponic.model.CustomNotif
import com.example.smarthydroponic.model.Hidro
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val notificationAdapter: NotificationAdapter by lazy {
        NotificationAdapter {

        }
    }
    private val databaseNotification = FirebaseDatabase.getInstance().getReference("notification")
    private val listNotif = ArrayList<CustomNotif>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvNotif.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter
        }
        // Read from the database
        databaseNotification.addValueEventListener(notificationListener)
    }

    private val notificationListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            listNotif.clear()
            if (dataSnapshot.exists()) {
                binding.emptyState.isVisible = false
                binding.rvNotif.isVisible = true
                Log.d("TAG", "Value is: ${dataSnapshot.value}")
                dataSnapshot.children.forEach { value ->
                    value.getValue(CustomNotif::class.java)?.let { listNotif.add(it) }
                }
                notificationAdapter.setItems(listNotif)
            } else {
                binding.emptyState.isVisible = true
                binding.rvNotif.isVisible = false
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w("TAG", "Failed to read value.", error.toException())
            Toast.makeText(context, "$error was happened", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        databaseNotification.removeEventListener(notificationListener)
        _binding = null
    }
}