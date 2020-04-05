package com.miem.timfedo.miemcam.Presenter

import android.content.Context
import android.os.Handler
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
    fun setLoadingVisibility(isVisible: Boolean)
}

class MainPresenter(private var viewController: MainController, private val session: Session, private val context: Context) {

    private val client = OkHttpClient()
    private var controlPanelFragment: ControlPanelFragment
    private val camerasListFragment: CamerasListFragment
    private val recordFragment: RecordFragment
    private val vmixFragment: VmixControlFragment
    private val cameraServices: CameraServices
    private var isCameraPickerOpened = false
    private var currentFragment = FragentType.CONTROL

    init {
        client.dispatcher.maxRequests = 1
        controlPanelFragment = ControlPanelFragment(client, session) { viewController.setActionBarLabel("MIEMCam") }
        camerasListFragment = CamerasListFragment(client, session, this::setLoadingAnimationVisibility, this::onCameraPicked)
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
                if (isCameraPickerOpened) {
                    viewController.closeCameraPicker()
                }
                viewController.setActionBarLabel("MIEMCam")
                viewController.setArrowVisibility(false)
                viewController.setFragment(recordFragment)
            }
            FragentType.VMIX -> {
                if (isCameraPickerOpened) {
                    viewController.closeCameraPicker()
                }
                viewController.setActionBarLabel("MIEMCam")
                viewController.setArrowVisibility(false)
                viewController.setFragment(vmixFragment)
            }
        }
    }

    fun viewCreated() {
        if (session.token.isEmpty()) {
            Authorizer.shared.showAuth()
        } else {
            startUp()
        }
    }

    fun startUp() {
        viewController.setUpViews()
        viewController.setFragment(controlPanelFragment)
        viewController.setBackgroundFragment(camerasListFragment)
        viewController.setLoadingVisibility(false)
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

    fun clean() {
        session.clean()
        viewController.closeCameraPicker()
        camerasListFragment.clearList()
    }

    private fun onCameraPicked(text: String) {
        viewController.setActionBarLabel(text)
        viewController.setLoadingVisibility(false)
        viewController.closeCameraPicker()
        isCameraPickerOpened = false
        controlPanelFragment.resetView()
    }

    private fun setLoadingAnimationVisibility(isVisible: Boolean) {
        viewController.setLoadingVisibility(isVisible)
    }
}