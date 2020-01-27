package com.miem.timfedo.miemcam.Presenter

import com.miem.timfedo.miemcam.Model.DataServices.*
import com.miem.timfedo.miemcam.Model.Session
import okhttp3.OkHttpClient
import java.lang.Math.abs

interface ControlPanelController {
    fun changeIsEnabledFocusAutoBtn(isEnabled: Boolean)
    fun changeIsEnabledFocusManualBtn(isEnabled: Boolean)
}

class ControlPanelPresenter(private val controlPanelController: ControlPanelController,
                            client: OkHttpClient,
                            session: Session) {

    private var presetServices = PresetServices(client, session)
    private var actionServices = ActionServices(client, session)
    private var prevX = 2f
    private var prevY = 2f
    private var currentFocusMode = FocusMode.AUTO

    fun stopped() {
        actionServices.stop {}
        prevX = 2f
        prevY = 2f
    }

    fun presetBtnClicked(id: Int) {
        presetServices.execPreset(id) {}
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
        actionServices.setFocusMode(mode) { currentFocusMode = mode }
        updateFocusModeBtns()
    }

    fun updateFocusModeBtns() {
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