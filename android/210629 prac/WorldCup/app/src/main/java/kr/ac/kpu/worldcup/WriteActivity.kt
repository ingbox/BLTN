package kr.ac.kpu.worldcup

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_write.*

class WriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)


        btn_goback.setOnClickListener {
            val intent = Intent()
            intent.putExtra("title", et_write.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}