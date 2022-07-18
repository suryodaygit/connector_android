package com.cmrk.ui.activity.target

import com.cmrk.base.BaseNavigator
import com.cmrk.ui.activity.lead.LeadResponseModel
import com.cmrk.ui.activity.target.TargetResponseModel

interface TargetNavigator:BaseNavigator {
    fun getTargetListSuccess(responseModel: TargetResponseModel)
}