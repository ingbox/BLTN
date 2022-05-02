package kr.ac.kpu.dtuch

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    internal lateinit var viewpager: ViewPager
    lateinit var firstName : String       // 이름
    lateinit var lastName : String        // 성
    lateinit var birthDay : String        // 일
    lateinit var birthMonth : String      // 월
    lateinit var birthYear : String       // 년
    var initApp : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager = findViewById(R.id.view_pager) as ViewPager

        val adapter = ViewPagerAdapter(this)
        viewpager.adapter = adapter


        btn_1.setOnClickListener {       // 데이터 입력
            val DataIntent = Intent(this, DataActivity::class.java)

            startActivity(DataIntent)
            onStop()
        }

        btn_2.setOnClickListener {     // 커피 추천
            var AnalysisIntent = Intent(this, PreAnalysisAcitivity::class.java)
                .putExtra("first_name", firstName)
                .putExtra("last_name", lastName)
                .putExtra("birthDay", birthDay)
                .putExtra("birthMonth", birthMonth)
                .putExtra("birthYear", birthYear)

            startActivity(AnalysisIntent)
        }

        btn_3.setOnClickListener {
            var in_useway = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/IptQbHSC4I0"))
            startActivity(in_useway)
        }

        btn_4.setOnClickListener {
            var in_as = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://sonicdutch.modoo.at/?link=bthy7r63")
            )
            startActivity(in_as)
        }

    }

        private fun loadData(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)      // Preference 객체 정의

        val lfirstName = pref.getString("keyFname", "")
        val llastName = pref.getString("keyLname", "")
        val lbirthDay = pref.getString("keyBday", "")
        val lbirthMonth = pref.getString("keyBmonth", "")
        val lbirthYear = pref.getString("keyByear", "")

        if(lfirstName!="")
            initApp = 1

        // load시 초기화 (늦은 초기화)
        firstName = lfirstName.toString()      // 이름
        lastName = llastName.toString()        // 성
        birthDay = lbirthDay.toString()        // 일
        birthMonth = lbirthMonth.toString()    // 월
        birthYear = lbirthYear.toString()      // 년

    }

    override fun onResume() {       // from Data
        super.onResume()
        loadData()
        if (initApp == 0) {
            btn_2.isEnabled = false
        } else {
            btn_2.isEnabled = true
        }
    }

}