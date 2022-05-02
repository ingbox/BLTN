package kr.ac.kpu.dtuch
import android.os.Bundle
import android.os.Handler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_analysis.*
import kotlinx.android.synthetic.main.bottomsheet_fragment.*


class BottomSheetFragment: BottomSheetDialogFragment() {


    private var i = 0
    private var txtView: TextView? = null
    private val handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_fragment, container, false)

        extract_button.setOnClickListener {

            progress_Bar.visibility = View.VISIBLE
            var i = progress_Bar.progress
            Thread(Runnable {
                // this loop will run until the value of i becomes 99
                while (i < 100) {
                    i += 1
                    // Update the progress bar and display the current value
                    handler.post(Runnable {
                        progress_Bar.progress = i
                        // setting current progress to the textview
                        txtView!!.text = i.toString() + "/" + progress_Bar.max
                    })
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                progress_Bar.visibility = View.INVISIBLE
            }).start()
        }
    }

}


