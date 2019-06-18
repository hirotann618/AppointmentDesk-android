package jp.dip.hirotann.appointmentdesk

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_event.*
import android.widget.ProgressBar
import android.support.v4.os.HandlerCompat.postDelayed
import android.view.View


class CreateEventActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var flg = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        progress_bar.setVisibility(android.widget.ProgressBar.INVISIBLE)

        createbutton.setOnClickListener {
            enableWaitHandler(1000L,createbutton)
            if(flg) {
                flg = false
                progress_bar.setVisibility(android.widget.ProgressBar.VISIBLE)
                val data = mutableMapOf<String, Any>()

                val user = FirebaseAuth.getInstance().currentUser
                val uid = user?.uid.toString()

                data.put("name", eventEditText.text.toString())

                db.collection("root").document("event").collection(uid).document()
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

    fun enableWaitHandler(stopTime: Long, view: View) {
        view.setEnabled(false)
        Handler().postDelayed(Runnable { view.setEnabled(true) }, stopTime)
    }
}
