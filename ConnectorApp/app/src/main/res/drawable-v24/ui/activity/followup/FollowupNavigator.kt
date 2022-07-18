package com.cmrk.ui.activity.visit

import com.cmrk.base.BaseNavigator
import com.cmrk.ui.activity.followup.FollowupResponseModel

interface FollowupNavigator:BaseNavigator {
    fun getFolloupListSuccess(responseModel: FollowupResponseModel)
}