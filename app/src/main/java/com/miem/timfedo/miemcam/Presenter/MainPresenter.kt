package com.miem.timfedo.miemcam.Presenter

import androidx.fragment.app.Fragment
import com.miem.timfedo.miemcam.Model.Authorizer
import com.miem.timfedo.miemcam.Model.DataServices.CameraServices
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.View.CamerasListFragment
import com.miem.timfedo.miemcam.View.ControlPanelFragment
import com.miem.timfedo.miemcam.View.RecordFragment
import com.miem.timfedo.miemcam.View.VmixControlFragment
import okhttp3.OkHttpClient

enum class FragentType {
    CONTROL,
    RECORD,
    VMIX
}

interface MainController {
    fun setUpViews()
    fun setFragment(f: Fragment)
    fun setBackgroundFragment(f: Fragment)
    fun openCameraPicker()
    fun closeCameraPicker()
    fun setActionBarLabel(text: String)
    fun setArrowVisibility(isVisible: Boolean)
}

class MainPresenter(private var viewController: MainController, private val session: Session) {

    private val client = OkHttpClient()
    private val controlPanelFragment: ControlPanelFragment
    private val camerasListFragment: CamerasListFragment
    private val recordFragment: RecordFragment
    private val vmixFragment: VmixControlFragment
    private val cameraServices: CameraServices
    private var isCameraPickerOpened = false
    private var currentFragment = FragentType.CONTROL

    init {
        client.dispatcher.maxRequests = 1
        controlPanelFragment = ControlPanelFragment(client, session)
        camerasListFragment = CamerasListFragment(client, session, this::onCameraPicked)
        recordFragment = RecordFragment(client, session)
        vmixFragment = VmixControlFragment(client, session)
        cameraServices = CameraServices(client, session)
    }

    fun onBottomTabButtonPressed(type: FragentType) {
        currentFragment = type
        when (type) {
            FragentType.CONTROL -> {
                viewController.setActionBarLabel("MIEMCam")
                viewController.setArrowVisibility(true)
                viewController.setFragment(controlPanelFragment)
            }
            FragentType.RECORD -> {
                viewController.setActionBarLabel("MIEMCam")
                viewController.setArrowVisibility(false)
                viewController.setFragment(recordFragment)
            }
            FragentType.VMIX -> {
                viewController.setActionBarLabel("MIEMCam")
                viewController.setArrowVisibility(false)
                viewController.setFragment(vmixFragment)
            }
        }
    }

    fun viewCreated() {
        if (session.token == "") {
            Authorizer.shared.showAuth()
        }
        viewController.setUpViews()
        viewController.setFragment(controlPanelFragment)
        viewController.setBackgroundFragment(camerasListFragment)
    }

    fun changeCamerasListVisibility() {
        if (currentFragment != FragentType.CONTROL) { return }
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