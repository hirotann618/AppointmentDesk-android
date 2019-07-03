package jp.dip.hirotann.appointmentdesk.barcode

import android.content.Context
import android.graphics.Bitmap
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import jp.dip.hirotann.appointmentdesk.common.FrameMetadata
import jp.dip.hirotann.appointmentdesk.common.GraphicOverlay
import com.google.firebase.samples.apps.mlkit.kotlin.VisionProcessorBase
import java.io.IOException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import jp.dip.hirotann.appointmentdesk.AppReadList
import jp.dip.hirotann.appointmentdesk.common.CameraImageGraphic
import java.util.*
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener




/** Barcode Detector Demo.  */
class BarcodeScanningProcessor(contextin : Context) : VisionProcessorBase<List<FirebaseVisionBarcode>>() {
    val context = contextin

    var db = FirebaseFirestore.getInstance()

    // Note that if you know which format of barcode your app is dealing with, detection will be
    // faster to specify the supported barcode formats one by one, e.g.
    // FirebaseVisionBarcodeDetectorOptions.Builder()
    //     .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
    //     .build()
    private val detector: FirebaseVisionBarcodeDetector by lazy {
        FirebaseVision.getInstance().visionBarcodeDetector
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Barcode Detector: $e")
        }
    }

    override fun detectInImage(image: FirebaseVisionImage): Task<List<FirebaseVisionBarcode>> {
        return detector.detectInImage(image)
    }

    override fun onSuccess(
        originalCameraImage: Bitmap?,
        barcodes: List<FirebaseVisionBarcode>,
        frameMetadata: FrameMetadata,
        graphicOverlay: GraphicOverlay
    ) {
        graphicOverlay.clear()

        originalCameraImage?.let {
            val imageGraphic = CameraImageGraphic(graphicOverlay, it)
            graphicOverlay.add(imageGraphic)
        }

        barcodes.forEach {
            val barcodeGraphic = BarcodeGraphic(graphicOverlay, it)

           if(! AppReadList.instance.readlist.contains(it.rawValue.toString()) ){
               val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
               val vibrationEffect = VibrationEffect.createOneShot(300, DEFAULT_AMPLITUDE)
               vibrator.vibrate(vibrationEffect)
               val data = mutableMapOf<String,Any>()

               val user = FirebaseAuth.getInstance().currentUser
               val uid = user?.uid.toString()
               val id = it.rawValue.toString()

               db.collection("root").document("user").collection("info").whereEqualTo("id" , id).get().addOnCompleteListener {
                   it.result?.forEach {

                           val name  = it.getString("name") as String

                           data.put("id" , id )
                           data.put("name" , name )
                           data.put("date" , Date() )
                           data.put("eventname" , AppReadList.instance.eventname )

                           db.collection("root").document("record").collection(uid).document()
                               .set(data)
                               .addOnSuccessListener { documentReference ->
                               }
                               .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
                   }
               }

           }
            AppReadList.instance.readlist.add(it.rawValue.toString())
            graphicOverlay.add(barcodeGraphic)

        }
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Barcode detection failed $e")
    }

    companion object {

        private const val TAG = "BarcodeScanProc"
    }
}