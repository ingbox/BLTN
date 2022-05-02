package kr.ac.kpu.worldcup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainIntent = intent
        val address1 = mainIntent.getStringExtra("one")
        val address2 = mainIntent.getStringExtra("two")
        val earth = intent.getParcelableExtra<Space>("earth")



        text1.text = address1
        text2.text = address2
        if (earth != null) {
            text3.text = earth.id
            text4.text = earth.num.toString()
        } else {

            Log.e("MainActivity", "가져온 데이터가 없습니다~!!")
        }

        btn_write.setOnClickListener {
            val intent = Intent(this, WriteActivity:: class.java)
            startActivityForResult(intent, 100);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            when (requestCode){
                100 -> {
                    // text1.visibility = View.VISIBLE
                    text1.text = data!!.getStringExtra("title").toString()

                }
            }

        }
    }
}
