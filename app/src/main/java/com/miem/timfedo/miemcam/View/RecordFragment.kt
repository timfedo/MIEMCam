package com.miem.timfedo.miemcam.View

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.Presenter.RecordController
import com.miem.timfedo.miemcam.Presenter.RecordPresenter
import com.miem.timfedo.miemcam.Presenter.RecordState
import com.miem.timfedo.miemcam.Presenter.SoundSource
import com.miem.timfedo.miemcam.R
import kotlinx.android.synthetic.main.fragment_record.*
import okhttp3.OkHttpClient

class RecordFragment(client: OkHttpClient, session: Session) : Fragment(), RecordController {

    private val recordPresenter = RecordPresenter(this, client, session)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordPresenter.viewCreated()
        startRecordButton.setOnClickListener {
            recordPresenter.recordBtnClicked(RecordState.RECORDING)
        }
        stopRecordButton.setOnClickListener {
            recordPresenter.recordBtnClicked(RecordState.NOTRECORDING)
        }
        sourceCameraButton.setOnClickListener {
            recordPresenter.soundSourceBtnClicked(SoundSource.CAMERA)
        }
        sourceCoderButton.setOnClickListener {
            recordPresenter.soundSourceBtnClicked(SoundSource.CODER)
        }
    }

    override fun setTextToRecordBtn(text: String) {
        startRecordButton.text = text
    }

    override fun changeIsEnabledCameraBtn(isEnabled: Boolean) {
        sourceCameraButton.isEnabled = !isEnabled
        if (isEnabled) {
            sourceCameraButton.setTextColor(Color.BLACK)
            sourceCameraButton.strokeColor = ColorStateList.valueOf(Color.BLACK)
        } else {
            sourceCameraButton.setTextColor(Color.GRAY)
            sourceCameraButton.strokeColor = ColorStateList.valueOf(Color.GRAY)
        }
    }

    override fun changeIsEnabledCoderBtn(isEnabled: Boolean) {
        sourceCoderButton.isEnabled = !isEnabled
        if (isEnabled) {
            sourceCoderButton.setTextColor(Color.BLACK)
            sourceCoderButton.strokeColor = ColorStateList.valueOf(Color.BLACK)
        } else {
            sourceCoderButton.setTextColor(Color.GRAY)
            sourceCoderButton.strokeColor = ColorStateList.valueOf(Color.GRAY)
        }
    }

    override fun changeIsEnabledStartBtn(isEnabled: Boolean) {
        startRecordButton.isEnabled = isEnabled
        if (isEnabled) {
            startRecordButton.strokeColor = ColorStateList.valueOf(Color.BLACK)
        } else {
            startRecordButton.strokeColor = ColorStateList.valueOf(Color.GRAY)
        }
    }

    override fun changeIsEnabledStopBtn(isEnabled: Boolean) {
        stopRecordButton.isEnabled = isEnabled
        if (isEnabled) {
            stopRecordButton.strokeColor = ColorStateList.valueOf(Color.BLACK)
        } else {
            stopRecordButton.strokeColor = ColorStateList.valueOf(Color.GRAY)
        }
    }
}
