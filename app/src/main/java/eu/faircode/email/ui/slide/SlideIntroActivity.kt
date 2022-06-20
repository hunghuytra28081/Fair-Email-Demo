package eu.faircode.email.ui.slide

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import eu.faircode.email.R
import android.util.Pair
import eu.faircode.email.data.model.Slide
import eu.faircode.email.extension.setAnimationCloud
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
            view_pager.currentItem++
        }

        btn_next.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val pairs: Array<Pair<View, String>?> = arrayOfNulls(1)
            pairs[0] = Pair(constraint_cloud_slide_bottom, "img_cloud_bottom")

            val option = ActivityOptions.makeSceneTransitionAnimation(
                    this, *pairs
            )
            startActivity(intent, option.toBundle())
        }

        tv_skip.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val option = ActivityOptions.makeSceneTransitionAnimation(
                    this, constraint_cloud_slide_bottom, "img_cloud_bottom"
            )
            startActivity(intent, option.toBundle())
        }
    }

    private fun initView() {
        transparentStatusBar()
        img_cloud_slide_middle.setAnimationCloud(1500)
        img_cloud_slide_top.setAnimationCloud(2000)
    }

    override fun onStop() {
        super.onStop()
        img_cloud_slide_middle.animate().translationY(350f).duration = 500
        img_cloud_slide_top.animate().translationY(350F).duration = 500
    }

    override fun onRestart() {
        super.onRestart()
        img_cloud_slide_middle.setAnimationCloud(1500)
        img_cloud_slide_top.setAnimationCloud(2000)
    }

    private fun initData() {
        view_pager.adapter = pagerAdapter.apply {
            addSlide(
                    Slide(
                            R.drawable.img_slide_1,
                            resources.getString(R.string.slide_1_title),
                            resources.getString(R.string.slide_1_desc)
                    )
            )

            addSlide(
                    Slide(
                            R.drawable.img_slide_2,
                            resources.getString(R.string.slide_2_title),
                            resources.getString(R.string.slide_2_desc)
                    )
            )

            addSlide(
                    Slide(
                            R.drawable.img_slide_3,
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

    override fun onBackPressed() {
        finish()
    }
}
