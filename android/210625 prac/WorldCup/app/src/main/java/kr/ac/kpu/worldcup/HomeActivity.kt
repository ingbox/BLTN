package kr.ac.kpu.worldcup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        start_button.setOnClickListener{
            val nextIntent = Intent(this, MainActivity::class.java)
            nextIntent.putExtra("one","android")
            nextIntent.putExtra("two","apple")

            val earth = Space("Earth", 1)
            nextIntent.putExtra("earth",earth)
            startActivity(nextIntent)

        }

        rank_button.setOnClickListener {

            val nextIntent = Intent(this, MainCategories::class.java)
            startActivity(nextIntent)
        }

        fragment_button.setOnClickListener {

            val nextIntent = Intent(this, FragmentMainActivity::class.java)
            startActivity(nextIntent)
        }

        viewpaager_button.setOnClickListener {

            val nextIntent = Intent(this, viewPagerActivity::class.java)
            startActivity(nextIntent)
        }

        gallery_button.setOnClickListener {

            val nextIntent = Intent(this, GalleryActivity::class.java)
            startActivity(nextIntent)
        }

        alarm_button.setOnClickListener {

            val nextIntent = Intent(this, PendingActivity::class.java)
            startActivity(nextIntent)
        }

    }
}