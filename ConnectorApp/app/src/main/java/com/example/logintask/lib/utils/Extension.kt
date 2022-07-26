package com.example.logintask.lib.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthqrapp.lib.utils.Constant
import com.example.logintask.BuildConfig
import com.example.logintask.R
import com.example.logintask.dashboard.DashboardActivity
import com.example.logintask.onboarding.login.LoginActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)
/**
 * Print message in Toast
 */
fun showMessage(context:Context,msg:String){
    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
}

/**
 * Extension method to get View Model for Activity.
 */
fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this).get(viewModelClass)

/*
* Check the email validation
*/
fun checkEmail(email: String): Boolean {
    return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
}


fun showSnackbar(view: View, msg:String){
    val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
    snackbar.view.setPadding(10, 0, 10, 10)
    snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackbar.show()
}

fun setDataInPreference(mContext:Context,key:String,value:String) {
    val sharedPreference = mContext.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.putString(key,value)
    editor.apply()
}

fun getPreferenceData(mContext:Context,key:String,defaultString:String):String{
    val sharedPreference = mContext.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    val value = sharedPreference.getString(key,defaultString)
    return value.toString()
}

fun showToast(context:Context,msg:String){
    Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
}

 fun logoutDialog(mContext: Context) {
     val builder = android.app.AlertDialog.Builder(mContext)
     val view = LayoutInflater.from(mContext).inflate(
         R.layout.custom_logout_dialog, null
     )
     builder.setView(view)
     val btn_yes = view.findViewById<Button>(R.id.btn_yes)
     val btn_no = view.findViewById<Button>(R.id.btn_no)

     val alertDialog = builder.create()
     alertDialog.show()

     btn_yes.setOnClickListener {
         alertDialog.dismiss()
         mContext.startActivity(Intent(mContext, LoginActivity::class.java))
     }

     btn_no.setOnClickListener {
         alertDialog.dismiss()
     }
 }


fun SuccessDialog(mContext: Context,msg:String,redirect:String) {
    val builder = android.app.AlertDialog.Builder(mContext)
    val view = LayoutInflater.from(mContext).inflate(
        R.layout.custom_success_dialog, null
    )
    builder.setView(view)
    val tv_msg = view.findViewById<TextView>(R.id.tv_msg)
    val btn_ok = view.findViewById<Button>(R.id.btn_ok)

    tv_msg.text = msg

    val alertDialog = builder.create()
    alertDialog.show()

    btn_ok.setOnClickListener {
        alertDialog.dismiss()
        if(redirect == "Login") {
            mContext.startActivity(Intent(mContext, LoginActivity::class.java))
        }else{
            mContext.startActivity(Intent(mContext, DashboardActivity::class.java))
        }
    }


}


@RequiresApi(Build.VERSION_CODES.M)
 fun chooseImageOption(mContext:Activity){
    val builder = AlertDialog.Builder(mContext)
    val view = LayoutInflater.from(mContext).inflate(
        R.layout.custom_choose_option_alert_dialog,null)
    builder.setView(view)
    val iv_gallery = view.findViewById<ImageView>(R.id.iv_gallery)
    val iv_camera = view.findViewById<ImageView>(R.id.iv_camera)

    val alertDialog = builder.create()
    alertDialog.show()

    iv_gallery.setOnClickListener {
        openGallery(mContext)
        alertDialog.dismiss()
    }

    iv_camera.setOnClickListener {
        openCameraApp(mContext)
        alertDialog.dismiss()
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun openCameraApp(mContext: Activity) {
    if (!PermissionHelper.checkPermission(
            mContext,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) || !PermissionHelper.checkPermission(
            mContext,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    ) {
        PermissionHelper.askPermission(
            mContext, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            CAMERA_REQUEST
        )
    }else{
        // This is not the right way to do this, but for some reason, having
        // it store it in
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI isn't working right.
      /*  val date = Date()
        val df = SimpleDateFormat("-mm-ss")

        val newPicFile = "/qr" + df.format(date) + ".jpg"
        val outPath = Environment.getExternalStorageDirectory().toString() + newPicFile
        val outFile = File(outPath)

        cameraFilePath = outFile.toString()
        cameraOutURI = FileProvider.getUriForFile(
            mContext, BuildConfig.APPLICATION_ID +
                    ".provider", outFile
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
       // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutURI)
        mContext.startActivityForResult(cameraIntent, CAMERA_REQUEST)*/

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile(mContext)

        val uri: Uri = FileProvider.getUriForFile(
            mContext!!,
            "${mContext?.packageName}.fileprovider",
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        mContext.startActivityForResult(intent, CAMERA_REQUEST)

    }
}

 fun openGallery(mContext: Activity) {

    if (ContextCompat.checkSelfPermission(
            mContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        mContext.startActivityForResult(pickPhoto,PICK_GALLERY_IMAGES_CODE)
    } else {
        // Request permission from the user
        ActivityCompat.requestPermissions(
            mContext,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0
        )
    }
}

private fun createFile(mContext: Activity): File {
    val storageDir: File = mContext!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    val timeStamp = System.currentTimeMillis()
    return File.createTempFile("IMG_$timeStamp" + "_DOC", ".jpg", storageDir).apply {
        Constant.mCurrentPhotoPath = absolutePath
    }
}


