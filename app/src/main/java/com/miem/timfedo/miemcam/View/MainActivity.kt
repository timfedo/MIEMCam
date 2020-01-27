package com.miem.timfedo.miemcam.View

import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.miem.timfedo.miemcam.Presenter.FragentType
import com.miem.timfedo.miemcam.Presenter.MainController
import com.miem.timfedo.miemcam.Presenter.MainPresenter
import com.miem.timfedo.miemcam.R
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.miem.timfedo.miemcam.Model.Session
import android.text.InputType
import android.widget.EditText
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.view.View
import androidx.constraintlayout.widget.Placeholder
import kotlin.random.Random
import android.animation.Animator



class MainActivity : AppCompatActivity(), MainController {

    private val mainPresenter = MainPresenter(this)

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
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        setUpActionBar()
        mainPresenter.viewCreated()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
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
                    .setMessage("1) Добвлена кнопка для загрузки новой версии\n2) Добавлен change log\n3) Доработана логика отпраки сообщений о перемещении\n4) На данный момент настрокий камер не работают, это только UI")
                    .setCancelable(false)
                    .setNegativeButton(if (Random.nextBoolean()) "Узнал" else "Согласен") { dialog, _ -> dialog.cancel() }
                val alert = builder.create()
                alert.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setFragment(f: Fragment) {
        setFragment(f, R.id.placeholder)
    }

    override fun setBackgroundFragment(f: Fragment) {
        setFragment(f, R.id.camerasListPlaceholder)
    }

    override fun openCameraPicker() {
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

    override fun closeCameraPicker() {
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

    override fun setToolbarLabel(text: String) {
        runOnUiThread {
            toolbarTitle.text = text
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.elevation = 0F
        toolbarBtn.setOnClickListener {
            mainPresenter.toolbarClicked()
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
