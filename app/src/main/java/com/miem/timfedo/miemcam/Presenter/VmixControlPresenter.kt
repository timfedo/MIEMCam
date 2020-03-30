package com.miem.timfedo.miemcam.Presenter

import com.miem.timfedo.miemcam.Model.DataServices.VmixServices
import com.miem.timfedo.miemcam.Model.Session
import okhttp3.OkHttpClient

interface VmixControlController {
    fun addSource(name: Int, color: String)
}

class VmixControlPresenter(private val vmixControlController: VmixControlController,
                            client: OkHttpClient,
                            session: Session) {

    private val vmixServices = VmixServices(client, session)
    private val colors = arrayOf(
        "#E15241",
        "#F1A338",
        "#67AD5B",
        "#4994EC"
    )

    fun viewCreated() {
        for (i in 0..3) {
            vmixControlController.addSource(i + 1, colors[i])
        }
    }

    fun overlayBtnClicked(position: Int, source: Int) {
        vmixServices.executeFunction("OverlayInput$position", source.toString()) {}
    }

    fun programBtnClicked(source: Int) {
        vmixServices.executeFunction("CutDirect", source.toString()) {}
    }

    fun previewBtnClicked(source: Int) {
        vmixServices.executeFunction("PreviewInput", source.toString()) {}
    }

    fun cutBtnClicked() {
        vmixServices.executeFunction("Cut", null) {}
    }

    fun fadeBtnClicked() {
        vmixServices.executeFunction("Fade", null) {}
    }

    fun recBtnClicked() {
        vmixServices.executeFunction("StartStopRecording", null) {}
    }

    fun streamBtnClicked() {
        vmixServices.executeFunction("StartStopStreaming", null) {}
    }
}