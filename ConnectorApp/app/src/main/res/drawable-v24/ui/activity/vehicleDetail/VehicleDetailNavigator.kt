package com.cmrk.ui.activity.vehicleDetail

import com.cmrk.base.BaseNavigator
import com.cmrk.ui.activity.visitor_detail.DeleteImageResponseModel
import com.cmrk.ui.activity.visitor_detail.VisitorPersonAreaDetailResponseModel

interface VehicleDetailNavigator : BaseNavigator {
    fun ongetVehicleDetailSuccess(response: VehicleDetailResponseModel)
    fun ongetVisitorPersonAreaDetailSuccess(response: VisitorPersonAreaDetailResponseModel)
    fun ongetImageDeleteSuccess(response: DeleteImageResponseModel)

}