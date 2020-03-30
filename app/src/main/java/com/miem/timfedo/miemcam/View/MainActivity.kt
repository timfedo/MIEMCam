package com.miem.timfedo.miemcam.View

import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.miem.timfedo.miemcam.Presenter.FragentType
import com.miem.timfedo.miemcam.Presenter.MainController
import com.miem.timfedo.miemcam.Presenter.MainPresenter
import com.miem.timfedo.miemcam.R
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.miem.timfedo.miemcam.Model.Session
import android.text.InputType
import android.widget.EditText
import android.content.Intent
import android.net.Uri
import kotlin.random.Random
import android.animation.Animator
import android.app.Activity
import android.view.View
import com.miem.timfedo.miemcam.Model.Authorizer
import java.net.Authenticator

const val TOKEN_REQUEST = 1

class MainActivity : AppCompatActivity(), MainController {

    private lateinit var session: Session
    private lateinit var mainPresenter: MainPresenter

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_control -> {
                mainPresenter.onBottomTabButtonPressed(FragentType.CONTROL)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_record -> {
                mainPresenter.onBottomTabButtonPressed(FragentType.RECORD)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_vmix -> {
                mainPresenter.onBottomTabButtonPressed(FragentType.VMIX)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = Session(this)
        mainPresenter = MainPresenter(this, session)
        setContentView(R.layout.activity_main)

        Authorizer.shared.showAuth = {
            //session.token = ""
            val intent = Intent(this, AuthorizationView::class.java)
            startActivityForResult(intent, TOKEN_REQUEST)
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        mainPresenter.viewCreated()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TOKEN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                //session.token = data?.getStringExtra("token") ?: ""
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.logout -> {
                Authorizer.shared.showAuth()
                return true
            }
            R.id.setBaseUrl -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Title")

                val input = EditText(this)
                input.inputType =
                    InputType.TYPE_CLASS_TEXT
                builder.setView(input)

                builder.setPositiveButton(
                    "OK"
                ) { dialog, which -> Session.basicAdress = input.text.toString() }
                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which -> dialog.cancel() }

                builder.show()
                return true
            }
            R.id.downloadNewApp -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1Sh25PGqkfdqdjOzXT4o49a84tHaRulc0/view?usp=drivesdk"))
                startActivity(browserIntent)
                return true
            }
            R.id.updateLog -> {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Информация о последнем обновлении")
                    .setMessage("1) Добавлено управление vmix\n2) Исправлен экран записи")
                    .setCancelable(false)
                    .setNegativeButton(if (Random.nextBoolean()) "Узнал" else "Согласен") { dialog, _ -> dialog.cancel() }
                val alert = builder.create()
                alert.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setUpViews() {
        setUpActionBar()
    }

    override fun setFragment(f: Fragment) {
        setFragment(f, R.id.placeholder)
    }

    override fun setBackgroundFragment(f: Fragment) {
        setFragment(f, R.id.camerasListPlaceholder)
    }

    override fun openCameraPicker() {
        runOnUiThread {
            placeholder.animate()
                .translationY(placeholder.height.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationEnd(animation)
                        toolbar.elevation = 4f
                    }
                })
            arrowIcon.animate()
                .rotation(180f)
        }
    }

    override fun closeCameraPicker() {
        runOnUiThread {
            placeholder.animate()
                .translationY(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        toolbar.elevation = 0f
                    }
                })
            arrowIcon.animate()
                .rotation(0f)
        }
    }

    override fun setActionBarLabel(text: String) {
        runOnUiThread {
            toolbarTitle.text = text
        }
    }

    override fun setArrowVisibility(isVisible: Boolean) {
        if (isVisible) {
            arrowIcon.visibility = View.VISIBLE
        } else {
            arrowIcon.visibility = View.INVISIBLE
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.elevation = 0F
        toolbarBtn.setOnClickListener {
            mainPresenter.changeCamerasListVisibility()
        }
    }

    private fun setFragment(f: Fragment, placeholder: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        with (fragmentTransaction) {
            detach(f)
            attach(f)
            replace(placeholder, f)
            commit()
        }
    }
}
