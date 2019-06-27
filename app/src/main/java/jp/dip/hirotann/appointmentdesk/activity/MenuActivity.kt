package jp.dip.hirotann.appointmentdesk.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import jp.dip.hirotann.appointmentdesk.R
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        ownbutton.setOnClickListener {
            val intent = Intent(application, SelectActivity::class.java)
            startActivity(intent)
        }
    }
}
