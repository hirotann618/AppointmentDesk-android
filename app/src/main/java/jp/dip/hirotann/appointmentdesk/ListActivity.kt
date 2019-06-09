package jp.dip.hirotann.appointmentdesk

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)




        fab.setOnClickListener { view ->
            // FABが押された時に処理
            val intent = Intent(application, LivePreviewActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume()
    {
        super.onResume()

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
        textList.clear()
        db.collection("root").document("record").collection(uid)
            .orderBy("date" , Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        textList.add(document.getString("code").toString())

                    }
                    list_recycler_view.layoutManager = LinearLayoutManager(this)
                    list_recycler_view.adapter = ListAdapter(textList)
                } else {
                }
            }
    }
}
