package com.example.qr_code_decode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonToGenerate=findViewById<Button>(R.id.toGenerator)
        val buttonToScanner=findViewById<Button>(R.id.toScanner)
        buttonToGenerate.setOnClickListener()
        {
            startActivity(Intent(this,Generator::class.java))
        }

        buttonToScanner.setOnClickListener()
        {
            val integrator = IntentIntegrator(this)
            integrator.setPrompt("Scan a QR code!")
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setOrientationLocked(true)
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true)
            integrator.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult =IntentIntegrator.parseActivityResult(requestCode,resultCode,data)

        if(result.contents==null){
            Toast.makeText(baseContext,"Cancelled",Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(baseContext,result.contents,Toast.LENGTH_LONG).show()
            val intent=Intent(this,Scanner::class.java)
            intent.putExtra("result",result.contents)
            startActivity(intent)
        }
    }
}
