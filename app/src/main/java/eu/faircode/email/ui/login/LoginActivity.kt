package eu.faircode.email.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.faircode.email.ActivityMain
import eu.faircode.email.R
import eu.faircode.email.ui.LaunchApp
import eu.faircode.email.ui.login.account.ChooseAccountActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initData()
        initHandles()
    }

    private fun initData() {
        preferences = getSharedPreferences(LaunchApp.SLIDE_INTRO, Context.MODE_PRIVATE)
        preferences.edit().putBoolean("isFirstRun", false).apply()
    }

    private fun initHandles() {
        btn_add.setOnClickListener {
            val intent = Intent(this,ChooseAccountActivity::class.java)
            startActivity(intent)
        }


    }
}
