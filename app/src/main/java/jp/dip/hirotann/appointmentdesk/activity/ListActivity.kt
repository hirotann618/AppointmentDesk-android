package jp.dip.hirotann.appointmentdesk.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
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

        supportActionBar!!.title = this.eventname
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


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


    //メニュー表示の為の関数
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater
        //メニューのリソース選択
        inflater.inflate(R.menu.item_menu, menu)
        return true
    }

    //メニューのアイテムを押下した時の処理の関数
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.getMenuInfo() as AdapterView.AdapterContextMenuInfo
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString()
        when (item.getItemId()) {
            //削除ボタンを押したとき
            R.id.delete -> {
                // ダイアログを作成して表示
                AlertDialog.Builder(this).apply {
                    setMessage("イベントデータをすべて削除してよろしいでしょうか")
                    setPositiveButton("OK", DialogInterface.OnClickListener {  _, _ ->
                        db.collection("root").document("record").collection(uid).whereEqualTo("eventname" , eventname).get().addOnCompleteListener {
                            it.result?.forEach {
                                db.collection("root").document("record").collection(uid).document( it.id ).delete()
                                finish()
                            }
                        }
                    })
                    setNegativeButton("Cancel", null)
                    show()
                }

                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}
