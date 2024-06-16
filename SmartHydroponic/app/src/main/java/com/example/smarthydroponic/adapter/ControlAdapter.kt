package com.example.smarthydroponic.adapter;

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthydroponic.databinding.ItemControlBinding
import com.example.smarthydroponic.model.CustomNotif
import com.example.smarthydroponic.presentation.notif.NotificationFragment
import com.example.smarthydroponic.receiver.SendNotification
import com.google.firebase.database.FirebaseDatabase


class ControlAdapter :
    RecyclerView.Adapter<ControlAdapter.ControlViewHolder>() {

    private var items: MutableList<Pair<String, Int>> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Pair<String, Int>>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControlViewHolder {
        val binding = ItemControlBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ControlViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ControlViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ControlViewHolder(
        private val binding: ItemControlBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val databaseNotification =
            FirebaseDatabase.getInstance().getReference("notification")

        fun bindView(item: Pair<String, Int>) {
            binding.run {
                tvTitle.text = item.first
                if (item.first.contains("Ketinggian Vitamin")) {
                    wavePB.progress =
                        if (item.second > 30) 100 else if (item.second < 0) 0 else item.second * 100 / 30
                    if (item.second <= (30 / 100.0) * 30) {
                        SendNotification().sendNotification(
                            "Ketinggian vitamin Sudah dibawah <b>30%</b>, jangan lupa mengisi vitamin!",
                            itemView.context,
                            1
                        )
                        databaseNotification.child("0").setValue(
                            CustomNotif(
                                "Ketinggian Vitamin",
                                "Ketinggian vitamin Sudah dibawah <b>30%</b>, jangan lupa mengisi vitamin!"
                            )
                        )
                    } else {
                        databaseNotification.child("0").removeValue()
                    }
                }
                if (item.first.contains("Ketinggian Air Murni")) {
                    wavePB.progress =
                        if (item.second > 80) 100 else if (item.second < 0) 0 else item.second * 100 / 80
                    if (item.second <= (30 / 100.0) * 80) {
                        SendNotification().sendNotification(
                            "Air murni Sudah dibawah <b>30%</b>, jangan lupa mengisi air murni!",
                            itemView.context,
                            2
                        )
                        databaseNotification.child("1").setValue(
                            CustomNotif(
                                "Ketinggian Air Murni",
                                "Air murni Sudah dibawah <b>30%</b>, jangan lupa mengisi air murni!"
                            )
                        )
                    } else {
                        databaseNotification.child("1").removeValue()
                    }
                }
                if (item.first.contains("Ketinggian Air Campuran")) {
                    wavePB.progress =
                        if (item.second > 40) 100 else if (item.second < 0) 0 else item.second * 100 / 40
                }
                if (item.first.contains("Kadar Vitamin")) {
                    wavePB.visibility = View.INVISIBLE
                    tvPPMdesc.text = item.second.toString()
                    linPPM.isVisible = true
                }
                wavePB.setAnimationSpeed(100)
                wavePB.startAnimation()
            }
        }
    }
}