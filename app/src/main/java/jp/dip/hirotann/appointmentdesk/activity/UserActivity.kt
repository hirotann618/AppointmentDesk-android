package jp.dip.hirotann.appointmentdesk.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import jp.dip.hirotann.appointmentdesk.R
import android.util.AndroidRuntimeException
import com.google.zxing.WriterException
import android.graphics.Bitmap
import android.util.Log
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.zxing.BarcodeFormat
import jp.dip.hirotann.appointmentdesk.AppReadList
import jp.dip.hirotann.appointmentdesk.barcode.BarcodeScanningProcessor
import kotlinx.android.synthetic.main.activity_user.*
import java.util.*


class UserActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        supportActionBar!!.title = "マイバーコード"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()

        val user = this.auth?.currentUser

        //QRコード化する文字列
        val data = user?.uid.toString()

        //QRコード画像の大きさを指定(pixel)
        val size = 500

        db.collection("root").document("user").collection("info").whereEqualTo("id" , data).get().addOnCompleteListener {
            it.result?.forEach {
                val name = it.getString("name") as String
                this.editNameText.setText(name, TextView.BufferType.NORMAL)

            }
        }


        try {
            val barcodeEncoder = BarcodeEncoder()
            //QRコードをBitmapで作成
            val bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, size, size)

            //作成したQRコードを画面上に配置
            imageBarcode.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            throw AndroidRuntimeException("Barcode Error.", e)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}
