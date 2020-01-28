package com.miem.timfedo.miemcam.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miem.timfedo.miemcam.Model.Entity.CamerasList
import com.miem.timfedo.miemcam.Model.Entity.Objects.Camera
import com.miem.timfedo.miemcam.R

class CamerasAdapter(var camerasList: CamerasList,
                     val onItemClicked: ((Camera) -> Unit)) : RecyclerView.Adapter<CamerasAdapter.CameraHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = CameraHolder(LayoutInflater.from(parent.context).inflate(R.layout.camera, parent, false))

    override fun getItemCount() = camerasList.cameras.size

    override fun onBindViewHolder(holder: CameraHolder, position: Int) {
        val shouldShowRoom =
            (position == 0) || (camerasList.cameras[position].room != camerasList.cameras[position - 1].room)
        holder.bind(camerasList.cameras[position], shouldShowRoom)
    }

    inner class CameraHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val room = itemView.findViewById<TextView>(R.id.room)
        private val name = itemView.findViewById<TextView>(R.id.name)

        fun bind(item: Camera, shouldShowRoom: Boolean) {
            room.text = if (shouldShowRoom) item.room else ""
            name.text = item.name
            itemView.setOnClickListener {
                onItemClicked(item)
            }
        }
    }
}