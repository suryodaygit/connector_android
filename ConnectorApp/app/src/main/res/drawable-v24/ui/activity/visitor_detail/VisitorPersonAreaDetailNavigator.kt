package com.cmrk.ui.activity.visitor_detail

import com.cmrk.base.BaseNavigator

interface VisitorPersonAreaDetailNavigator : BaseNavigator {
    fun ongetVisitorPersonAreaDetailSuccess(response: VisitorPersonAreaDetailResponseModel)
    fun ongetNoOFVisitSuccess(response: NoOfVisitResponseModel)
    fun ongetdeleteImageSuccess(response: DeleteImageResponseModel)
}