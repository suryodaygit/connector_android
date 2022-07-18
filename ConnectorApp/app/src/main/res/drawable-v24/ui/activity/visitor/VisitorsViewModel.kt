package com.cmrk.ui.activity.visitor

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmrk.base.BaseViewModel
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.util.AppPreference
import com.cmrk.util.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class VisitorsViewModel(private var visitorsNavigator: VisitorsNavigator, context: Context) :
    BaseViewModel<VisitorsNavigator>(visitorsNavigator, context) {

    var layoutManager: LinearLayoutManager
    var visitorsListAdapter: VisitorsListAdapter

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        visitorsListAdapter = VisitorsListAdapter(
            this, ArrayList()
        )
        /*END*/
    }

    fun onVisitorItemClick(visitorsModel: VisitorsModel) {
        visitorsNavigator.onVisitorListItemClick(visitorsModel)
    }

}

