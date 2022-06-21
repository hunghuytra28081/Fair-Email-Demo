package eu.faircode.email.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.faircode.email.R
import eu.faircode.email.extension.setAnimationCloud
import kotlinx.android.synthetic.main.fragment_welcom_login.*

class WelcomeLoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_welcom_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        img_cloud_welcome_middle.setAnimationCloud(1500)
        img_cloud_welcome_top.setAnimationCloud(2000)
    }
}
