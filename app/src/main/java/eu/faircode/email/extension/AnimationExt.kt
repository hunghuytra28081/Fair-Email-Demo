package eu.faircode.email.extension

import android.animation.Animator
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import eu.faircode.email.R

fun View.setAnimationCloud(duration: Long){
    this.animate().translationY(-16F)
            .setDuration(duration)
            .setListener(object : Animator.AnimatorListener{
        override fun onAnimationStart(p0: Animator?) {
        }

        override fun onAnimationEnd(p0: Animator?) {
            this@setAnimationCloud.animate().translationY(0F).duration = 400
        }

        override fun onAnimationCancel(p0: Animator?) {
        }

        override fun onAnimationRepeat(p0: Animator?) {
        }
    })
}
fun View.animationCloud(context: Context, layout: Int) {
    val anim = AnimationUtils.loadAnimation(context, layout)
    this.startAnimation(anim)
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(p0: Animation?) {
            startAnimationAgain(anim)
        }
        override fun onAnimationStart(p0: Animation?) {}

        override fun onAnimationRepeat(p0: Animation?) {}

    })
}

fun View.startAnimationAgain (anim: Animation){
    this.startAnimation(anim)
}