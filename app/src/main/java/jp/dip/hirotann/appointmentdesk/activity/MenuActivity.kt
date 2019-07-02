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

        supportActionBar!!.title = "メニュー"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ownbutton.setOnClickListener {
            val intent = Intent(application, SelectActivity::class.java)
            startActivity(intent)
        }

        userbutton.setOnClickListener{
            val intent = Intent(application, UserActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}
