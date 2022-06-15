package eu.faircode.email.ui.slide

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import eu.faircode.email.ActivityMain
import eu.faircode.email.R
import eu.faircode.email.data.model.Slide
import eu.faircode.email.extension.transparentStatusBar
import eu.faircode.email.extension.viewInVisible
import eu.faircode.email.extension.viewVisible
import eu.faircode.email.ui.login.LoginActivity
import eu.faircode.email.ui.slide.adapter.SlideIntroPagerAdapter
import kotlinx.android.synthetic.main.activity_slide_intro.*

class SlideIntroActivity : AppCompatActivity() {

    private val pagerAdapter by lazy { SlideIntroPagerAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_intro)

        initView()
        initData()
        initHandles()
    }

    private fun initHandles() {
        tv_next.setOnClickListener {
            view_pager.currentItem ++
        }

        btn_next.setOnClickListener {
            val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            prefs.edit().putBoolean("eula", true).apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        tv_skip.setOnClickListener {
            val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            prefs.edit().putBoolean("eula", true).apply()
            val intent = Intent(this, ActivityMain::class.java)
            startActivity(intent)
        }
    }

    private fun initView() {
        transparentStatusBar()
    }

    private fun initData() {
        view_pager.adapter = pagerAdapter.apply {
            addSlide(
                Slide(
                    R.drawable.ic_shortcut_email,
                    resources.getString(R.string.slide_1_title),
                    resources.getString(R.string.slide_1_desc)
                )
            )

            addSlide(
                Slide(
                    R.drawable.ic_shortcut_email,
                    resources.getString(R.string.slide_2_title),
                    resources.getString(R.string.slide_2_desc)
                )
            )

            addSlide(
                Slide(
                    R.drawable.ic_shortcut_email,
                    resources.getString(R.string.slide_3_title),
                    resources.getString(R.string.slide_3_desc)
                )
            )
        }
        dots_indicator.setViewPager(view_pager)
        view_pager.apply {

            offscreenPageLimit = 3
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> {
                            tv_next.viewVisible()
                            tv_skip.viewVisible()
                            btn_next.viewInVisible()
                        }
                        1 -> {
                            tv_next.viewVisible()
                            tv_skip.viewVisible()
                            btn_next.viewInVisible()
                        }
                        2 -> {
                            tv_next.viewInVisible()
                            tv_skip.viewInVisible()
                            btn_next.viewVisible()
                        }
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
        }
    }
}