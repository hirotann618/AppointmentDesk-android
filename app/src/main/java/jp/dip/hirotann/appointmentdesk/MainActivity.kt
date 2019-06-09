package jp.dip.hirotann.appointmentdesk

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.sql.ClientInfoStatus


class MainActivity : AppCompatActivity() ,View.OnClickListener {


    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        this.auth = FirebaseAuth.getInstance()


        val button: Button = findViewById(R.id.loginbutton) as Button
        button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        var email =  findViewById(R.id.emailText) as EditText
        var password = findViewById(R.id.passwordText) as EditText

        this.signIn(email.text.toString(),password.text.toString())
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        this.auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(application, ListActivity::class.java)
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
