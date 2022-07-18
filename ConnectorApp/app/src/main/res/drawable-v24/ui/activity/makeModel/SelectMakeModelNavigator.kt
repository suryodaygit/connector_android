package com.cmrk.ui.activity.makeModel

import com.cmrk.base.BaseNavigator

interface SelectMakeModelNavigator : BaseNavigator {

    //    fun onSurveyFetchList(surveyResModel: SurveyResModel)
//    fun onFailed(error: String)
    fun onMakeItemClick(selectMakeModel: MakeListResponseModel.Data)
    fun onModelItemClick(selectMakeModel: ModelListResponseModel.Data)
    fun ongetMakeListSuccess(response: MakeListResponseModel)
    fun ongetModelListSuccess(response: ModelListResponseModel)
//    fun clearAdapter()
}