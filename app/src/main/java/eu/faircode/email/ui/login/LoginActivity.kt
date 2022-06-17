package eu.faircode.email.ui.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.faircode.email.R
import eu.faircode.email.extension.customTextViewAgree
import eu.faircode.email.extension.setAnimationCloud
import eu.faircode.email.ui.login.account.ChooseAccountActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
        initHandles()
    }

    private fun initView() {
        img_cloud_login_middle.setAnimationCloud(1500)
        img_cloud_login_top.setAnimationCloud(2000)
    }

    private fun initHandles() {
        customTextViewAgree(this, terms_and_privacy, constraint_terms, constraint_privacy)

        btn_add.setOnClickListener {
            val intent = Intent(this,ChooseAccountActivity::class.java)
            startActivity(intent)
        }

        img_back_terms.setOnClickListener {
            constraint_terms.animate().translationY(3500F).duration = 1000
        }

        img_back_privacy.setOnClickListener {
            constraint_privacy.animate().translationY(3500F).duration = 1000
        }
    }

    override fun onBackPressed() {
        if (constraint_terms.translationY == 0F) {
            constraint_terms.animate().translationY(3500F).duration = 1000
        } else if (constraint_privacy.translationY == 0F) {
            constraint_privacy.animate().translationY(3500F).duration = 1000
        } else {
            finish()
        }
    }
}
