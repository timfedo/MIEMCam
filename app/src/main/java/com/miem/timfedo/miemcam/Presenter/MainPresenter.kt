package com.miem.timfedo.miemcam.Presenter

import androidx.fragment.app.Fragment
import com.miem.timfedo.miemcam.Model.DataServices.CameraServices
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.View.CamerasListFragment
import com.miem.timfedo.miemcam.View.ControlPanelFragment
import com.miem.timfedo.miemcam.View.RecordFragment
import okhttp3.OkHttpClient

enum class FragentType {
    CONTROL,
    RECORD
}

interface MainController {
    fun setUpViews()
    fun setFragment(f: Fragment)
    fun setBackgroundFragment(f: Fragment)
    fun openCameraPicker()
    fun closeCameraPicker()
    fun setActionBarLabel(text: String)
}

class MainPresenter(private var viewController: MainController) {

    private val session = Session("9445980805164d5aa0efc6c91500d298")
    private val client = OkHttpClient()
    private val controlPanelFragment: ControlPanelFragment
    private val camerasListFragment: CamerasListFragment
    private val recordFragment: RecordFragment
    private val cameraServices: CameraServices
    private var isCameraPickerOpened = false

    init {
        client.dispatcher.maxRequests = 1
        controlPanelFragment = ControlPanelFragment(client, session)
        camerasListFragment = CamerasListFragment(client, session, this::onCameraPicked)
        recordFragment = RecordFragment(client, session)
        cameraServices = CameraServices(client, session)
    }

    fun onBottomTabButtonPressed(type: FragentType) {
        when (type) {
            FragentType.CONTROL -> {
                viewController.setFragment(controlPanelFragment)
            }
            FragentType.RECORD -> {
                viewController.setFragment(recordFragment)
            }
        }
    }

    fun viewCreated() {
        viewController.setUpViews()
        viewController.setFragment(controlPanelFragment)
        viewController.setBackgroundFragment(camerasListFragment)
    }

    fun changeCamerasListVisibility() {
        if (!isCameraPickerOpened) {
            viewController.openCameraPicker()
        } else {
            viewController.closeCameraPicker()
        }
        isCameraPickerOpened = !isCameraPickerOpened
    }

    private fun onCameraPicked(text: String) {
        changeCamerasListVisibility()
        viewController.setActionBarLabel(text)
        controlPanelFragment.resetView()
    }
}