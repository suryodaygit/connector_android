package com.cmrk.ui.activity.vehicleDetail

import android.content.Context
import com.cmrk.base.BaseViewModel
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.network.NetworkCallBack
import com.cmrk.ui.activity.visitor_detail.DeleteImageResponseModel
import com.cmrk.ui.activity.visitor_detail.VisitorPersonAreaDetailResponseModel
import com.cmrk.util.Controller
import com.cmrk.util.LoadingDialog
import com.cmrk.util.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.RequestBody


class VehicleDetailViewModel(
    private var vehicleDetailNavigator: VehicleDetailNavigator,
    context: Context
) :
    BaseViewModel<VehicleDetailNavigator>(vehicleDetailNavigator, context), NetworkCallBack {

    fun getVehicleDetail(modelId: String) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .getVehicleDetail(modelId),
                this@VehicleDetailViewModel
            )
        }
    }

    fun getVisitorPersonAreaDetail(multipleImageArray: RequestBody) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .addMultipleImage(multipleImageArray),
                this@VehicleDetailViewModel
            )
        }
    }

    fun getDeleteImage(imageName:String) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context).deleteImageDBAsync(imageName),
                this@VehicleDetailViewModel
            )
        }

    }


    override fun onSuccess(response: Any?) {
        if (response is VehicleDetailResponseModel) {
            when (response.status) {
                0 -> {
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
                    vehicleDetailNavigator.ongetVehicleDetailSuccess(response)
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
        } else if (response is VisitorPersonAreaDetailResponseModel) {
            when (response.status) {
                0 -> {
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
                    vehicleDetailNavigator.ongetVisitorPersonAreaDetailSuccess(response)
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
        } else if (response is DeleteImageResponseModel) {
            when (response.status) {
                0 -> {
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
                    vehicleDetailNavigator.ongetImageDeleteSuccess(response)
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

