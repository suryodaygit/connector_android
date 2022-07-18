package com.cmrk.ui.activity.vehicleBuying

import com.cmrk.base.BaseNavigator

interface ScrapVehicleNavigator : BaseNavigator {
    fun openDatepicker()
    fun successVisitorDetails(response:SubmitVisitResponseModel)
}
