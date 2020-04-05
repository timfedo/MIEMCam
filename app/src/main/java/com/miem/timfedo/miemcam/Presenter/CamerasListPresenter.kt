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
    fun setLoadingVisibility(isVisible: Boolean)
    fun setUpCamerasListView(camerasAdapter: CamerasAdapter)
    fun updateCamerasListView()
    fun stopLoadAnimation()
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

    fun clearList() {
        camerasList.replaceAll(ArrayList())
    }

    private fun onCameraPicked(camera: Camera) {
        camerasListController.setLoadingVisibility(true)
        cameraServices.choseCam(camera.uid, { result ->
            session.pickedRoom = camera.room
            session.pickedCamera = camera.uid
            session.port = result
            this.camerasListController.setToolbarLabel(camera.name)
        }, { camerasListController.setLoadingVisibility(false) })
        cameraServices.releaseCamera()
    }

    private fun onCamerasReceived(cameras: ArrayList<Camera>) {
        cameras.sortBy { it.room }
        this.camerasList.replaceAll(cameras)
        camerasListController.stopLoadAnimation()
        camerasListController.updateCamerasListView()
    }
}