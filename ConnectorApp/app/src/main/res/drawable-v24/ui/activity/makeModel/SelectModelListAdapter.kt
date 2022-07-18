package com.cmrk.ui.activity.makeModel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmrk.R
import com.cmrk.databinding.ModelListRowBinding


class SelectModelListAdapter(
    private var visitorsViewModel: SelectMakeModelViewModel,
    private var surveyList: List<ModelListResponseModel.Data>
) : RecyclerView.Adapter<SelectModelListAdapter.RoutListAdapterHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RoutListAdapterHolder {
        return RoutListAdapterHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.model_list_row,
                viewGroup,
                false
            ) as ModelListRowBinding

        )
    }

    override fun getItemCount(): Int {
        return surveyList.size
    }

    fun setData(surveyList: ArrayList<ModelListResponseModel.Data>) {
        this.surveyList = surveyList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(routListAdapterHolder: RoutListAdapterHolder, position: Int) {
        routListAdapterHolder.bindView(visitorsViewModel, surveyList[position])
    }

    class RoutListAdapterHolder(var modelListRowBinding: ModelListRowBinding) :
        RecyclerView.ViewHolder(modelListRowBinding.root) {

        fun bindView(
            dashboardViewModel: SelectMakeModelViewModel,
            sekectmakemodel: ModelListResponseModel.Data
        ) {
            modelListRowBinding.selectMakeviewmodel = dashboardViewModel
            modelListRowBinding.makemodel = sekectmakemodel
            modelListRowBinding.executePendingBindings()
        }

    }

}