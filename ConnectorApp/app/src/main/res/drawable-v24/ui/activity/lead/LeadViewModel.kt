package com.cmrk.ui.activity.visit

import android.content.Context
import com.cmrk.base.BaseViewModel
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.network.NetworkCallBack
import com.cmrk.ui.activity.lead.LeadResponseModel
import com.cmrk.util.Controller
import com.cmrk.util.LoadingDialog
import com.cmrk.util.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LeadViewModel(private var visitNavigator: LeadNavigator,
                    context: Context): BaseViewModel<LeadNavigator>(visitNavigator, context), NetworkCallBack {

    fun getLeadList(employeeId:String,type:String) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .getDashbordLeadList(employeeId,type),
                this@LeadViewModel
            )
        }
    }
    override fun onSuccess(response: Any?) {
       if(response is LeadResponseModel){
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
                   visitNavigator.getLeadListSuccess(response)
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