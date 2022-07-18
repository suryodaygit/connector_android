package com.cmrk.ui.profile

import com.cmrk.base.BaseNavigator

interface ProfileNavigator : BaseNavigator {
    fun ongetDashboardDataSuccess(dashboardResponseModel: DashboardResponseModel)
    fun onFailed(error: String)
}