package com.cmrk.ui.activity.visitor

import com.cmrk.base.BaseNavigator

interface VisitorsNavigator : BaseNavigator {

//    fun onSurveyFetchList(surveyResModel: SurveyResModel)
//    fun onFailed(error: String)
    fun onVisitorListItemClick(visitorsModel: VisitorsModel)
//    fun clearAdapter()
}