package com.miem.timfedo.miemcam.Presenter

import android.util.Log
import com.miem.timfedo.miemcam.Model.DataServices.CameraServices
import com.miem.timfedo.miemcam.Model.Entity.CamerasList
import com.miem.timfedo.miemcam.Model.Entity.Objects.Camera
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.View.CamerasAdapter
import okhttp3.OkHttpClient

interface CamerasListController {
    fun setToolbarLabel(text: String)
    fun setUpCamerasListView(camerasAdapter: CamerasAdapter)
    fun updateCamerasListView()
}

class CamerasListPresenter(private val camerasListController: CamerasListController,
                           client: OkHttpClient,
                           private val session: Session) {

    private val cameraServices = CameraServices(client, session)
    private var camerasList = CamerasList()
    private val camerasAdapter = CamerasAdapter(camerasList, ::onCameraPicked)

    fun onViewCreated() {
        cameraServices.discovery(::onCamerasReceived)
        camerasListController.setUpCamerasListView(camerasAdapter)
    }

    private fun onCameraPicked(camera: Camera) {
        cameraServices.choseCam(camera.uid) {
            this.camerasListController.setToolbarLabel(camera.name)
            session.pickedRoom = camera.room
        }
    }

    private fun onCamerasReceived(cameras: ArrayList<Camera>) {
        this.camerasList.replaceAll(cameras)
        camerasListController.updateCamerasListView()
    }
}