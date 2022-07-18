package com.cmrk.ui.activity.makeModel

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmrk.base.BaseViewModel
import com.cmrk.network.ApiProvider
import com.cmrk.network.ApiRequest
import com.cmrk.network.NetworkCallBack
import com.cmrk.ui.activity.sparepartsSelling.SelectOEMMakelListAdapter
import com.cmrk.util.Controller
import com.cmrk.util.LoadingDialog
import com.cmrk.util.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SelectMakeModelViewModel(
    private var selectMakeModelNavigator: SelectMakeModelNavigator,
    context: Context
) :
    BaseViewModel<SelectMakeModelNavigator>(selectMakeModelNavigator, context), NetworkCallBack {

    var layoutManager: LinearLayoutManager
    var selectMakeModelListAdapter: SelectMakeModelListAdapter

    var modellayoutManager: LinearLayoutManager
    var selectModelListAdapter: SelectModelListAdapter

    var OemMakelayoutManager: LinearLayoutManager
    var selectOEMMakeListAdapter: SelectOEMMakelListAdapter

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        selectMakeModelListAdapter = SelectMakeModelListAdapter(
            this, ArrayList()
        )
        /*END*/
        modellayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        selectModelListAdapter = SelectModelListAdapter(
            this, ArrayList()
        )
        OemMakelayoutManager = GridLayoutManager(context, 2)
        selectOEMMakeListAdapter = SelectOEMMakelListAdapter(
            this, ArrayList()
        )
    }

    fun onMakeItemClick(surveyItem: MakeListResponseModel.Data) {
        selectMakeModelNavigator.onMakeItemClick(surveyItem)
    }

    fun onModelItemClick(surveyItem: ModelListResponseModel.Data) {
        selectMakeModelNavigator.onModelItemClick(surveyItem)
    }

    fun getMakeList() {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .getMakeList(),
                this@SelectMakeModelViewModel
            )
        }
    }

    fun getModelList(make: String) {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                ApiProvider.provideApi(context)
                    .getModelist(make),
                this@SelectMakeModelViewModel
            )
        }
    }

    override fun onSuccess(response: Any?) {
        if (response is MakeListResponseModel) {
            when (response.status) {
                0 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            context,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                }
                1 -> {
                    LoadingDialog.hideLoading()
                    selectMakeModelNavigator.ongetMakeListSuccess(response)
                }
                2 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            context,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(context)
                }
            }
        }else if(response  is ModelListResponseModel){
            when (response.status) {
                0 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            context,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                }
                1 -> {
                    LoadingDialog.hideLoading()
                    selectMakeModelNavigator.ongetModelListSuccess(response)
                }
                2 -> {
                    response?.message?.let {
                        Toaster.showShortToast(
                            context,
                            it
                        )
                    }
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(context)
                }
            }
        }
    }

    override fun onError(errorBody: String) {
    }

}

