package com.example.logintask.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.logintask.R
import java.io.File
import java.util.ArrayList

class ImagePicker {

    companion object {
        private val DEFAULT_MIN_WIDTH_QUALITY = 400 // min pixels
        private val TAG = "ImagePicker"
        private val TEMP_IMAGE_NAME = "tempImage"


        fun onPickImage(ID: Int,context: Activity) {
            try {
                val chooseImageIntent = getPickImageIntent(context)
                context.startActivityForResult(chooseImageIntent, ID)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        private fun getPickImageIntent(context: Context): Intent? {
            var chooserIntent: Intent? = null
            var intentList: MutableList<Intent?> = ArrayList()
            val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoIntent.putExtra("return-data", true)
            takePhotoIntent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(getTempFile(context))
            )
            intentList = addIntentsToList(context, intentList, pickIntent)
            intentList = addIntentsToList(context, intentList, takePhotoIntent)
            if (intentList.size > 0) {
                chooserIntent = Intent.createChooser(intentList.removeAt(intentList.size - 1),
                    context.getString(R.string.imageChoose)
                )
                chooserIntent.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    intentList.toTypedArray()
                )
            }
            return chooserIntent
        }

        fun onPickCamImage(ID: Int,context: Activity) {
            try {
                val chooseImageIntent = getPickCamIntent(context)
                context.startActivityForResult(chooseImageIntent, ID)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        private fun getPickCamIntent(context: Context): Intent? {
            var chooserIntent: Intent? = null
            var intentList: MutableList<Intent?> = ArrayList()
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoIntent.putExtra("return-data", true)
            takePhotoIntent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(getTempFile(context))
            )
            intentList = addIntentsToList(context, intentList, takePhotoIntent)
            if (intentList.size > 0) {
                chooserIntent = Intent.createChooser(intentList.removeAt(intentList.size - 1),
                    context.getString(R.string.imageChoose)
                )
                chooserIntent.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    intentList.toTypedArray()
                )
            }
            return chooserIntent
        }

        private fun getTempFile(context: Context): File {
            val imageFile = File(context.externalCacheDir, TEMP_IMAGE_NAME)
            imageFile.parentFile!!.mkdirs()
            return imageFile
        }

    fun getImageFromResultUri(
        context: Context, resultCode: Int,
        imageReturnedIntent: Intent?
    ): Uri? {
        val bm: Bitmap? = null
        val imageFile = getTempFile(context)
        var selectedImage: Uri? = null
        if (resultCode == Activity.RESULT_OK) {
            val isCamera = imageReturnedIntent == null || imageReturnedIntent.data == null ||
                    imageReturnedIntent.data.toString().contains(imageFile.toString())
            selectedImage = if (isCamera) {
                /** CAMERA  */
                FileProvider.getUriForFile(context,context.applicationContext.packageName
                        + ".provider", imageFile)
            } else {
                /** ALBUM  */
                imageReturnedIntent!!.data!!
            }
            /*bm = getImageResized(context, selectedImage);
            int rotation = getRotation(context, selectedImage, isCamera);
            bm = rotate(bm, rotation);*/
        }
        return selectedImage
    }


    private fun addIntentsToList( context: Context,list: MutableList<Intent?>,
                                      intent: Intent): MutableList<Intent?> {
            val resInfo = context.packageManager.queryIntentActivities(intent, 0)
            for (resolveInfo in resInfo) {
                val packageName = resolveInfo.activityInfo.packageName
                val targetedIntent = Intent(intent)
                targetedIntent.setPackage(packageName)
                list.add(targetedIntent)
            }
            return list
        }
    }
}