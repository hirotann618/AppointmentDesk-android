package jp.dip.hirotann.appointmentdesk

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity()  {

    var textList: ArrayList<String> = ArrayList()

    var db = FirebaseFirestore.getInstance()

    var eventname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

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

    override fun onResume()
    {
        super.onResume()

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
        textList.clear()
        db.collection("root").document("record").collection(uid).whereEqualTo("eventname" , this.eventname)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        textList.add(document.getString("code").toString())

                    }
                    list_recycler_view.layoutManager = LinearLayoutManager(this)
                    list_recycler_view.adapter = ListAdapter(textList){}
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("データの読み込みに失敗")
                        .setMessage("再度通信環境の良い場所で試してみてください。")
                        .setPositiveButton("ok"){ dialog, which ->
                        }.show()
                }
            }
    }
}
