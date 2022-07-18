package com.cmrk.ui.trips

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmrk.base.BaseViewModel
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.network.NetworkCallBack
import com.cmrk.ui.activity.lead.LeadResponseModel
import com.cmrk.ui.activity.visit.LeadNavigator
import com.cmrk.util.Controller
import com.cmrk.util.LoadingDialog
import com.cmrk.util.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PerformanceViewModel(private var performanceNavigator: PerformanceNavigator,
                           context: Context): BaseViewModel<PerformanceNavigator>(performanceNavigator, context), NetworkCallBack {


    fun getPerformanceList(employeeId:String) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .getPerformanceList(employeeId),
                this@PerformanceViewModel
            )
        }
    }

    override fun onSuccess(response: Any?) {
        if(response is PerformanceResponseModel){
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
                    performanceNavigator.performanceSuccess(response)
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
    }
}