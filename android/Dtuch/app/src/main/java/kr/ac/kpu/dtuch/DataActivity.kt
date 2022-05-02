package kr.ac.kpu.dtuch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_data.*
import kotlinx.android.synthetic.main.activity_main.*

class DataActivity : AppCompatActivity() {

    // from main Activity
    private fun saveData(sfirstName:String, slastName:String, sbirthDay:String, sbirthMonth:String, sbirthYear:String){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)      // Preference 객체 정의
        val editor = pref.edit()        // Preference에 데이터를 담을수 있는 Preference editor

        Log.d("저장할 데이터","$sfirstName, $slastName, $sbirthDay $sbirthMonth, $sbirthYear")

        editor.putString("keyFname",sfirstName)
        editor.putString("keyLname",slastName)
        editor.putString("keyBday",sbirthDay)
        editor.putString("keyBmonth",sbirthMonth)
        editor.putString("keyByear",sbirthYear)
            .apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        sign_in_button.setOnClickListener{
            val datePicker = findViewById<DatePicker>(R.id.dpSpinner)  // datepickerSpinner
            val birthDay = datePicker.getDayOfMonth();      // 일
            val birthMonth = datePicker.getMonth() + 1;     // 월
            val birthYear = datePicker.getYear();           // 년

            val MainIntent = Intent(this, MainActivity::class.java)     // to Main from Data

            saveData(input_first_name.text.toString(), input_last_name.text.toString(),
                birthDay.toString(), birthMonth.toString(), birthYear.toString())

            startActivity(MainIntent)
        }
    }
}