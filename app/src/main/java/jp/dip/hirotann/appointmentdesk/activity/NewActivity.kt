package jp.dip.hirotann.appointmentdesk.activity

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jp.dip.hirotann.appointmentdesk.R
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.activity_new.*

class NewActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        progressBar2.visibility = View.INVISIBLE

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        registrationbutton.setOnClickListener{
            registrationbutton.isEnabled = false
            progressBar2.visibility = View.VISIBLE
            if(new_emailText.text.isEmpty() || new_passwordText.text.isEmpty() || viewName.text.isEmpty()){
                AlertDialog.Builder(this)
                    .setTitle("入力エラー")
                    .setMessage("メールアドレスまたはパスワードまたは表示名を入力してください。")
                    .setPositiveButton("ok"){ dialog, which ->
                        registrationbutton.isEnabled = true
                        progressBar2.visibility = View.INVISIBLE
                    }.show()

            }else{
                // [START create_user_with_email]
                var email : String = new_emailText.text.toString()
                var password : String = new_passwordText.text.toString()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                            var db = FirebaseFirestore.getInstance()

                            val data = mutableMapOf<String,Any>()
                            val uid = task.result?.user?.uid.toString()

                            data.put("id" , uid  )
                            data.put("name" , viewName.text.toString() )

                            db.collection("root").document("user").collection("info").document( uid )
                                .set(data)
                                .addOnSuccessListener { documentReference ->
                                    progressBar2.visibility = View.INVISIBLE
                                    AlertDialog.Builder(this)
                                        .setTitle("登録成功")
                                        .setMessage("登録に成功しました。")
                                        .setPositiveButton("ok"){ dialog, which ->
                                            finish()
                                        }.show()
                                }
                                .addOnFailureListener { e -> Log.w("", "Error adding document", e) }
                        } else {
                            AlertDialog.Builder(this)
                                .setTitle("登録エラー")
                                .setMessage(task.exception?.message)
                                .setPositiveButton("ok"){ dialog, which ->
                                    registrationbutton.isEnabled = true
                                    progressBar2.visibility = View.INVISIBLE
                                }.show()
                        }
                    }
                // [END create_user_with_email]
            }
        }
    }

}
