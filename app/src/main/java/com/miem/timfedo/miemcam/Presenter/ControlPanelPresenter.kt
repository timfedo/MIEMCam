package com.miem.timfedo.miemcam.Presenter

import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import com.miem.timfedo.miemcam.Model.DataServices.*
import com.miem.timfedo.miemcam.Model.Session
import okhttp3.OkHttpClient
import java.lang.Math.abs
import java.util.*

interface ControlPanelController {
    fun changeIsEnabledFocusAutoBtn(isEnabled: Boolean)
    fun changeIsEnabledFocusManualBtn(isEnabled: Boolean)
    fun startStream(uri: Uri)
    fun setScaleMode(mode: ScaleMode)
    fun releaseCamera()
}

enum class ScaleMode {
    FIT,
    FILL
}

class ControlPanelPresenter(private val controlPanelController: ControlPanelController,
                            client: OkHttpClient,
                            private val session: Session) {

    private var presetServices = PresetServices(client, session)
    private var actionServices = ActionServices(client, session)
    private var cameraServices = CameraServices(client, session)
    private var prevX = 2f
    private var prevY = 2f
    private var currentFocusMode = FocusMode.AUTO
    private var scaleMode = ScaleMode.FILL

    fun stopped() {
        actionServices.stop {}
        prevX = 2f
        prevY = 2f
    }

    fun presetBtnClicked(id: Int) {
        presetServices.execPreset(id) {}
    }

    fun presetBtnLongClicked(id: Int) {
        presetServices.setPreset(id) {}
    }

    fun zoomBtnClicked(isIncreasing: Boolean) {
        val zoom = if (isIncreasing) {
            0.5f
        } else {
            -0.5f
        }
        actionServices.moveCam(arrayListOf(0f, 0f, zoom)) {}
    }

    fun touchPadTouched(x: Float, y: Float) {
        if (abs(x  - prevX) > 0.05 || abs(y  - prevY) > 0.05 || prevX == 2f) {
            prevX = x
            prevY = y
            actionServices.moveCam(arrayListOf(x, y, 0f)) {}
        }
    }

    fun setSetting(type: SettingType, value: Float) {
        actionServices.setSetting(type, value) {}
    }

    fun focusBtnClicked(isIncreasing: Boolean) {
        val focus = if (isIncreasing) {
            0.5f
        } else {
            -0.5f
        }
        actionServices.setFocusContinious(focus) {}
    }

    fun focusStopped() {
        actionServices.stopFocus {}
    }

    fun setFocusMode(mode: FocusMode) {
        actionServices.setFocusMode(mode) {
            currentFocusMode = mode
            updateFocusModeBtns()
        }
    }

    fun startStream() {
        if (session.port.isEmpty() || session.pickedCamera.isEmpty()) { return }
        controlPanelController.startStream(Uri.parse("rtsp://92.53.78.98:${session.port}/${session.pickedCamera}"))
    }

    fun viewDestroyed() {
        session.pickedCamera = ""
        session.port = ""
        session.pickedRoom = ""
        controlPanelController.releaseCamera()
        cameraServices.releaseCamera()
    }

    fun touchPadDoubleClicked() {
        scaleMode = when (scaleMode) {
            ScaleMode.FIT -> ScaleMode.FILL
            ScaleMode.FILL -> ScaleMode.FIT
        }
        controlPanelController.setScaleMode(scaleMode)
    }

    private fun updateFocusModeBtns() {
        when (currentFocusMode) {
            FocusMode.AUTO -> {
                controlPanelController.changeIsEnabledFocusAutoBtn(true)
                controlPanelController.changeIsEnabledFocusManualBtn(false)
            }
            FocusMode.MANUAL -> {
                controlPanelController.changeIsEnabledFocusAutoBtn(false)
                controlPanelController.changeIsEnabledFocusManualBtn(true)
            }
        }
    }
}