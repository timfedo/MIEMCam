package com.miem.timfedo.miemcam.View

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miem.timfedo.miemcam.Model.Entity.Objects.Camera
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.Presenter.CamerasListController
import com.miem.timfedo.miemcam.Presenter.CamerasListPresenter
import com.miem.timfedo.miemcam.R
import kotlinx.android.synthetic.main.fragment_cameras_list.*
import okhttp3.OkHttpClient

class CamerasListFragment(client: OkHttpClient,
                          session: Session,
                          private val setLoadingAnimationVisibility: (Boolean) -> Unit,
                          private val setToolbarName: (String) -> Unit) : Fragment(), CamerasListController {

    private val camerasListPresenter = CamerasListPresenter(this, client, session)
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cameras_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        camerasListPresenter.onViewCreated()
    }

    override fun setToolbarLabel(text: String) {
        setToolbarName(text)
    }

    override fun setLoadingVisibility(isVisible: Boolean) {
        setLoadingAnimationVisibility(isVisible)
    }

    override fun setUpCamerasListView(camerasAdapter: CamerasAdapter) {
        viewManager = LinearLayoutManager(this.context)
        camerasListView.layoutManager = viewManager
        camerasListView.adapter = camerasAdapter
    }

    override fun updateCamerasListView() {
        activity?.runOnUiThread {
            camerasListView.adapter?.notifyDataSetChanged()
        }
    }

    override fun stopLoadAnimation() {
        loadingProgressList.visibility = View.INVISIBLE
    }

    fun clearList() {
        camerasListPresenter.clearList()
    }
}
