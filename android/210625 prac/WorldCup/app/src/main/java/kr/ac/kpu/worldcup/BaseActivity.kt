package kr.ac.kpu.worldcup
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

abstract class BaseActivity: AppCompatActivity() {

    abstract fun permissionGranted(requestCode: Int)
    abstract fun permissionDenied(requestCode: Int)

    fun requiredPermissions(permissions: Array<String>, requestCode: Int){
        // 마시멜로 이전 버전이면 권한 처리가 필요함
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            permissionGranted(requestCode)
        }else{
            ActivityCompat.requestPermissions(this, permissions, requestCode) // 허가
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
      if(grantResults.all { it == PackageManager.PERMISSION_GRANTED}){
          permissionGranted(requestCode)
      }else {
          permissionDenied(requestCode)
      }
    }
}