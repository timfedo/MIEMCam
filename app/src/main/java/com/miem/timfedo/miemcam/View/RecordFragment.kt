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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miem.timfedo.miemcam.Model.Session
import com.miem.timfedo.miemcam.Presenter.RecordController
import com.miem.timfedo.miemcam.Presenter.RecordPresenter
import com.miem.timfedo.miemcam.R
import kotlinx.android.synthetic.main.fragment_cameras_list.*
import kotlinx.android.synthetic.main.fragment_record.*
import kotlinx.android.synthetic.main.fragment_record.loadingProgressList
import okhttp3.OkHttpClient

class RecordFragment(client: OkHttpClient, session: Session) : Fragment(), RecordController {

    private val recordPresenter = RecordPresenter(this, client, session)
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordPresenter.viewCreated()

    }

    override fun setUpRoomsListView(roomsAdapter: RoomsAdapter) {
        viewManager = LinearLayoutManager(this.context)
        roomsListView.layoutManager = viewManager
        roomsListView.adapter = roomsAdapter
    }

    override fun updateRoomsListView() {
        activity?.runOnUiThread {
            roomsListView.adapter?.notifyDataSetChanged()
        }
    }

    override fun stopLoadAnimation() {
        loadingProgressList.visibility = View.INVISIBLE
    }

    override fun startLoadAnimation() {
        loadingProgressList.visibility = View.VISIBLE
    }
}
