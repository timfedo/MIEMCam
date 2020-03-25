package com.miem.timfedo.miemcam.View


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.Presenter.ControlPanelController
import com.miem.timfedo.miemcam.Presenter.ControlPanelPresenter
import com.miem.timfedo.miemcam.R
import kotlinx.android.synthetic.main.fragment_control_panel.*
import okhttp3.OkHttpClient
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.DisplayMetrics
import android.widget.FrameLayout
import android.widget.SeekBar
import com.miem.timfedo.miemcam.Model.DataServices.FocusMode
import com.miem.timfedo.miemcam.Model.DataServices.SettingType
import com.miem.timfedo.miemcam.View.SectionView.OnScrollStoppedListener
import org.videolan.libvlc.IVLCVout
import org.videolan.libvlc.IVLCVout.Callback
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.MediaPlayer
import java.net.URI
import android.net.Uri
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*
import org.videolan.libvlc.Media


class ControlPanelFragment(client: OkHttpClient,
                           private val session: Session) : Fragment(), ControlPanelController, Callback {

    private val controlPanelPresenter = ControlPanelPresenter(this, client, session)
    private lateinit var libVLC: LibVLC
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vout: IVLCVout
    private lateinit var mediaSurface: SurfaceView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        libVLC = LibVLC(context, arrayListOf("-vvv"))
        mediaPlayer = MediaPlayer(libVLC)
        vout = mediaPlayer.vlcVout
        mediaSurface = SurfaceView(context)
        return inflater.inflate(R.layout.fragment_control_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        touchPad.addView(mediaSurface)
        touchPad.addView(TouchPadView(context))

        setUpActions()
        controlPanelPresenter.setUpStream()
        resetView()
        //controlPanelPresenter.updateFocusModeBtns()
    }

    override fun onSurfacesCreated(vlcVout: IVLCVout?) {
        vlcVout?.setWindowSize(mediaSurface.width, mediaSurface.height)
    }

    override fun onSurfacesDestroyed(vlcVout: IVLCVout?) {

    }

    override fun changeIsEnabledFocusAutoBtn(isEnabled: Boolean) {
        auto.isEnabled = !isEnabled
        activity?.runOnUiThread {
            if (isEnabled) {
                auto.setTextColor(Color.BLACK)
                auto.strokeColor = ColorStateList.valueOf(Color.BLACK)
            } else {
                auto.setTextColor(Color.GRAY)
                auto.strokeColor = ColorStateList.valueOf(Color.GRAY)
            }
        }
    }

    override fun changeIsEnabledFocusManualBtn(isEnabled: Boolean) {
        manual.isEnabled = !isEnabled
        activity?.runOnUiThread {
            if (isEnabled) {
                manual.setTextColor(Color.BLACK)
                manual.strokeColor = ColorStateList.valueOf(Color.BLACK)
            } else {
                manual.setTextColor(Color.GRAY)
                manual.strokeColor = ColorStateList.valueOf(Color.GRAY)
            }
        }
    }

    override fun setUpVout() {
        vout.addCallback(this)
        with (vout) {
            setVideoView(mediaSurface)
            attachViews()
        }
    }

    override fun startStream(uri: Uri) {
        val media = Media(libVLC, uri)
        with(media) {
            setHWDecoderEnabled(true, false)
            addOption(":clock-jitter=0")
            addOption(":network-caching=500")
            addOption(":clock-synchro=0")
            addOption(":fullscreen")
        }
        with (mediaPlayer) {
            this.media = media
            aspectRatio = "16:9"
            scale = 1f
            position = MediaPlayer.Position.Center.toFloat()
            setEventListener { event ->
//                if (event.type == MediaPlayer.Event.Playing) {
//                    loadingProgressStream.visibility = View.INVISIBLE
//                    mediaSurface.setBackgroundColor(Color.TRANSPARENT)
//                } else {
//                    loadingProgressStream.visibility = View.VISIBLE
//                    mediaSurface.setBackgroundColor(Color.WHITE)
//                }
            }
            play()
        }
    }

    fun resetView() {
        auto.setTextColor(Color.GRAY)
        auto.strokeColor = ColorStateList.valueOf(Color.GRAY)
        manual.setTextColor(Color.GRAY)
        manual.strokeColor = ColorStateList.valueOf(Color.GRAY)
        controlPanelPresenter.setStreamUri(Uri.parse("rtsp://92.53.78.98:${session.port}/${session.pickedCamera}"))
    }

    private fun setUpActions() {
        setUpPresetBtns()
        setUpZoomBtns()
        setUpTouchPad()
        setUpScrollView()
        setUpQuickSettings()
        setUpFocusBtns()
        setUpFocusModeBtns()
    }

    private fun setUpPresetBtns() {
        preset1.setOnClickListener {
            controlPanelPresenter.presetBtnClicked(1)
        }
        preset2.setOnClickListener {
            controlPanelPresenter.presetBtnClicked(2)
        }
        preset3.setOnClickListener {
            controlPanelPresenter.presetBtnClicked(3)
        }
        preset4.setOnClickListener {
            controlPanelPresenter.presetBtnClicked(4)
        }
        preset5.setOnClickListener {
            controlPanelPresenter.presetBtnClicked(5)
        }
        preset6.setOnClickListener {
            controlPanelPresenter.presetBtnClicked(6)
        }
        preset7.setOnClickListener {
            controlPanelPresenter.presetBtnClicked(7)
        }
        preset8.setOnClickListener {
            controlPanelPresenter.presetBtnClicked(8)
        }
        preset9.setOnClickListener {
            controlPanelPresenter.presetBtnClicked(9)
        }
        preset1.setOnLongClickListener {
            controlPanelPresenter.presetBtnLongClicked(1)
            true
        }
        preset2.setOnLongClickListener {
            controlPanelPresenter.presetBtnLongClicked(2)
            true
        }
        preset3.setOnLongClickListener {
            controlPanelPresenter.presetBtnLongClicked(3)
            true
        }
        preset4.setOnLongClickListener {
            controlPanelPresenter.presetBtnLongClicked(4)
            true
        }
        preset5.setOnLongClickListener {
            controlPanelPresenter.presetBtnLongClicked(5)
            true
        }
        preset6.setOnLongClickListener {
            controlPanelPresenter.presetBtnLongClicked(6)
            true
        }
        preset7.setOnLongClickListener {
            controlPanelPresenter.presetBtnLongClicked(7)
            true
        }
        preset8.setOnLongClickListener {
            controlPanelPresenter.presetBtnLongClicked(8)
            true
        }
        preset9.setOnLongClickListener {
            controlPanelPresenter.presetBtnLongClicked(9)
            true
        }
    }

    private fun setUpZoomBtns() {
        zoomIn.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                Log.i("zoom in", "touch")
                controlPanelPresenter.zoomBtnClicked(true)
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                Log.i("zoom in", "release")
                controlPanelPresenter.stopped()
            }
            true
        }
        zoomOut.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                Log.i("zoom out", "touch")
                controlPanelPresenter.zoomBtnClicked(false)
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                Log.i("zoom out", "release")
                controlPanelPresenter.stopped()
            }
            true
        }
    }

    private fun setUpTouchPad() {
        touchPad.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_MOVE || motionEvent.action == MotionEvent.ACTION_DOWN) {
                val x = motionEvent.x / (view.width / 2) - 1
                val y = 1 - motionEvent.y / (view.height / 2)
                //Log.i("touchPad", "x: $x, y: $y")
                controlPanelPresenter.touchPadTouched(x, y)
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                //Log.i("touchPad", "release")
                controlPanelPresenter.stopped()
            }
            true
        }
    }

    private fun setUpScrollView() {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        controlsPlaceholder.layoutParams.width = 2 * width
        tableLayout.layoutParams.width = width
        quickSettingsLayout.layoutParams.width = width

        scrollView.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                scrollView.startScrollerTask()
            }
            false
        }

        scrollView.setOnScrollStoppedListener(object : OnScrollStoppedListener {
            override fun onScrollStopped() {
                Log.i("st", "stopped")
                if (scrollView.scrollX > width / 2) {
                    scrollView.smoothScrollTo(width, 0)
                } else {
                    scrollView.smoothScrollTo(0, 0)
                }
            }
        })
    }

    private fun setUpQuickSettings() {
        brightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) { }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                controlPanelPresenter.setSetting(SettingType.B, brightness.progress.toFloat())
            }
        })

        contrast.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) { }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                controlPanelPresenter.setSetting(SettingType.C, contrast.progress.toFloat())
            }
        })

        saturation.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) { }

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.e("seek", "${saturation.progress}")
                controlPanelPresenter.setSetting(SettingType.S, saturation.progress.toFloat())
            }
        })
    }

    private fun setUpFocusBtns() {
        focusIn.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                Log.i("focus in", "touch")
                controlPanelPresenter.focusBtnClicked(true)
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                Log.i("focus in", "release")
                controlPanelPresenter.focusStopped()
            }
            true
        }
        focusOut.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                Log.i("focus out", "touch")
                controlPanelPresenter.focusBtnClicked(false)
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                Log.i("focus out", "release")
                controlPanelPresenter.focusStopped()
            }
            true
        }
    }

    private fun setUpFocusModeBtns() {
        auto.setOnClickListener {
            controlPanelPresenter.setFocusMode(FocusMode.AUTO)
        }
        manual.setOnClickListener {
            controlPanelPresenter.setFocusMode(FocusMode.MANUAL)
        }
    }
}
