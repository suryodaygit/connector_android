package com.cmrk.ui.activity.visit

import com.cmrk.base.BaseNavigator
import com.cmrk.ui.activity.lead.LeadResponseModel

interface LeadNavigator:BaseNavigator {
    fun getLeadListSuccess(responseModel: LeadResponseModel)
}