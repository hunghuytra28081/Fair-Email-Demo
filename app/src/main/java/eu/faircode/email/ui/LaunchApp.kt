package eu.faircode.email.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eu.faircode.email.extension.transparentStatusBar
import eu.faircode.email.ui.slide.SlideIntroActivity
import eu.faircode.email.ActivityMain
import eu.faircode.email.ui.login.LoginActivity
import eu.faircode.email.ui.main.HomeMainActivity

class LaunchApp : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        transparentStatusBar()
        preferences = getSharedPreferences(SLIDE_INTRO, Context.MODE_PRIVATE)

        val isFirstRun: Boolean = preferences.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            val intent = Intent(this, SlideIntroActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object{
        const val SLIDE_INTRO = "slide_intro"
    }
}