package kr.ac.kpu.worldcup.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_a.view.*
import kr.ac.kpu.worldcup.Communicator
import kr.ac.kpu.worldcup.R

class FragmentA : Fragment() {

    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_a, container, false)
        communicator  = activity as Communicator

        view.send_button.setOnClickListener {
            communicator.passDataCom(view.message_input.text.toString())
        }
        return view
    }
}