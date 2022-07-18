package com.cmrk.ui.activity.visit

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

class VisitViewModel(private var visitNavigator: VisitNavigator,
                     context: Context): BaseViewModel<VisitNavigator>(visitNavigator, context), NetworkCallBack {

    fun getVisitList(employeeId:String,type:String) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .getDashbordVisitList(employeeId,type),
                this@VisitViewModel
            )
        }
    }
    override fun onSuccess(response: Any?) {
       if(response is VisitResponseModel){
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
                   visitNavigator.getVisitListSuccess(response)
                   LoadingDialog.hideLoading()
               }
               2 -> {
                   response?.message?.let {
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