import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.cmrk.util.view.CircleImageView
import com.example.logintask.R
import com.example.logintask.databinding.DialogLoadingBinding

object LoadingDialog {

    private lateinit var dialog: Dialog

    fun showLoading(context: Context) {
        dialog = Dialog(context)
        val circleImageView = CircleImageView(context, R.color.white)
        val circleProgressBar = CircularProgressDrawable(context)
        circleProgressBar.setColorSchemeColors(
            context.resources.getColor(android.R.color.holo_red_dark),
            context.resources.getColor(android.R.color.holo_green_dark),
            context.resources.getColor(android.R.color.holo_blue_dark),
            context.resources.getColor(android.R.color.holo_orange_dark)
        )
        circleProgressBar.strokeCap = Paint.Cap.ROUND
        circleProgressBar.strokeWidth = context.resources.getDimension(com.intuit.sdp.R.dimen._4sdp)
        circleProgressBar.centerRadius = context.resources.getDimension(com.intuit.sdp.R.dimen._15sdp)
        circleProgressBar.backgroundColor = Color.TRANSPARENT
        circleImageView.setBackgroundDrawable(circleProgressBar)
        circleProgressBar.start()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogLoadingBinding: DialogLoadingBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_loading, null, false)
        dialog.setContentView(dialogLoadingBinding.root)
        if (dialog.window != null) {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        }
        dialog.setCanceledOnTouchOutside(false)
        dialogLoadingBinding.rootView.addView(circleImageView)
        val lp: LinearLayout.LayoutParams = circleImageView.layoutParams as LinearLayout.LayoutParams
        lp.height = context.resources.getDimension(com.intuit.sdp.R.dimen._50sdp).toInt()
        lp.width = context.resources.getDimension(com.intuit.sdp.R.dimen._50sdp).toInt()
        dialog.show()
    }

    fun hideLoading() {
        if(this::dialog.isInitialized){
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }

    }
}