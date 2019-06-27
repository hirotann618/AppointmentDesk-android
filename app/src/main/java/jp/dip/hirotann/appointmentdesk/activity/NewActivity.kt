package jp.dip.hirotann.appointmentdesk.activity

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import jp.dip.hirotann.appointmentdesk.R
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.activity_new.*

class NewActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        registrationbutton.setOnClickListener{
            registrationbutton.isEnabled = false
            if(new_emailText.text.isEmpty() || new_passwordText.text.isEmpty()){
                AlertDialog.Builder(this)
                    .setTitle("入力エラー")
                    .setMessage("メールアドレスまたはパスワードを入力してください。")
                    .setPositiveButton("ok"){ dialog, which ->
                        registrationbutton.isEnabled = true
                    }.show()

            }else{
                // [START create_user_with_email]
                var email : String = new_emailText.text.toString()
                var password : String = new_passwordText.text.toString()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            AlertDialog.Builder(this)
                                .setTitle("登録成功")
                                .setMessage("登録に成功しました。")
                                .setPositiveButton("ok"){ dialog, which ->
                                    finish()
                                }.show()
                        } else {
                            AlertDialog.Builder(this)
                                .setTitle("登録エラー")
                                .setMessage(task.exception?.message)
                                .setPositiveButton("ok"){ dialog, which ->
                                    registrationbutton.isEnabled = true
                                }.show()
                        }
                    }
                // [END create_user_with_email]
            }
        }
    }

}
