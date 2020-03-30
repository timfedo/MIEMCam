package com.miem.timfedo.miemcam.Presenter

import com.miem.timfedo.miemcam.Model.DataServices.RecordServices
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.View.RoomsAdapter
import okhttp3.OkHttpClient

interface RecordController {
    fun setUpRoomsListView(roomsAdapter: RoomsAdapter)
    fun updateRoomsListView()
    fun stopLoadAnimation()
    fun startLoadAnimation()
}

class RecordPresenter(private val recordController: RecordController,
                      client: OkHttpClient,
                      private val session: Session) {

    private var recordServices = RecordServices(client, session)
    private var roomsList = arrayListOf<String>()
    private var roomsAdapter = RoomsAdapter(roomsList, ::onRequestClicked)

    fun viewCreated() {
        recordController.startLoadAnimation()
        recordServices.getRooms(::onRoomsReceived)
        recordController.setUpRoomsListView(roomsAdapter)
    }

    fun onRequestClicked(room: String, email: String, name: String, date: String, start: String, stop: String) {
        recordServices.requestRecord(room, email, name, date, start, stop) {}
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