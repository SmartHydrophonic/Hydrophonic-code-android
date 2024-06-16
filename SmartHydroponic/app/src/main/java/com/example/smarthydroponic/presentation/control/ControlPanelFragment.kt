package com.example.smarthydroponic.presentation.control

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthydroponic.adapter.ControlAdapter
import com.example.smarthydroponic.databinding.FragmentControlPanelBinding
import com.example.smarthydroponic.model.CustomNotif
import com.example.smarthydroponic.model.Hidro
import com.example.smarthydroponic.presentation.notif.NotificationFragment
import com.example.smarthydroponic.receiver.SendNotification
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ControlPanelFragment : Fragment() {

    private var _binding: FragmentControlPanelBinding? = null
    private val binding get() = _binding!!
    private val controlAdapter: ControlAdapter by lazy {
        ControlAdapter()
    }
    private val listHidro = mutableListOf<Pair<String, Int>>()
    private val database = FirebaseDatabase.getInstance().getReference("hidro")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlPanelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("TAG", "onViewCreated: $database")
        binding.rvControl.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = controlAdapter
        }
        // Read from the database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listHidro.clear()
                if (dataSnapshot.exists()) {
                    val value = dataSnapshot.getValue(Hidro::class.java)
                    Log.d("TAG", "Value is: $value")
                    listHidro.add(Pair("Kadar Vitamin", value?.tds ?: 0))
                    listHidro.add(Pair("Ketinggian Air Murni", value?.air_murni ?: 0))
                    listHidro.add(Pair("Ketinggian Vitamin", value?.vitamin ?: 0))
                    listHidro.add(Pair("Ketinggian Air Campuran", value?.air_campuran ?: 0))
                    controlAdapter.setItems(listHidro)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
                Toast.makeText(context, "$error was happened", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}