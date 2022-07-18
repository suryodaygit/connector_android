package com.cmrk.ui.activity.visitor_detail

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
import okhttp3.RequestBody


class VisitoPersonAreaDetailViewModel(
    private var visitorPersonAreaDetailNavigator: VisitorPersonAreaDetailNavigator,
    context: Context
) :
    BaseViewModel<VisitorPersonAreaDetailNavigator>(visitorPersonAreaDetailNavigator, context), NetworkCallBack {

    fun getVisitorPersonAreaDetail(multipleImageArray: RequestBody) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .addMultipleImage(multipleImageArray),
                this@VisitoPersonAreaDetailViewModel
            )
        }
    }

    fun getNoOfVisit(garageName:String) {
     //   LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context).noOfVisitDetails(garageName),
                this@VisitoPersonAreaDetailViewModel
            )
        }
    }

        fun getDeleteImage(imageName:String) {
            LoadingDialog.showLoading(context)
            GlobalScope.launch(Dispatchers.Main) {
                ApiRequest(context, true).apiRequest(
                    ApiProvider.provideApi(context).deleteImageDBAsync(imageName),
                    this@VisitoPersonAreaDetailViewModel
                )
            }

        }


    override fun onSuccess(response: Any?) {
        if (response is VisitorPersonAreaDetailResponseModel) {
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
                    visitorPersonAreaDetailNavigator.ongetVisitorPersonAreaDetailSuccess(response)
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
        }else if (response is NoOfVisitResponseModel) {
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
                        visitorPersonAreaDetailNavigator.ongetNoOFVisitSuccess(response)
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
                    visitorPersonAreaDetailNavigator.ongetdeleteImageSuccess(response)
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

