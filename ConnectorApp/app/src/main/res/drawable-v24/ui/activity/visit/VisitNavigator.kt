package com.cmrk.ui.activity.visit

import com.cmrk.base.BaseNavigator

interface VisitNavigator:BaseNavigator {
    fun getVisitListSuccess(responseModel: VisitResponseModel)
}