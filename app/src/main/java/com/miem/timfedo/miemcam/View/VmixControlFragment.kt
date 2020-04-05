package com.miem.timfedo.miemcam.View

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginLeft
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.Presenter.VmixControlController
import com.miem.timfedo.miemcam.Presenter.VmixControlPresenter

import com.miem.timfedo.miemcam.R
import kotlinx.android.synthetic.main.camera.view.*
import kotlinx.android.synthetic.main.fragment_vmix_control.*
import kotlinx.android.synthetic.main.vmix_source_layout.view.*
import okhttp3.OkHttpClient
import kotlin.math.floor

class VmixControlFragment(client: OkHttpClient,
                          session: Session
) : Fragment(), VmixControlController {

    private val vmixControlPresenter = VmixControlPresenter(this, client, session)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vmix_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmixControlPresenter.viewCreated()
        setUpBtns()
    }

    override fun addSource(name: Int, color: String) {
        val color = Color.parseColor(color)
        val source = LayoutInflater.from(context).inflate(R.layout.vmix_source_layout, null)
        sources.addView(source)
        with (source) {
            sourceName.text = name.toString()
            sourceName.setTextColor(color)
            overlay1.setTextColor(color)
            overlay1.strokeColor = ColorStateList.valueOf(color)
            overlay2.setTextColor(color)
            overlay2.strokeColor = ColorStateList.valueOf(color)
            overlay3.setTextColor(color)
            overlay3.strokeColor = ColorStateList.valueOf(color)
            overlay4.setTextColor(color)
            overlay4.strokeColor = ColorStateList.valueOf(color)
            program.setTextColor(color)
            program.strokeColor = ColorStateList.valueOf(color)
            preview.setTextColor(color)
            preview.strokeColor = ColorStateList.valueOf(color)

            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val gap = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f,
                displayMetrics
            ).toInt()
            layoutParams.width = (width - gap) / 4

            overlay1.setOnClickListener {
                vmixControlPresenter.overlayBtnClicked(1, name)
            }
            overlay2.setOnClickListener {
                vmixControlPresenter.overlayBtnClicked(2, name)
            }
            overlay3.setOnClickListener {
                vmixControlPresenter.overlayBtnClicked(3, name)
            }
            overlay4.setOnClickListener {
                vmixControlPresenter.overlayBtnClicked(4, name)
            }
            program.setOnClickListener {
                vmixControlPresenter.programBtnClicked(name)
            }
            preview.setOnClickListener {
                vmixControlPresenter.previewBtnClicked(name)
            }
        }
    }

    fun setUpBtns() {
        cut.setOnClickListener {
            vmixControlPresenter.cutBtnClicked()
        }
        fade.setOnClickListener {
            vmixControlPresenter.fadeBtnClicked()
        }
        recStart.setOnClickListener {
            vmixControlPresenter.recBtnClicked()
        }
        recStop.setOnClickListener {
            vmixControlPresenter.recBtnClicked()
        }
        streamStart.setOnClickListener {
            vmixControlPresenter.streamBtnClicked()
        }
        streamStop.setOnClickListener {
            vmixControlPresenter.streamBtnClicked()
        }
    }
}
