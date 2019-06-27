package jp.dip.hirotann.appointmentdesk.activity

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import jp.dip.hirotann.appointmentdesk.R


class MainActivity : AppCompatActivity() ,View.OnClickListener {

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        this.auth = FirebaseAuth.getInstance()

        val button: Button = findViewById(R.id.registrationbutton) as Button
        button.setOnClickListener(this)

        var newbutton : Button = findViewById(R.id.newbutton) as Button
        newbutton.setOnClickListener {
            val intent = Intent(application, NewActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {

        var email =  findViewById(R.id.emailText) as EditText
        var password = findViewById(R.id.passwordText) as EditText

        if( email.text.isEmpty() || password.text.isEmpty() ){
            AlertDialog.Builder(this)
                .setTitle("入力エラー")
                .setMessage("メールアドレスまたはパスワードを入力してください。")
                .setPositiveButton("ok"){ dialog, which ->
                }.show()
            return
        }

        this.signIn(email.text.toString(),password.text.toString())
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        this.auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(application, SelectActivity::class.java)
                    startActivity(intent)

                } else {
                    AlertDialog.Builder(this)
                        .setTitle("ログインに失敗")
                        .setMessage("メールアドレスとパスワードを確認してください。")
                        .setPositiveButton("ok"){ dialog, which ->
                        }.show()

                }
            }
        // [END sign_in_with_email]
    }


}
