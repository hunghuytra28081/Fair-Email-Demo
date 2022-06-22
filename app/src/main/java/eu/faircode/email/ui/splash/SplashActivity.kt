package eu.faircode.email.ui.splash

import android.animation.Animator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import eu.faircode.email.R
import eu.faircode.email.extension.setAnimationCloud
import eu.faircode.email.extension.transparentStatusBar
import eu.faircode.email.ui.leftmenu.MainMenuLeftActivity
import eu.faircode.email.ui.slide.SlideIntroActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initView()
    }

    private fun initView() {
        transparentStatusBar()
        img_cloud_bottom.setAnimationCloud(1300)
        img_cloud_top.setAnimationCloud(2000)
        img_cloud_middle.animate().translationY(-14F)
                .setDuration(2200)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        initHandles()
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationRepeat(p0: Animator?) {
                    }
                })
    }

    private fun initHandles() {
        Handler(Looper.getMainLooper()).postDelayed({
            /*
                     val intent = Intent(this, SlideIntroActivity::class.java)
         //            val pairs: Array<Pair<View, String>?> = arrayOfNulls(1)
         //            pairs[0] = Pair(img_cloud_bottom, "img_cloud_bottom")
                     val option = ActivityOptions.makeSceneTransitionAnimation(
                             this, img_cloud_bottom, "img_cloud_bottom"
                     )
                     startActivity(intent, option.toBundle())
                     finish()*/
            startActivity(Intent(this, MainMenuLeftActivity::class.java))
        }, 1000)

    }
}
