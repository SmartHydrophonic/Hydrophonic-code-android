package com.example.smarthydroponic.adapter;

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthydroponic.databinding.ItemNotifBinding
import com.example.smarthydroponic.model.CustomNotif


class NotificationAdapter(private val itemClick: (CustomNotif) -> Unit) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var items: MutableList<CustomNotif> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<CustomNotif>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size

    class NotificationViewHolder(
        private val binding: ItemNotifBinding,
        val itemClick: (CustomNotif) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: CustomNotif) {
            with(item) {
                itemView.setOnClickListener { itemClick(this) }
                binding.run {
                    txtTitle.text = item.title
                    txtDesc.text = Html.fromHtml(item.text)
                }
            }
        }
    }
}