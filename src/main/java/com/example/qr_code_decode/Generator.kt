package com.example.qr_code_decode

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class Generator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generator)

        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        //declaration and assignment of variables
        val imageView = findViewById<ImageView>(R.id.image)
        val button = findViewById<Button>(R.id.button)
        val sendButton=findViewById<Button>(R.id.button2)
        val textView = findViewById<EditText>(R.id.inputText)
        val spinner: Spinner=findViewById(R.id.spinner)

        ArrayAdapter.createFromResource(
            this,
            R.array.Correction_level,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        var uri:Uri?=null
        //event performed when the button is clicked
        button.setOnClickListener(View.OnClickListener {
            //if the text is not empty it generates qr code and binds to the image
            if (!TextUtils.isEmpty(textView.text)) {
                val level = spinner.selectedItem.toString()
                val text = textView.text.toString()
                val qrCode = createBitmap(text,ErrorCorrectionLevel.valueOf(level))
                uri = getBitmapFromView(qrCode)
                imageView.setImageURI(uri)

            }
        })

        sendButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, "Share Image"))
        }
    }
    //function to generate QR code
    private fun createBitmap(text: String,level: ErrorCorrectionLevel): Bitmap? {

        var result: BitMatrix?

        val hintsMap: MutableMap<EncodeHintType, Any> = HashMap()
        hintsMap[EncodeHintType.CHARACTER_SET] = "utf-8"
        hintsMap[EncodeHintType.ERROR_CORRECTION] = level
        //encoding using Zxing library and assigning to result
        result = try {
            MultiFormatWriter().encode(
                text, BarcodeFormat.QR_CODE, 300,
                300, hintsMap
            )
        } catch (e: WriterException) {
            e.printStackTrace()
            return null
        }

        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)

        //fill the pixel array with representation of the result (black or white)
        for (x in 0 until height) {
            val offset = x * width
            for (k in 0 until width) {
                pixels[offset + k] = if (result[k, x]
                ) Color.BLACK else Color.WHITE
            }
        }
        //creating and return QR code image
        val myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        myBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return myBitmap
    }

    fun getBitmapFromView(bmp: Bitmap?): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(this.externalCacheDir, System.currentTimeMillis().toString() + ".jpg")
            val out = FileOutputStream(file)
            bmp?.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.close()
            bmpUri = Uri.fromFile(file)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }
}
