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
import jp.dip.hirotann.appointmentdesk.ListAdapter
import jp.dip.hirotann.appointmentdesk.R
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity()  {

    var textList: ArrayList<String> = ArrayList()

    var db = FirebaseFirestore.getInstance()

    var eventname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        AppReadList.onCreateApplication(applicationContext)
        AppReadList.instance.readlist.clear()

        progressBar3.visibility = View.INVISIBLE

        val i = getIntent()
        this.eventname = i.getStringExtra("keyname")

        this.textView3.text = this.eventname

        fab.setOnClickListener { view ->
            // FABが押された時に処理

            val intent = Intent(application, LivePreviewActivity::class.java)
            intent.putExtra("keyname", this.eventname )
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        progressBar3.visibility = View.VISIBLE

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
        textList.clear()
        db.collection("root").document("record").collection(uid).whereEqualTo("eventname" , this.eventname)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        textList.add(document.getString("name").toString())
                        AppReadList.instance.readlist.add(document.getString("id").toString())
                    }
                    list_recycler_view.layoutManager = LinearLayoutManager(this)
                    list_recycler_view.adapter = ListAdapter(textList) {}
                    progressBar3.visibility = View.INVISIBLE
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("データの読み込みに失敗")
                        .setMessage("再度通信環境の良い場所で試してみてください。")
                        .setPositiveButton("ok"){ dialog, which ->
                            progressBar3.visibility = View.INVISIBLE
                        }.show()
                }
            }
    }

}
