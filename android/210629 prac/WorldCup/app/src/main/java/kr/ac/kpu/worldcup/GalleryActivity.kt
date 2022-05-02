package kr.ac.kpu.worldcup

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_gallery.*
import kr.ac.kpu.worldcup.databinding.ActivityGalleryBinding
import java.io.IOException
import java.text.SimpleDateFormat

class GalleryActivity : BaseActivity() {

    val PERM_STORAGE = 9
    val PERM_CAMERA = 10
    val REQ_CAMERA = 11
    val REQ_GALLERY = 12
    val binding by lazy { ActivityGalleryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //공용 저장소에 권한이 있는지 확인
        requiredPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),PERM_STORAGE)
    }

    fun initViews(){
        //카메라 버튼에 onClickListener을 하고 띄우는 즉 권한이 permission 된 것만 띄우도록


        binding.buttonCamera.setOnClickListener{
            requiredPermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
        }

        binding.buttonGallery.setOnClickListener {
            openGallery()
        }
    }


    //원본 이미지의 주소를 저장할 변수
    var realUri: Uri? = null

    // 카메라에 찍은 사진을 저장하기 위한 Uri를 넘겨줌
    fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        createImageUri(newfileName(),"image/jpg")?.let { uri->  //반환되면 그 값을 가져와서 바로 이어서 쓸 수 있도록 하는 건가벼

            realUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)  // 카메라 uri를 realUri 가지고 해
            startActivityForResult(intent,REQ_CAMERA)
        }
    }

    fun openGallery() {

        val intent = Intent(Intent.ACTION_PICK) //선택창에서 어떤 데이터를 할지
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_GALLERY)
    }

    //원본 이미지를 저장할 Uri를 MediaStore에 생성 (안드로이드가 가지고 있는 데이터베이스)
    fun createImageUri(filename: String, mimeType: String): Uri?{

        val values = ContentValues()//내가 저장하려는 미디어의 정보를 넘겨줌
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename) // 파일의 이름을
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType) // 파일의 이름을

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) // 미디어가 저장되는 데이터베이스 테이블 values를 집어넣어
        //파일을 저장할 수 있는 uri를 넘겨줌
    }

    //파일 이름을 생성하는 메서드
    fun newfileName(): String{

        val sdf =  SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }

    //원본 이미지를 불러오는 메서드

    fun loadBitmap(photoUri: Uri) : Bitmap? {
        var image: Bitmap?  = null
        try {
            return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1){
                val source = ImageDecoder.createSource(contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            }
        } catch (e:IOException){
            e.printStackTrace()
        }
        return null
    }

    override fun permissionGranted(requestCode: Int) {
        when(requestCode){
            PERM_STORAGE -> initViews()
            PERM_CAMERA ->  openCamera()
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when(requestCode){
            PERM_STORAGE -> {

                Toast.makeText(this, "공용 저장소 권한을 요청해야만 앱을 사용할 수 있습니다.",Toast.LENGTH_SHORT).show()
                finish()
            }
            PERM_CAMERA -> Toast.makeText(this, "카메라 권한을 승인해야만 카메라를 사용할 수 있습니다.",Toast.LENGTH_SHORT).show()

        }
    }


    // 카메라 찍은 후 호출 , 갤러리 선택 후 호출
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // 결과값이 넘어오면 이리로 넘어옴 data안에 이미지가 들어가 있어
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK ){
            when(requestCode){
                REQ_CAMERA -> {
//                    val bitmap = data?.extras?.get("data") as Bitmap// 미리보기 이미지야 그냥 이미지가 아니라 원본 이미지를 가져와야돼 저장해야할 URI를 만들어야한다네
//                    binding.imagePreview.setImageBitmap(bitmap)
                    realUri?.let { uri -> //스코프 함수 널이 아닐 때만 사용할 수 있게
                        val bitmap = loadBitmap(uri)
                        binding.imagePreview.setImageBitmap(bitmap)

                        realUri = null
                    }
                }

                REQ_GALLERY -> {
                    data?.data?.let { uri ->
                        binding.imagePreview.setImageURI(uri)
                    } //uri 형태로 이미지 주소가 이미 담겨 있음

                }
            }
        }
    }
}