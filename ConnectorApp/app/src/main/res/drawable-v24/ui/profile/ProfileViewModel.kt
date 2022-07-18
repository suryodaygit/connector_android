package com.cmrk.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cmrk.R
import com.cmrk.base.BaseViewModel
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.network.NetworkCallBack
import com.cmrk.util.AppPreference
import com.cmrk.util.Controller
import com.cmrk.util.LoadingDialog
import com.cmrk.util.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileViewModel(private var profileNavigator: ProfileNavigator, context: Context) :
    BaseViewModel<ProfileNavigator>(profileNavigator, context), NetworkCallBack {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getDashboardData() {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .getDashboardTripData(AppPreference.loadLoginData(context).data?.employeeKeyId.toString()),
                this@ProfileViewModel
            )
        }

    }

    }
    override fun onSuccess(response: Any?) {
            if(response is DashboardResponseModel){
                when (response.status){
                    0 ->{
                        response?.message?.let {
                            Toaster.showShortToast(
                                context,
                                it
                            )
                        }
                        LoadingDialog.hideLoading()
                    }
                    1 -> {
                        LoadingDialog.hideLoading()
                        profileNavigator.ongetDashboardDataSuccess(response)
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

                    else -> {
                        Toaster.showShortToast(
                            context,
                            context.resources.getString(R.string.somethingwentwrong)
                        )
                        LoadingDialog.hideLoading()
                    }
                }
            }
    }

    override fun onError(errorBody: String) {
        profileNavigator.onFailed(errorBody)
        LoadingDialog.hideLoading()
    }
}