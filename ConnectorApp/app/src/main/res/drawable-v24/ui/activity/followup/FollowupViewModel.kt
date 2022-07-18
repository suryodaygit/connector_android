package com.cmrk.ui.activity.visit

import android.content.Context
import com.cmrk.base.BaseViewModel
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.network.NetworkCallBack
import com.cmrk.ui.activity.followup.FollowupResponseModel
import com.cmrk.util.Controller
import com.cmrk.util.LoadingDialog
import com.cmrk.util.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FollowupViewModel(private var visitNavigator: FollowupNavigator,
                        context: Context): BaseViewModel<FollowupNavigator>(visitNavigator, context), NetworkCallBack {

    fun getVisitList(employeeId:String,type:String) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .getDashbordFollowupList(employeeId,type),
                this@FollowupViewModel
            )
        }
    }
    override fun onSuccess(response: Any?) {
       if(response is FollowupResponseModel){
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
                   visitNavigator.getFolloupListSuccess(response)
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