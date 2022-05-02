package kr.ac.kpu.worldcup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_pending.*

class PendingActivity : AppCompatActivity() {

    val channel_name: String = "CHANNEL_1"
    val CHANNEL_ID: String = "MY_CH"
    val notificationId: Int = 1002
    val notificationId2: Int = 1003
//    val notificationId3: Int = 1004
//    val notificationId4: Int = 1005
//    val notificationId5: Int = 1006
//    val notificationId6: Int = 1007


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending)

        button1.setOnClickListener{ view->
            var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("기본 알림")
                .setContentText("띠리리리롱 따르르링 또로로롱")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            createNotificationChannel(builder, notificationId)

        }

        button2.setOnClickListener{ view->
            var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("긴급 알림")
                .setContentText("삐용~ 삐용~ 삐용~ 삐용~ 삐용~ 삐용~")
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText(
                        "삐요이이이잉이이이이이잉 삐요이이이이이이이이이잉 삐요이이이이이이이이잉 삐까츄"
                    )
                ).setPriority(NotificationCompat.PRIORITY_DEFAULT)

            createNotificationChannel(builder, notificationId2)

        }
    }

    private fun createNotificationChannel(
        builder: NotificationCompat.Builder,
        notificationId: Int
    ) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = "1번 채널입니다"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, channel_name, importance).apply {
                description = descriptionText
            }

            channel.lightColor = Color.BLUE
            channel.enableVibration(true)

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(notificationId, builder.build())
        } else{
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, builder.build())

        }
    }
}