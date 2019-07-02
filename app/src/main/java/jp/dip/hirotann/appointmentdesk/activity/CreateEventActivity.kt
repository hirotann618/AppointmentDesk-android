package jp.dip.hirotann.appointmentdesk.activity

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_event.*
import android.view.View
import jp.dip.hirotann.appointmentdesk.R
import kotlinx.android.synthetic.main.activity_new.*
import com.google.firebase.firestore.CollectionReference





class CreateEventActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var flg = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        supportActionBar!!.title = "イベント作成"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progress_bar.setVisibility(android.widget.ProgressBar.INVISIBLE)

        createbutton.setOnClickListener {
            enableWaitHandler(1000L,createbutton)
            if(flg) {
                flg = false
                progress_bar.setVisibility(android.widget.ProgressBar.VISIBLE)
                val data = mutableMapOf<String, Any>()

                val user = FirebaseAuth.getInstance().currentUser
                val uid = user?.uid.toString()

                if(eventEditText.text.isEmpty()){
                    AlertDialog.Builder(this)
                        .setTitle("入力エラー")
                        .setMessage("イベント名を入力してください")
                        .setPositiveButton("ok"){ dialog, which ->
                            registrationbutton.isEnabled = true
                        }.show()
                    flg = true
                    return@setOnClickListener
                }

                data.put("name", eventEditText.text.toString())

                db.collection("root").document("event").collection(uid).document( eventEditText.text.toString() )
                    .set(data)
                    .addOnSuccessListener {
                        finish()
                        flg = true
                        progress_bar.setVisibility(android.widget.ProgressBar.INVISIBLE)

                    }
                    .addOnFailureListener { e ->
                        Log.w("", "Error adding document", e)
                        flg = true
                        progress_bar.setVisibility(android.widget.ProgressBar.INVISIBLE)
                    }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    fun enableWaitHandler(stopTime: Long, view: View) {
        view.setEnabled(false)
        Handler().postDelayed(Runnable { view.setEnabled(true) }, stopTime)
    }
}
