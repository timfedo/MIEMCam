package com.miem.timfedo.miemcam.Presenter

import com.miem.timfedo.miemcam.Model.DataServices.RecordServices
import com.miem.timfedo.miemcam.Model.Session
import okhttp3.OkHttpClient

enum class RecordState {
    RECORDING,
    NOTRECORDING
}

enum class SoundSource {
    CAMERA,
    CODER
}

interface RecordController {
    fun setTextToRecordBtn(text: String)
    fun changeIsEnabledCameraBtn(isEnabled: Boolean)
    fun changeIsEnabledCoderBtn(isEnabled: Boolean)
    fun changeIsEnabledStartBtn(isEnabled: Boolean)
    fun changeIsEnabledStopBtn(isEnabled: Boolean)
}

class RecordPresenter(private val recordController: RecordController,
                      client: OkHttpClient,
                      private val session: Session) {

    private var stateRecord: RecordState
    private var soundSource: SoundSource
    private var recordServices = RecordServices(client, session)

    init {
        stateRecord = RecordState.NOTRECORDING
        soundSource = SoundSource.CAMERA
    }

    fun viewCreated() {
        setRecordBtnState(stateRecord)
        setSourceBtnState(soundSource)
    }

    fun recordBtnClicked(state: RecordState) {
        setRecordBtnState(state)
        when (state) {
            RecordState.RECORDING -> recordServices.startRecord(session.pickedRoom, {})
            RecordState.NOTRECORDING -> recordServices.stopRecord(session.pickedRoom, {})
        }
    }

    fun soundSourceBtnClicked(source: SoundSource) {
        setSourceBtnState(source)
        recordServices.setSoundSource(session.pickedRoom, source, {})
    }

    private fun setRecordBtnState(state: RecordState) {
        when (state) {
            RecordState.RECORDING -> {
                recordController.changeIsEnabledStopBtn(true)
                recordController.changeIsEnabledStartBtn(false)
            }
            RecordState.NOTRECORDING -> {
                recordController.changeIsEnabledStopBtn(false)
                recordController.changeIsEnabledStartBtn(true)
            }
        }
        stateRecord = state
    }

    private fun setSourceBtnState(source: SoundSource) {
        when (source) {
            SoundSource.CAMERA -> {
                recordController.changeIsEnabledCameraBtn(true)
                recordController.changeIsEnabledCoderBtn(false)
            }
            SoundSource.CODER -> {
                recordController.changeIsEnabledCameraBtn(false)
                recordController.changeIsEnabledCoderBtn(true)
            }
        }
        soundSource = source
    }
}