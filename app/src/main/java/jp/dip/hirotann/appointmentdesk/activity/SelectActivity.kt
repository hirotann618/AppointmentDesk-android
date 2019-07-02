package jp.dip.hirotann.appointmentdesk.activity

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jp.dip.hirotann.appointmentdesk.AppReadList
import kotlinx.android.synthetic.main.activity_select.*
import jp.dip.hirotann.appointmentdesk.R
import jp.dip.hirotann.appointmentdesk.SelectAdapter
import kotlin.collections.ArrayList
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper




class SelectActivity : AppCompatActivity() {

    var textList: ArrayList<String> = ArrayList()

    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        supportActionBar!!.title = "イベント選択"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressBar5.visibility = View.INVISIBLE

        add_event_button.setOnClickListener { view ->
            val intent = Intent(application, CreateEventActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
        textList.clear()
        progressBar5.visibility = View.VISIBLE
        db.collection("root").document("event").collection(uid)
            .orderBy("name" )
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        textList.add(document.getString("name").toString())
                    }
                    select_recycler_view.layoutManager = LinearLayoutManager(this)
                    select_recycler_view.adapter = SelectAdapter(textList) {
                        val intent =
                            Intent(application, ListActivity::class.java)
                        intent.putExtra("keyname", it)
                        startActivity(intent)
                    }

                    progressBar5.visibility = View.INVISIBLE
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("データの読み込みに失敗")
                        .setMessage("再度通信環境の良い場所で試してみてください。")
                        .setPositiveButton("ok"){ dialog, which ->
                            progressBar5.visibility = View.INVISIBLE
                        }.show()
                }
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }


}
