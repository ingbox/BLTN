package kr.ac.kpu.worldcup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main_categories.*

class MainCategories : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_categories)

        val categoriesList = arrayListOf(
        Categories(R.drawable.man,"잉브",25,"편집자") ,
        Categories(R.drawable.woman,"잉브",25,"편집자"),
        Categories(R.drawable.man,"잉브",25,"편집자"),
        Categories(R.drawable.woman,"잉브",25,"편집자"),
        Categories(R.drawable.man,"잉브",25,"편집자"),
        Categories(R.drawable.woman,"잉브",25,"편집자"),
        Categories(R.drawable.man,"잉브",25,"편집자"),
        Categories(R.drawable.woman,"잉브",25,"편집자"),
        Categories(R.drawable.man,"잉브",25,"편집자")
        )

        rv_categories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false )//recyclerView에서 연결해줘야하는 부분
        rv_categories.setHasFixedSize(true) // 성능 개선 부분

        rv_categories.adapter = CategoriesAdapter(categoriesList)
    }
}