package reprise.qrcodescanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity

class MainActivity : CaptureActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scanner = IntentIntegrator(this)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt(getString(R.string.prompt))
                .setOrientationLocked(true)
                .setCameraId(0)
                .setBeepEnabled(true)
                .setBarcodeImageEnabled(false)
                .setCaptureActivity(CaptureActivityPortrait::class.java)
                .initiateScan()
    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
         super.onActivityResult(requestCode, resultCode, data)
         val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

         if (scanResult != null) {
             var scan = scanResult.contents
             if (!scan.startsWith("https://") && !scan.startsWith("http://"))
                 scan = "http://" + scan

             val openLink = Intent(Intent.ACTION_VIEW, Uri.parse(scan))
             try {
                 Toast.makeText(this, getString(R.string.successful) + scan, Toast.LENGTH_LONG).show()
                 startActivity(openLink)
             } catch (e: Exception) {
                 Toast.makeText(this, getString(R.string.wrongIntent), Toast.LENGTH_LONG).show()
                 e.printStackTrace()
             }

         } else {
             Toast.makeText(this, getString(R.string.failure), Toast.LENGTH_LONG).show()
         }
     }
}
