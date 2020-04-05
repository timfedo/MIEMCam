package com.miem.timfedo.miemcam.Presenter

import com.miem.timfedo.miemcam.Model.DataServices.RecordServices
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.Model.Toaster
import com.miem.timfedo.miemcam.View.RoomsAdapter
import okhttp3.OkHttpClient

interface RecordController {
    fun setUpRoomsListView(roomsAdapter: RoomsAdapter)
    fun updateRoomsListView()
    fun stopLoadAnimation()
}

class RecordPresenter(private val recordController: RecordController,
                      client: OkHttpClient,
                      session: Session) {

    private var recordServices = RecordServices(client, session)
    private var roomsList = arrayListOf<String>()
    private var roomsAdapter = RoomsAdapter(roomsList, session.email, ::onRequestClicked)

    fun viewCreated() {
        recordServices.getRooms(::onRoomsReceived)
        recordController.setUpRoomsListView(roomsAdapter)
        if (roomsList.isNotEmpty()) {
            recordController.stopLoadAnimation()
        }
    }

    fun onRequestClicked(room: String, email: String, name: String, date: String, start: String, stop: String) {
        recordServices.requestRecord(room, email, name, date, start, stop) {
            Toaster.shared.showToast("Когда запись будет готова, на указанную почту придет письмо")
        }
        viewCreated()
    }

    private fun onRoomsReceived(rooms: ArrayList<String>) {
        roomsList = rooms
        roomsList.sort()
        roomsAdapter.roomsList = roomsList
        recordController.stopLoadAnimation()
        recordController.updateRoomsListView()
    }
}