package com.cmrk.ui.activity.sparepartsSelling

import android.content.Context
import android.util.Log
import com.cmrk.base.BaseViewModel
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.network.NetworkCallBack
import com.cmrk.ui.activity.SubmitLeadReqModel
import com.cmrk.util.Controller
import com.cmrk.util.LoadingDialog
import com.cmrk.util.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SubmitInquiryViewModel(
    private var submitInquiryNavigator: SubmitInquiryNavigator,
    context: Context
) : BaseViewModel<SubmitInquiryNavigator>(submitInquiryNavigator, context), NetworkCallBack {
    companion object {
    }

    fun submitInquiry(submitLeadReqModel: SubmitLeadReqModel) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .submitLeadInquiry(
                        submitLeadReqModel.employeeKeyId,
                        submitLeadReqModel.tripKeyId,
                        submitLeadReqModel.make,
                        submitLeadReqModel.model,
                        submitLeadReqModel.vehicleId,
                        submitLeadReqModel.manufactureYear,
                        submitLeadReqModel.registrationNumber,
                        submitLeadReqModel.kerbWeight,
                        submitLeadReqModel.vehicleOwnrship,
                        submitLeadReqModel.vehicleCondition,
                        submitLeadReqModel.fuelType,
                        submitLeadReqModel.tankCapacity,
                        submitLeadReqModel.tankExpiryDate,
                        submitLeadReqModel.towingCharge,
                        submitLeadReqModel.expectedAmount,
                        submitLeadReqModel.vehicleImages,
                        submitLeadReqModel.sparePartRequirement,
                        submitLeadReqModel.vehiclesForSpareParts,
                        submitLeadReqModel.specificSparePart,
                        submitLeadReqModel.immediateRequirement,
                        submitLeadReqModel.priceExpectationPerKg,
                        submitLeadReqModel.dailyOfferInterest,
                        submitLeadReqModel.visit_key_id,
                        submitLeadReqModel.orcAvailable
                    ),
                this@SubmitInquiryViewModel
            )
        }
    }

    override fun onSuccess(response: Any?) {
        if (response is SubmitScrapVehicleResponse) {
            when (response.status) {
                0 -> {
                    response.message?.let {
                        Toaster.showShortToast(
                            context,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                }
                1 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            context,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                    submitInquiryNavigator.onSuccessofSubmitLead(response)
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
        Log.e("mTag", "onError= $errorBody")
    }

}

