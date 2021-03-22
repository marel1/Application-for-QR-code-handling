package com.example.qr_code_decode

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Scanner : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        val text = intent.getStringExtra("result")
        val textView=findViewById<TextView>(R.id.result)
        textView.text = text

        val share=findViewById<Button>(R.id.button3)

        share.setOnClickListener(){
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, textView.text as String?)
            }
            startActivity(Intent.createChooser(shareIntent, "My QR code!"))
        }

        if(URLUtil.isValidUrl(textView.text.toString()))
            {
            val searchIntent: Intent = Intent().apply {
                action = Intent.ACTION_WEB_SEARCH
                putExtra(SearchManager.QUERY, textView.text as String?)
            }


            startActivity(searchIntent)
        }
    }

}