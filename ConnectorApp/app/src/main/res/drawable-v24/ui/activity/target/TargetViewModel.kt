package com.cmrk.ui.activity.target

import android.content.Context
import com.cmrk.base.BaseViewModel
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.network.NetworkCallBack
import com.cmrk.util.Controller
import com.cmrk.util.LoadingDialog
import com.cmrk.util.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TargetViewModel(private var visitNavigator: TargetNavigator,
                      context: Context): BaseViewModel<TargetNavigator>(visitNavigator, context), NetworkCallBack {

    fun getTargetList(employeeId:String,type:String) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .getDashbordTargetList(employeeId,type),
                this@TargetViewModel
            )
        }
    }
    override fun onSuccess(response: Any?) {
       if(response is TargetResponseModel){
           when(response.status){
               0->{
                   response?.message?.let {
                       Toaster.showShortToast(
                           context,
                           it
                       )
                   }
                   LoadingDialog.hideLoading()
               }
               1->{
                   visitNavigator.getTargetListSuccess(response)
                   LoadingDialog.hideLoading()
               }
               2 -> {
                   response.message?.let {
                       Toaster.showShortToast(
                           context,
                           it
                       )
                   }
                   LoadingDialog.hideLoading()
                   Controller.RedirectToLoginActivity(context)
               }
           }
       }
    }

    override fun onError(errorBody: String) {
            Toaster.showShortToast(
                context,
                errorBody
            )
    }
}