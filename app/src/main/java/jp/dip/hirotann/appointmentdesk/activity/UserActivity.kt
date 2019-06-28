package jp.dip.hirotann.appointmentdesk.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import jp.dip.hirotann.appointmentdesk.R
import android.util.AndroidRuntimeException
import com.google.zxing.WriterException
import android.graphics.Bitmap
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.zxing.BarcodeFormat
import kotlinx.android.synthetic.main.activity_user.*


class UserActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        auth = FirebaseAuth.getInstance()

        val user = this.auth?.currentUser

        //QRコード化する文字列
        val data = user?.uid.toString()

        //QRコード画像の大きさを指定(pixel)
        val size = 500


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
}