package kr.ac.kpu.dtuch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.preference.PreferenceManager

import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pre_analysis_acitivity.*


class PreAnalysisAcitivity : AppCompatActivity() {

    private var m_bluetoothAdapter: BluetoothAdapter? =null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    internal lateinit var viewpager: ViewPager
    lateinit var firstName : String       // 이름
    lateinit var lastName : String        // 성
    lateinit var birthDay : String        // 일
    lateinit var birthMonth : String      // 월
    lateinit var birthYear : String       // 년


    companion object{
        val EXTRA_ADDRESS: String = "Device_address"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_analysis_acitivity)

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter == null){
            Toast.makeText( this,"this device doesn't support bluetooth", Toast.LENGTH_SHORT).show()
            return
        }
        if(!m_bluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }
        select_device_refresh.setOnClickListener{
            pairedDeviceList()
        }

    }

    private fun pairedDeviceList(){

        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val list: ArrayList<BluetoothDevice> = ArrayList()

        if(!m_pairedDevices.isEmpty()) {
            for (device: BluetoothDevice in m_pairedDevices) {
                list.add(device)
                Log.i("device", "" + device)
            }
        } else{
            Toast.makeText(this, "no paired bluetooth devices found", Toast.LENGTH_SHORT).show()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address

            val intent = Intent(this, AnalysisActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
                .putExtra("first_name", firstName)
                .putExtra("last_name", lastName)
                .putExtra("birthDay", birthDay)
                .putExtra("birthMonth", birthMonth)
                .putExtra("birthYear", birthYear)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){}
        if(resultCode == Activity.RESULT_OK){
            if(m_bluetoothAdapter!!.isEnabled){
                Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bluetooth has been disabled", Toast.LENGTH_SHORT).show()

            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Blutooth enabling has been canceled", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadData(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)      // Preference 객체 정의

        val lfirstName = pref.getString("keyFname", "")
        val llastName = pref.getString("keyLname", "")
        val lbirthDay = pref.getString("keyBday", "")
        val lbirthMonth = pref.getString("keyBmonth", "")
        val lbirthYear = pref.getString("keyByear", "")

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
    }


}