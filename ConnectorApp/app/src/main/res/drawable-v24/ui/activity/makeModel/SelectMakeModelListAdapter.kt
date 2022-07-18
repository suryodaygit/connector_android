package com.cmrk.ui.activity.makeModel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmrk.R
import com.cmrk.databinding.MakeModelListRowBinding


class SelectMakeModelListAdapter(
    private var visitorsViewModel: SelectMakeModelViewModel,
    private var surveyList: List<MakeListResponseModel.Data>
) : RecyclerView.Adapter<SelectMakeModelListAdapter.RoutListAdapterHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RoutListAdapterHolder {
        return RoutListAdapterHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.make_model_list_row,
                viewGroup,
                false
            ) as MakeModelListRowBinding

        )
    }

    override fun getItemCount(): Int {
        return surveyList.size
    }

    fun setData(surveyList: ArrayList<MakeListResponseModel.Data>) {
        this.surveyList = surveyList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(routListAdapterHolder: RoutListAdapterHolder, position: Int) {
        routListAdapterHolder.bindView(visitorsViewModel, surveyList[position])
    }

    class RoutListAdapterHolder(var makeModelListRowBinding: MakeModelListRowBinding) :
        RecyclerView.ViewHolder(makeModelListRowBinding.root) {

        fun bindView(
            dashboardViewModel: SelectMakeModelViewModel,
            makelistresponseModel: MakeListResponseModel.Data
        ) {
            makeModelListRowBinding.selectMakeviewmodel = dashboardViewModel
            makeModelListRowBinding.makemodel = makelistresponseModel
            makeModelListRowBinding.executePendingBindings()
        }

    }

}