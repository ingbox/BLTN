package kr.ac.kpu.worldcup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_view_pager.*
import kr.ac.kpu.worldcup.fragments.FragmentFour
import kr.ac.kpu.worldcup.fragments.FragmentOne
import kr.ac.kpu.worldcup.fragments.FragmentThree
import kr.ac.kpu.worldcup.fragments.FragmentTwo

class viewPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        val adapter = MyViewPagerAdapter(supportFragmentManager) // 여기에 adapter를 달아놓고
        adapter.addFragment(FragmentOne(),"One") // 클릭하면 FragmentOne이 실행되도록 하는 거네
        adapter.addFragment(FragmentTwo(),"Two")
        adapter.addFragment(FragmentThree(),"Three")
        adapter.addFragment(FragmentFour(),"Four")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    //viewPager란 화면을 페이지와 같이 좌우로 넘길 때 사용, 페이지의 생명주기를 관리하기 위해 Fragment와 함께 쓰이는 경우가 많음

    class MyViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager){

        private  val fragmentList: MutableList<Fragment> = ArrayList()
        private  val titleList: MutableList<String> = ArrayList()
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
           return fragmentList[position]
        }

        fun addFragment(fragment: Fragment, title: String){
            fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
    }
}