package com.cmrk.ui.activity.sparepartsSelling

import com.cmrk.base.BaseNavigator

interface SubmitInquiryNavigator : BaseNavigator {
    fun onSuccessofSubmitLead(response: SubmitScrapVehicleResponse)
}