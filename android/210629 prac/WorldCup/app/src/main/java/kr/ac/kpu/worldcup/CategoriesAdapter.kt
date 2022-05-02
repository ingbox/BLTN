package kr.ac.kpu.worldcup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CategoriesAdapter(val categoriesList: ArrayList<Categories>): RecyclerView.Adapter<CategoriesAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapter.CustomViewHolder {
        // activty onCreate랑 비슷 이거는 xml을 연결하는 역할인데 adapter에서 plug로 연결해야할 것이 먼가 찾는거 우리는 list item을 만들어놨으니까 그것을 붙이게 될거야

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        //inflate 붙이다
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener{
                val curPos: Int = adapterPosition //dialog로 넘기는 기능을 구현해야한다
                val category: Categories = categoriesList.get(curPos)
                Toast.makeText(parent.context, "이름: ${category.name} 나이: ${category.age} 직업: ${category.job}",Toast.LENGTH_SHORT).show() // parent.context 는 MainActivity를 의미
            }
        }
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.CustomViewHolder, position: Int) {
        //스크롤을 할 때 이게 지속적으로 호출해주면서 뷰에대해서 안정적으로 매칭을 시켜주는 부분
        holder.gender.setImageResource(categoriesList.get(position).gender) // 바인딩 되고 있는 포지션 연동
        holder.name.text = categoriesList.get(position).name // 리스트 아이템에 있는 set text를 하는 부분
        holder.age.text = categoriesList.get(position).age.toString() // categories 리스트에서 Int형으로 선언 해 놓았기 때문에 변환
        holder.job.text = categoriesList.get(position).job
    }


    override fun getItemCount(): Int {
        //총 리스트에 대한 총 개수를 적어줘야함
        return categoriesList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gender = itemView.findViewById<ImageView>(R.id.iv_profile) // 성별
        val name = itemView.findViewById<TextView>(R.id.iv_name) // 이름
        val age = itemView.findViewById<TextView>(R.id.iv_age) // 나이
        val job = itemView.findViewById<TextView>(R.id.iv_job) // 직업

    }


}