package com.cmrk.ui.activity.vehicleBuying

import android.content.Context
import android.widget.Toast
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import kotlin.properties.Delegates


class ScrapVehicleViewModel(
    private var scrapVehicleNavigator: ScrapVehicleNavigator,
    context: Context
) : BaseViewModel<ScrapVehicleNavigator>(scrapVehicleNavigator, context),NetworkCallBack {
    companion object{
        var isScrapVehicleSelling by Delegates.notNull<Boolean>()
    }

     var isScrapVehicleSelling: Boolean = false

    fun openDartePickerDialog() {
        scrapVehicleNavigator.openDatepicker()
    }
    fun isScrapVehicleSellingRightNowORNot(isChecked:Boolean){
        isScrapVehicleSelling = isChecked
    }

    fun submitScarpVehicleDetails(submitVisitDetails: SubmitVisitRequestModel) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .submitVisitDetails(
                        submitVisitDetails.employee_key_id,
                        submitVisitDetails.trip_key_id,
                        submitVisitDetails.master_vehicle_id,
                        submitVisitDetails.owner_mobile_number,
                        submitVisitDetails.photo_of_place,
                        submitVisitDetails.visitor,
                        submitVisitDetails.latitude,
                        submitVisitDetails.longitude,
                        submitVisitDetails.date,
                        submitVisitDetails.mobile_number,
                        submitVisitDetails.garage_name,
                        submitVisitDetails.contact_person,
                        submitVisitDetails.owner_name,
                        submitVisitDetails.business_card,
                        submitVisitDetails.no_of_visit,
                        submitVisitDetails.remarks,
                        submitVisitDetails.scrap_vehicle,
                        submitVisitDetails.monthly_vehicles_vol,
                        submitVisitDetails.expected_price_per_kg,
                        submitVisitDetails.scrap_vehicle_now,
                        submitVisitDetails.next_folowup_date,
                        submitVisitDetails.scrap_remark
                    ), this@ScrapVehicleViewModel
            )
        }

    }

        override fun onSuccess(response: Any?) {
            if (response is SubmitVisitResponseModel) {
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
                        response?.message?.let {
                            Toaster.showShortToast(
                                context,
                                it
                            )
                        }
                        LoadingDialog.hideLoading()
                        scrapVehicleNavigator.successVisitorDetails(response)
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
        Toast.makeText(context,"msg "+errorBody,Toast.LENGTH_LONG).show()
    }

}

