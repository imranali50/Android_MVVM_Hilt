package com.app.mvvm.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.mvvm.R


//permission
val locationCourse = Manifest.permission.ACCESS_COARSE_LOCATION
val locationFine = Manifest.permission.ACCESS_FINE_LOCATION


//val CAMERA_PERMISSION = Manifest.permission.CAMERA
val RECORD_AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
val CONTACT_PERMISSION = Manifest.permission.READ_CONTACTS

val WRITE_PERMISSION = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
val AUDIO_PERMISSION = arrayOf(Manifest.permission.RECORD_AUDIO)
val GALLERY_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )
} else {
    arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}

val notificationPermission = arrayOf(Manifest.permission.POST_NOTIFICATIONS)

val PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO
    )
} else {
    arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}
val audioPermission = arrayOf(Manifest.permission.RECORD_AUDIO)
val CAMERA_RECORD_AUDIO_PERMISSION = arrayOf(
    CAMERA_PERMISSION, Manifest.permission.RECORD_AUDIO
)

val Audio_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    arrayOf(
        Manifest.permission.READ_MEDIA_AUDIO
    )
} else {
    arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
}

val packageUsesPermission = arrayOf(
    Manifest.permission.PACKAGE_USAGE_STATS
)

fun isGPSEnabled(mContext: Context): Boolean {
    val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}


fun hasPermissions(context: Activity, permissions: Array<String>): Boolean = permissions.all {
    ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
}

fun checkSelfPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
    ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
}


fun shouldShow(context: Activity, permissions: Array<String>): Boolean = permissions.all {
    ActivityCompat.shouldShowRequestPermissionRationale(context, it)
}

fun milliSecondsToTimer(milliseconds: Long): String {
    var finalTimerString = ""
    var secondsString = ""

    //Convert total duration into time
//    val hours = (milliseconds / (1000 * 60 * 60)).toInt()
    val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
    val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()

    // Pre appending 0 to seconds if it is one digit
    val minutesString = if (minutes < 10) {
        "0$minutes"
    } else {
        "" + minutes
    }
    secondsString = if (seconds < 10) {
        "0$seconds"
    } else {
        "" + seconds
    }
    finalTimerString = "$minutesString:$secondsString"

    // return timer string
    return finalTimerString
}


fun Activity.fullScreen() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
}

fun Activity.statusBarForAll() {
    val window: Window = window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(this@statusBarForAll, R.color.black)
    window.navigationBarColor =
        ContextCompat.getColor(this@statusBarForAll, R.color.black)
}
fun Activity.statusBarWhiteScreen() {
    val window: Window = window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(this@statusBarWhiteScreen, R.color.white)
    window.navigationBarColor =
        ContextCompat.getColor(this@statusBarWhiteScreen, R.color.white)
}
