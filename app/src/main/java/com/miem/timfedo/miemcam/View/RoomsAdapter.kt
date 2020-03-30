package com.miem.timfedo.miemcam.View

import android.app.ActionBar
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.miem.timfedo.miemcam.Model.Entity.CamerasList
import com.miem.timfedo.miemcam.Model.Entity.Objects.Camera
import com.miem.timfedo.miemcam.R
import java.util.*
import kotlin.collections.ArrayList
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginStart
import com.google.android.material.button.MaterialButton
import com.miem.timfedo.miemcam.Model.Session
import org.w3c.dom.Text
import java.text.SimpleDateFormat


class RoomsAdapter(var roomsList: ArrayList<String>,
                   val onRequestClicked: ((room: String, email: String, name: String, date: String, start: String, stop: String) -> Unit)) : RecyclerView.Adapter<RoomsAdapter.RoomHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = RoomHolder(parent.context, LayoutInflater.from(parent.context).inflate(R.layout.room, parent, false), onRequestClicked)

    override fun getItemCount() = roomsList.size

    override fun onBindViewHolder(holder: RoomHolder, position: Int) {
        holder.bind(roomsList[position])
    }

    inner class RoomHolder(var context: Context?, itemView: View, onRequestClicked: (room: String, email: String, name: String, date: String, start: String, stop: String) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.name)
        private val start = itemView.findViewById<TextView>(R.id.startTime)
        private val stop = itemView.findViewById<TextView>(R.id.stopTime)
        private val date = itemView.findViewById<TextView>(R.id.dateTime)
        private val create = itemView.findViewById<MaterialButton>(R.id.create)
        private val calendar = Calendar.getInstance()

        fun bind(item: String) {
            name.text = item
            setUpDate()
            setUpTimeLabel(start)
            setUpTimeLabel(stop)
            setUpCreateBtn()
        }

        private fun setUpDate() {
            setDateLabel()
            val dateListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setDateLabel()
            }
            date.setOnClickListener {
                DatePickerDialog(context, dateListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        private fun setUpTimeLabel(label: TextView) {
            val calendar = Calendar.getInstance()
            val timeListener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                calendar.set(Calendar.MINUTE, selectedMinute)
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                setTimeLabel(label, calendar)
            }
            label.setOnClickListener {
                TimePickerDialog(context, timeListener, calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE), true).show()
            }
        }

        private fun setUpCreateBtn() {
            create.setOnClickListener {
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("Создать запись?")

                val cutName = EditText(context)
                cutName.hint = "Название"
                cutName.inputType = InputType.TYPE_CLASS_TEXT
                val email = EditText(context)
                email.hint = "email"
                email.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                layoutParams.topMargin = 16
                layoutParams.marginStart = 30
                layoutParams.marginEnd = 30
                layout.addView(cutName, layoutParams)
                layout.addView(email, layoutParams)
                builder.setView(layout)

                builder.setPositiveButton("OK") { _, _ ->
                    onRequestClicked(
                        name.text.toString(),
                        email.text.toString(),
                        cutName.text.toString(),
                        date.text.toString(),
                        start.text.toString(),
                        stop.text.toString()
                    )
                }
                builder.setNegativeButton("Отменить") { dialog, _ ->
                    dialog.cancel()
                }

                builder.show()
            }
        }

        private fun setDateLabel() {
            val format = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(format, Locale.ENGLISH)
            date.text = sdf.format(calendar.time)
        }

        private fun setTimeLabel(label: TextView, calendar: Calendar) {
            val format = "HH:mm"
            val sdf = SimpleDateFormat(format, Locale.ENGLISH)
            label.text = sdf.format(calendar.time)
        }
    }
}